package com.yldefonso.lavacampus.data.repository

import androidx.room3.withWriteTransaction
import com.yldefonso.lavacampus.data.local.LavaDatabase
import com.yldefonso.lavacampus.data.local.entity.DetallePedido
import com.yldefonso.lavacampus.data.local.entity.Pedido
import com.yldefonso.lavacampus.data.local.entity.Servicio
import com.yldefonso.lavacampus.data.local.entity.Usuario
import com.yldefonso.lavacampus.data.local.relation.PedidoConDetalle
import com.yldefonso.lavacampus.model.ClienteResumen
import com.yldefonso.lavacampus.model.EstadoPedido
import com.yldefonso.lavacampus.model.IngresoServicio
import com.yldefonso.lavacampus.model.LineaPedido
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class LavaRepositoryRoom(private val db: LavaDatabase) : LavaRepository {

    private val servicioDao = db.servicioDao()
    private val usuarioDao = db.usuarioDao()
    private val pedidoDao = db.pedidoDao()
    private val detalleDao = db.detalleDao()

    // ---- Usuarios ----
    override suspend fun login(correo: String, clave: String): Usuario? =
        usuarioDao.login(correo, clave)

    override fun observarClientes(): Flow<List<ClienteResumen>> =
        usuarioDao.observarClientes()

    // ---- Servicios (ejemplo resuelto) ----
    override fun observarServicios(): Flow<List<Servicio>> = servicioDao.observarTodos()
    override fun observarServiciosActivos(): Flow<List<Servicio>> = servicioDao.observarActivos()
    override suspend fun guardarServicio(s: Servicio) { servicioDao.insertar(s) }
    override suspend fun eliminarServicio(s: Servicio) = servicioDao.eliminar(s)
    override suspend fun cambiarActivo(id: Int, activo: Boolean) = servicioDao.cambiarActivo(id, activo)
    override suspend fun servicioTieneDetalles(id: Int): Boolean =
        detalleDao.contarPorServicio(id) > 0

    // ---- Pedidos ----
    /**
     * UNA transacción: pedido + todas sus líneas. Si algo falla, se deshace todo
     * y no queda un pedido sin detalles. Congela precioUnitario y subtotal.
     */
    override suspend fun crearPedido(
        usuarioId: Int,
        lineas: List<LineaPedido>,
        fechaEntrega: String,
        nota: String
    ): Long = db.withWriteTransaction {
        val filas = lineas.map { linea ->
            val servicio = servicioDao.obtener(linea.servicioId)
                ?: throw IllegalArgumentException("El servicio ${linea.servicioId} no existe")
            Triple(servicio, linea.cantidad, servicio.precio * linea.cantidad)
        }
        val total = filas.sumOf { it.third }

        val pedidoId = pedidoDao.insertar(
            Pedido(
                usuarioId = usuarioId,
                fechaRegistro = LocalDate.now().toString(),
                fechaEntrega = fechaEntrega,
                estado = EstadoPedido.RECIBIDO.name,
                total = total,
                casillero = null,
                pagado = false,
                nota = nota
            )
        )

        detalleDao.insertarTodos(
            filas.map { (servicio, cantidad, subtotal) ->
                DetallePedido(
                    pedidoId = pedidoId.toInt(),
                    servicioId = servicio.id,
                    cantidad = cantidad,
                    precioUnitario = servicio.precio,
                    subtotal = subtotal
                )
            }
        )
        pedidoId
    }

    override suspend fun contarActivos(usuarioId: Int): Int = pedidoDao.contarActivos(usuarioId)

    override fun observarPedidosDeUsuario(usuarioId: Int): Flow<List<PedidoConDetalle>> =
        pedidoDao.observarPorUsuario(usuarioId)

    override fun observarPedidos(): Flow<List<PedidoConDetalle>> = pedidoDao.observarTodos()

    override fun observarPedido(id: Int): Flow<PedidoConDetalle?> = pedidoDao.observarUno(id)

    /** No valida reglas: viven en el ViewModel. */
    override suspend fun cambiarEstado(id: Int, estado: EstadoPedido, casillero: String?) =
        pedidoDao.actualizarEstado(id, estado.name, casillero)

    override suspend fun marcarPagado(id: Int, pagado: Boolean) =
        pedidoDao.actualizarPagado(id, pagado)

    override fun observarCasillerosOcupados(): Flow<List<String>> =
        pedidoDao.observarCasillerosOcupados()

    // ---- Panel ----
    override fun observarPedidosDeFecha(fecha: String): Flow<Int> = pedidoDao.contarPorFecha(fecha)

    override fun observarCantidadPorEstado(estado: EstadoPedido): Flow<Int> =
        pedidoDao.contarPorEstado(estado.name)

    override fun observarIngresosDesde(fechaInicio: String): Flow<Double> =
        pedidoDao.ingresosDesde(fechaInicio)

    override fun observarIngresosPorServicio(fechaInicio: String): Flow<List<IngresoServicio>> =
        detalleDao.observarIngresosPorServicio(fechaInicio)
}