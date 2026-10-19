package com.yldefonso.lavacampus.data.repository

import com.yldefonso.lavacampus.data.local.entity.Servicio
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de datos de la app.
 * En el proyecto base solo está activa la sección de Servicios (ejemplo resuelto).
 * Cada bloque comentado se descomenta en el paso indicado, cuando ya existen
 * las entidades que usa (Usuario, PedidoConDetalle, etc.).
 */
interface LavaRepository {
    // Servicios (ejemplo resuelto)
    fun observarServicios(): Flow<List<Servicio>>
    fun observarServiciosActivos(): Flow<List<Servicio>>
    suspend fun guardarServicio(s: Servicio)
    suspend fun eliminarServicio(s: Servicio)
    suspend fun cambiarActivo(id: Int, activo: Boolean)
    // suspend fun servicioTieneDetalles(id: Int): Boolean            // <- Paso 4

    // ---- Paso 4: Usuarios ----
    // suspend fun login(correo: String, clave: String): Usuario?
    // fun observarClientes(): Flow<List<ClienteResumen>>

    // ---- Paso 4: Pedidos ----
    // suspend fun crearPedido(usuarioId: Int, lineas: List<LineaPedido>,
    //                         fechaEntrega: String, nota: String): Long
    // suspend fun contarActivos(usuarioId: Int): Int
    // fun observarPedidosDeUsuario(usuarioId: Int): Flow<List<PedidoConDetalle>>
    // fun observarPedidos(): Flow<List<PedidoConDetalle>>
    // fun observarPedido(id: Int): Flow<PedidoConDetalle?>
    // suspend fun cambiarEstado(id: Int, estado: EstadoPedido, casillero: String?)
    // suspend fun marcarPagado(id: Int, pagado: Boolean)
    // fun observarCasillerosOcupados(): Flow<List<String>>

    // ---- Paso 4: Panel ----
    // fun observarPedidosDeFecha(fecha: String): Flow<Int>
    // fun observarCantidadPorEstado(estado: EstadoPedido): Flow<Int>
    // fun observarIngresosDesde(fechaInicio: String): Flow<Double>
    // fun observarIngresosPorServicio(fechaInicio: String): Flow<List<IngresoServicio>>
}
