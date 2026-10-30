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

class InicioViewModel(repo: LavaRepository, usuarioId: Int) : ViewModel() {

    // El DAO ya los ordena por id descendente: el primero es el más reciente
    private val pedidos = repo.observarPedidosDeUsuario(usuarioId)

    /** El pedido más reciente en estado RECIBIDO, EN_PROCESO o LISTO. */
    val pedidoActivo: StateFlow<PedidoConDetalle?> = pedidos
        .map { lista -> lista.firstOrNull { EstadoPedido.valueOf(it.pedido.estado).esActivo } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /** Los 2 últimos pedidos. */
    val recientes: StateFlow<List<PedidoConDetalle>> = pedidos
        .map { it.take(2) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}