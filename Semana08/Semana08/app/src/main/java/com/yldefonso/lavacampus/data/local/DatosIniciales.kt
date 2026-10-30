package com.yldefonso.lavacampus.data.local

import com.yldefonso.lavacampus.data.local.entity.DetallePedido
import com.yldefonso.lavacampus.data.local.entity.Pedido
import com.yldefonso.lavacampus.data.local.entity.Servicio
import com.yldefonso.lavacampus.data.local.entity.Usuario
import com.yldefonso.lavacampus.model.EstadoPedido
import com.yldefonso.lavacampus.model.Rol
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

object DatosIniciales {

    /** Siembra solo la primera vez (cuando no hay usuarios). Datos ficticios (Ley 29733). */
    suspend fun sembrar(db: LavaDatabase) {
        if (db.usuarioDao().contar() > 0) return

        val servicios = listOf(
            Servicio(id = 1, nombre = "Lavado y secado", descripcion = "Lavado y secado por kilo",
                unidad = "KG", precio = 8.0, horasEntrega = 24, icono = "wash"),
            Servicio(id = 2, nombre = "Planchado", descripcion = "Planchado por prenda",
                unidad = "PRENDA", precio = 2.5, horasEntrega = 48, icono = "iron"),
            Servicio(id = 3, nombre = "Lavado en seco", descripcion = "Para prendas delicadas",
                unidad = "PRENDA", precio = 12.0, horasEntrega = 48, icono = "shirt"),
            Servicio(id = 4, nombre = "Edredón / frazada", descripcion = "Lavado de ropa de cama",
                unidad = "UNIDAD", precio = 20.0, horasEntrega = 48, activo = false, icono = "wash")
        )
        if (db.servicioDao().contar() == 0) db.servicioDao().insertarTodos(servicios)

        db.usuarioDao().insertarTodos(
            listOf(
                Usuario(id = 1, nombre = "Ana Torres", codigo = "U202310234",
                    correo = "ana@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
                Usuario(id = 2, nombre = "Luis Ramos", codigo = "U202310871",
                    correo = "luis@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
                Usuario(id = 3, nombre = "Marta Quispe", codigo = "U202411502",
                    correo = "marta@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
                Usuario(id = 4, nombre = "Administrador", codigo = "",
                    correo = "admin@lavacampus.test", clave = "admin", rol = Rol.ADMIN.name)
            )
        )

        val hoy = LocalDate.now()
        val lunes = hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        // Día de esta semana (lunes + k), sin pasar de hoy: así los ingresos de la semana nunca quedan en cero
        fun semana(k: Long): LocalDate = lunes.plusDays(k).let { if (it.isAfter(hoy)) hoy else it }

        // ENTREGADOS y pagados esta semana
        pedido(db, servicios, 1, semana(0), EstadoPedido.ENTREGADO, true, null, listOf(1 to 4.0, 2 to 6.0))
        pedido(db, servicios, 2, semana(1), EstadoPedido.ENTREGADO, true, null, listOf(1 to 3.0, 3 to 2.0))
        pedido(db, servicios, 3, semana(2), EstadoPedido.ENTREGADO, true, null, listOf(2 to 10.0))
        // CANCELADO
        pedido(db, servicios, 1, hoy.minusDays(3), EstadoPedido.CANCELADO, false, null, listOf(1 to 2.0))
        // LISTOS con casillero
        pedido(db, servicios, 2, hoy.minusDays(2), EstadoPedido.LISTO, false, "C-05", listOf(1 to 5.0, 2 to 4.0))
        pedido(db, servicios, 3, hoy.minusDays(2), EstadoPedido.LISTO, false, "C-07", listOf(3 to 3.0))
        // EN PROCESO
        pedido(db, servicios, 1, hoy.minusDays(1), EstadoPedido.EN_PROCESO, false, null, listOf(1 to 6.0))
        pedido(db, servicios, 2, hoy.minusDays(1), EstadoPedido.EN_PROCESO, false, null, listOf(2 to 8.0))
        // RECIBIDOS de hoy
        pedido(db, servicios, 1, hoy, EstadoPedido.RECIBIDO, false, null, listOf(1 to 3.5, 2 to 5.0))
        pedido(db, servicios, 3, hoy, EstadoPedido.RECIBIDO, false, null, listOf(1 to 2.0))
    }

    /** Inserta un pedido y sus detalles; el total siempre es la suma de los subtotales. */
    private suspend fun pedido(
        db: LavaDatabase,
        servicios: List<Servicio>,
        usuarioId: Int,
        registro: LocalDate,
        estado: EstadoPedido,
        pagado: Boolean,
        casillero: String?,
        lineas: List<Pair<Int, Double>>
    ) {
        val filas = lineas.map { (servicioId, cantidad) ->
            val s = servicios.first { it.id == servicioId }
            Triple(s, cantidad, s.precio * cantidad)
        }
        val total = filas.sumOf { it.third }

        val dias = filas.maxOf { it.first.horasEntrega } / 24
        var entrega = registro.plusDays(dias.toLong())
        if (entrega.dayOfWeek == DayOfWeek.SUNDAY) entrega = entrega.plusDays(1)

        val pedidoId = db.pedidoDao().insertar(
            Pedido(
                usuarioId = usuarioId,
                fechaRegistro = registro.toString(),
                fechaEntrega = entrega.toString(),
                estado = estado.name,
                total = total,
                casillero = casillero,
                pagado = pagado,
                nota = ""
            )
        ).toInt()

        db.detalleDao().insertarTodos(
            filas.map { (s, cantidad, subtotal) ->
                DetallePedido(
                    pedidoId = pedidoId,
                    servicioId = s.id,
                    cantidad = cantidad,
                    precioUnitario = s.precio,
                    subtotal = subtotal
                )
            }
        )
    }
}