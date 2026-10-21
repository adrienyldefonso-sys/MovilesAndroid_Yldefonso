package com.yldefonso.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yldefonso.lavacampus.data.local.relation.PedidoConDetalle
import com.yldefonso.lavacampus.data.repository.LavaRepository
import com.yldefonso.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MisPedidosViewModel(
    private val repo: LavaRepository,
    usuarioId: Int
) : ViewModel() {

    private val pedidos = repo.observarPedidosDeUsuario(usuarioId)

    /** RECIBIDO, EN_PROCESO y LISTO. */
    val activos: StateFlow<List<PedidoConDetalle>> = pedidos
        .map { lista -> lista.filter { EstadoPedido.valueOf(it.pedido.estado).esActivo } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** ENTREGADO y CANCELADO. */
    val historial: StateFlow<List<PedidoConDetalle>> = pedidos
        .map { lista -> lista.filter { !EstadoPedido.valueOf(it.pedido.estado).esActivo } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Pedido LISTO con casillero, para la tarjeta "Aviso". */
    val avisoListo: StateFlow<PedidoConDetalle?> = activos
        .map { lista ->
            lista.firstOrNull { it.pedido.estado == EstadoPedido.LISTO.name && it.pedido.casillero != null }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /** Misma regla del detalle: solo se cancela en RECIBIDO. */
    fun puedeCancelar(pedido: PedidoConDetalle): Boolean =
        pedido.pedido.estado == EstadoPedido.RECIBIDO.name

    fun cancelar(pedido: PedidoConDetalle) {
        val actual = activos.value.firstOrNull { it.pedido.id == pedido.pedido.id } ?: return
        if (!puedeCancelar(actual)) return
        viewModelScope.launch {
            repo.cambiarEstado(actual.pedido.id, EstadoPedido.CANCELADO, null)
        }
    }
}