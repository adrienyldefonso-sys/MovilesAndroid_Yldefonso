package com.yldefonso.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yldefonso.lavacampus.data.local.relation.PedidoConDetalle
import com.yldefonso.lavacampus.data.repository.LavaRepository
import com.yldefonso.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

enum class FiltroBandeja(val etiqueta: String, val estado: EstadoPedido?) {
    TODOS("Todos", null),
    RECIBIDO("Recibido", EstadoPedido.RECIBIDO),
    EN_PROCESO("En proceso", EstadoPedido.EN_PROCESO),
    LISTO("Listo", EstadoPedido.LISTO)
}

class BandejaViewModel(private val repo: LavaRepository) : ViewModel() {

    private val _filtro = MutableStateFlow(FiltroBandeja.TODOS)
    val filtro: StateFlow<FiltroBandeja> = _filtro.asStateFlow()

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    /** IDs de pedidos atrasados (fechaEntrega menor a hoy y estado activo). */
    val atrasados: StateFlow<Set<Int>> =
        repo.observarIdsAtrasados(LocalDate.now().toString())
            .map { it.toSet() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    /** Filtrado por el chip elegido y por el buscador (nombre del estudiante o número). */
    val pedidos: StateFlow<List<PedidoConDetalle>> =
        combine(repo.observarPedidos(), _filtro, _busqueda) { lista, filtro, texto ->
            lista.filter { p ->
                val coincideEstado = filtro.estado == null || p.pedido.estado == filtro.estado.name
                coincideEstado && coincideBusqueda(p, texto)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun elegirFiltro(filtro: FiltroBandeja) { _filtro.value = filtro }
    fun buscar(texto: String) { _busqueda.value = texto }

    /** Acción rápida: RECIBIDO -> EN_PROCESO. Los demás avances se hacen en la gestión del pedido. */
    fun iniciar(pedido: PedidoConDetalle) {
        if (pedido.pedido.estado != EstadoPedido.RECIBIDO.name) return
        viewModelScope.launch {
            repo.cambiarEstado(pedido.pedido.id, EstadoPedido.EN_PROCESO, null)
        }
    }

    private fun coincideBusqueda(p: PedidoConDetalle, texto: String): Boolean {
        val q = texto.trim().lowercase().removePrefix("#")
        if (q.isEmpty()) return true
        return p.usuario.nombre.lowercase().contains(q) || p.pedido.id.toString().contains(q)
    }
}