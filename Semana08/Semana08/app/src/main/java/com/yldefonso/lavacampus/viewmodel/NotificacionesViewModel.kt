package com.yldefonso.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yldefonso.lavacampus.data.local.relation.PedidoConDetalle
import com.yldefonso.lavacampus.data.repository.LavaRepository
import com.yldefonso.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

data class Notificacion(
    val pedidoId: Int,
    val titulo: String,
    val detalle: String,
    val estado: EstadoPedido = EstadoPedido.EN_PROCESO,
    val atrasado: Boolean = false
)

class NotificacionesViewModel(
    repo: LavaRepository,
    usuarioId: Int,
    private val esAdmin: Boolean,
    activas: Flow<Boolean>
) : ViewModel() {

    private val _idsLeidos = MutableStateFlow<Set<Int>>(emptySet())

    /** Preferencia "Notificaciones / Avisos" del DataStore. */
    val habilitadas: StateFlow<Boolean> = activas
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    val notificaciones: StateFlow<List<Notificacion>> =
        (if (esAdmin) repo.observarPedidos() else repo.observarPedidosDeUsuario(usuarioId))
            .map { lista -> if (esAdmin) deAdmin(lista) else deEstudiante(lista) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    /** Cuenta solo las notificaciones cuyo pedidoId no ha sido marcado como leído. */
    val conteoSinLeer: StateFlow<Int> = combine(notificaciones, _idsLeidos) { lista, leidos ->
        lista.count { it.pedidoId !in leidos }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    /** Marca todas las notificaciones actuales como leídas para limpiar el contador/badge. */
    fun marcarComoLeidas() {
        _idsLeidos.value = _idsLeidos.value + notificaciones.value.map { it.pedidoId }.toSet()
    }

    private fun deEstudiante(lista: List<PedidoConDetalle>): List<Notificacion> {
        return lista.mapNotNull { pc ->
            val p = pc.pedido
            val estado = EstadoPedido.valueOf(p.estado)
            if (estado == EstadoPedido.LISTO) {
                Notificacion(
                    pedidoId = p.id,
                    titulo = "Tu pedido #${p.id} está listo",
                    detalle = "Recógelo en el casillero ${p.casillero ?: "asignado"}",
                    estado = estado
                )
            } else null
        }
    }

    private fun deAdmin(lista: List<PedidoConDetalle>): List<Notificacion> {
        val hoy = LocalDate.now().toString()
        return lista.mapNotNull { pc ->
            val p = pc.pedido
            val estado = EstadoPedido.valueOf(p.estado)
            val esAtrasado = estado.esActivo && p.fechaEntrega < hoy

            if (estado == EstadoPedido.RECIBIDO || estado == EstadoPedido.EN_PROCESO || esAtrasado) {
                val nombreCliente = pc.usuario.nombre.substringBefore(" ")
                val titulo = if (esAtrasado) "¡Pedido #${p.id} atrasado!" else "Nuevo pedido #${p.id}"
                val detalle = if (esAtrasado) "$nombreCliente espera su pedido" else "$nombreCliente realizó un pedido"

                Notificacion(
                    pedidoId = p.id,
                    titulo = titulo,
                    detalle = detalle,
                    estado = estado,
                    atrasado = esAtrasado
                )
            } else null
        }
    }
}