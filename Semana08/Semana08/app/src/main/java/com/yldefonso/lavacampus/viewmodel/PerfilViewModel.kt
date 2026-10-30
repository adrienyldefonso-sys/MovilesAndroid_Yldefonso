package com.yldefonso.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yldefonso.lavacampus.data.repository.LavaRepository
import com.yldefonso.lavacampus.model.EstadoPedido
import com.yldefonso.lavacampus.model.Unidad
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PerfilViewModel(repo: LavaRepository, usuarioId: Int) : ViewModel() {

    private val pedidos = repo.observarPedidosDeUsuario(usuarioId)

    val pedidosActivos: StateFlow<Int> = pedidos
        .map { l -> l.count { EstadoPedido.valueOf(it.pedido.estado).esActivo } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    /** Pedidos totales, sin cancelados. */
    val pedidosTotales: StateFlow<Int> = pedidos
        .map { l -> l.count { it.pedido.estado != EstadoPedido.CANCELADO.name } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    /** Kilos de los pedidos ya entregados. */
    val kilosLavados: StateFlow<Double> = pedidos
        .map { l ->
            l.filter { it.pedido.estado == EstadoPedido.ENTREGADO.name }
                .sumOf { pc ->
                    pc.detalles
                        .filter { Unidad.desde(it.servicio.unidad) == Unidad.KG }
                        .sumOf { it.detalle.cantidad }
                }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
}