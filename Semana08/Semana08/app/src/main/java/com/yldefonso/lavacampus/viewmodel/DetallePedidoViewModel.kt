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

class DetallePedidoViewModel(
    private val repo: LavaRepository,
    private val pedidoId: Int
) : ViewModel() {

    /** Si el administrador cambia el estado, esta pantalla se actualiza sola (Flow de Room). */
    val pedido: StateFlow<PedidoConDetalle?> = repo.observarPedido(pedidoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /** Regla del estudiante: solo se cancela en RECIBIDO (aún no empezó el lavado). */
    val puedeCancelar: StateFlow<Boolean> = pedido
        .map { it?.pedido?.estado == EstadoPedido.RECIBIDO.name }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun cancelar() {
        // Se vuelve a validar al ejecutar: el estado pudo cambiar mientras el diálogo estaba abierto
        if (pedido.value?.pedido?.estado != EstadoPedido.RECIBIDO.name) return
        viewModelScope.launch {
            repo.cambiarEstado(pedidoId, EstadoPedido.CANCELADO, null)
        }
    }
}