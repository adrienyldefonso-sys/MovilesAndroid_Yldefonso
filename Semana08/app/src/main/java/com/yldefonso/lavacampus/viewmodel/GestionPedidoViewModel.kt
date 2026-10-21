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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GestionPedidoViewModel(
    private val repo: LavaRepository,
    private val pedidoId: Int
) : ViewModel() {

    val pedido: StateFlow<PedidoConDetalle?> = repo.observarPedido(pedidoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /** Casilleros usados por otros pedidos LISTO. El casillero del propio pedido no cuenta. */
    val casillerosOcupados: StateFlow<Set<String>> =
        combine(repo.observarCasillerosOcupados(), pedido) { lista, p ->
            val propio = p?.pedido?.casillero
            if (propio == null) lista.toSet() else lista.toSet() - propio
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    private val _elegido = MutableStateFlow<String?>(null)

    /** El elegido por el administrador o, si ya está LISTO, el asignado al pedido. */
    val casilleroElegido: StateFlow<String?> =
        combine(_elegido, pedido) { elegido, p -> elegido ?: p?.pedido?.casillero }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun elegirCasillero(c: String) {
        if (c in casillerosOcupados.value) return
        _elegido.value = c
        _mensaje.value = null
    }

    /** Solo al estado siguiente. Si no cumple las reglas, publica el motivo y no cambia nada. */
    fun avanzar() {
        val p = pedido.value?.pedido ?: return
        val actual = EstadoPedido.valueOf(p.estado)

        val error = ReglasPedido.validarAvance(
            actual = actual,
            casilleroElegido = casilleroElegido.value,
            ocupados = casillerosOcupados.value,
            pagado = p.pagado
        )
        if (error != null) {
            _mensaje.value = error
            return
        }

        val siguiente = actual.siguiente() ?: return
        val casillero = if (siguiente == EstadoPedido.LISTO) casilleroElegido.value else p.casillero
        viewModelScope.launch {
            repo.cambiarEstado(pedidoId, siguiente, casillero)
            _elegido.value = null
            _mensaje.value = null
        }
    }

    fun cancelar() {
        val p = pedido.value?.pedido ?: return
        if (!ReglasPedido.puedeCancelarAdmin(EstadoPedido.valueOf(p.estado))) {
            _mensaje.value = "Solo se cancela un pedido Recibido o En proceso"
            return
        }
        viewModelScope.launch {
            repo.cambiarEstado(pedidoId, EstadoPedido.CANCELADO, null)
            _mensaje.value = null
        }
    }

    fun marcarPagado(pagado: Boolean) {
        val p = pedido.value?.pedido ?: return
        val estado = EstadoPedido.valueOf(p.estado)
        when {
            estado == EstadoPedido.CANCELADO -> { _mensaje.value = "Un pedido cancelado no se cobra"; return }
            estado == EstadoPedido.ENTREGADO && !pagado -> {
                _mensaje.value = "Un pedido entregado no puede volver a pendiente de pago"
                return
            }
        }
        viewModelScope.launch {
            repo.marcarPagado(pedidoId, pagado)
            _mensaje.value = null
        }
    }
}