package com.yldefonso.lavacampus.viewmodel

import com.yldefonso.lavacampus.model.Casilleros
import com.yldefonso.lavacampus.model.EstadoPedido

/** Reglas de la máquina de estados. Devuelven el motivo del rechazo, o null si todo está bien. */
object ReglasPedido {

    fun validarAvance(
        actual: EstadoPedido,
        casilleroElegido: String?,
        ocupados: Set<String>,
        pagado: Boolean
    ): String? {
        val siguiente = actual.siguiente() ?: return "Este pedido ya está en un estado final"
        return when (siguiente) {
            EstadoPedido.LISTO -> when {
                casilleroElegido == null -> "Elige un casillero antes de marcar el pedido como listo"
                !Casilleros.esValido(casilleroElegido) -> "El casillero debe ser de C-01 a C-30"
                casilleroElegido in ocupados -> "El casillero $casilleroElegido ya está ocupado por otro pedido"
                else -> null
            }
            EstadoPedido.ENTREGADO ->
                if (!pagado) "Cobra el pedido antes de entregarlo" else null
            else -> null
        }
    }

    /** El administrador cancela solo desde RECIBIDO o EN_PROCESO; nunca LISTO ni ENTREGADO. */
    fun puedeCancelarAdmin(estado: EstadoPedido): Boolean =
        estado == EstadoPedido.RECIBIDO || estado == EstadoPedido.EN_PROCESO
}