package com.yldefonso.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yldefonso.lavacampus.data.local.entity.Servicio
import com.yldefonso.lavacampus.data.repository.LavaRepository
import com.yldefonso.lavacampus.model.LineaPedido
import com.yldefonso.lavacampus.model.Unidad
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

/** Paso, mínimo y máximo de cantidad según la unidad del servicio. */
data class ReglaCantidad(val paso: Double, val minimo: Double, val maximo: Double)

fun Servicio.regla(): ReglaCantidad =
    if (Unidad.desde(unidad) == Unidad.KG) ReglaCantidad(paso = 0.5, minimo = 1.0, maximo = 10.0)
    else ReglaCantidad(paso = 1.0, minimo = 1.0, maximo = 30.0)

/** Una línea del resumen: servicio elegido, cantidad y subtotal. */
data class LineaResumen(val servicio: Servicio, val cantidad: Double, val subtotal: Double)

/** Hoy + horas de entrega (24 h = 1 día). Si cae domingo, pasa al lunes. */
fun calcularFechaEntrega(horas: Int, desde: LocalDate = LocalDate.now()): LocalDate {
    var fecha = desde.plusDays((horas / 24).toLong())
    if (fecha.dayOfWeek == DayOfWeek.SUNDAY) fecha = fecha.plusDays(1)
    return fecha
}

private const val MAX_PEDIDOS_ACTIVOS = 3

class NuevoPedidoViewModel(
    private val repo: LavaRepository,
    private val usuarioId: Int,
    recibirPedidos: Flow<Boolean>
) : ViewModel() {

    /** Si el administrador apagó "Recibir pedidos", no se pueden crear pedidos. */
    val recibiendo: StateFlow<Boolean> = recibirPedidos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    /** Solo servicios ACTIVOS. */
    val servicios: StateFlow<List<Servicio>> = repo.observarServiciosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _cantidades = MutableStateFlow<Map<Int, Double>>(emptyMap())
    val cantidades: StateFlow<Map<Int, Double>> = _cantidades.asStateFlow()

    private val _nota = MutableStateFlow("")
    val nota: StateFlow<String> = _nota.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _guardando = MutableStateFlow(false)
    val guardando: StateFlow<Boolean> = _guardando.asStateFlow()

    private val _pedidoCreado = MutableStateFlow(false)
    val pedidoCreado: StateFlow<Boolean> = _pedidoCreado.asStateFlow()

    /** Una línea por servicio elegido (cantidad mayor que 0). */
    val resumen: StateFlow<List<LineaResumen>> = combine(servicios, cantidades) { lista, cant ->
        lista.mapNotNull { s ->
            val c = cant[s.id] ?: 0.0
            if (c > 0.0) LineaResumen(s, c, c * s.precio) else null
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Se recalcula en cada cambio: suma de cantidad × precio. */
    val total: StateFlow<Double> = resumen
        .map { lineas -> lineas.sumOf { it.subtotal } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    /** Hoy + el mayor tiempo de entrega de los servicios elegidos; null si no hay ninguno. */
    val fechaEntrega: StateFlow<LocalDate?> = resumen
        .map { lineas ->
            lineas.maxOfOrNull { it.servicio.horasEntrega }?.let { calcularFechaEntrega(it) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /** El botón se habilita solo si hay productos y la lavandería está recibiendo pedidos. */
    val puedeConfirmar: StateFlow<Boolean> = combine(resumen, recibiendo) { lineas, abierto ->
        lineas.isNotEmpty() && abierto
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun cambiarNota(texto: String) { _nota.value = texto }

    /**
     * KG: pasos de 0.5, mínimo 1.0 al activar, máximo 10.0.
     * PRENDA y UNIDAD: pasos de 1, máximo 30. Con cantidad 0 el servicio queda apagado.
     */
    fun cambiarCantidad(servicioId: Int, delta: Double) {
        val servicio = servicios.value.firstOrNull { it.id == servicioId } ?: return
        val regla = servicio.regla()
        val actual = _cantidades.value[servicioId] ?: 0.0

        var nuevo = actual + delta
        if (nuevo < regla.minimo) nuevo = 0.0          // por debajo del mínimo: se apaga
        nuevo = nuevo.coerceAtMost(regla.maximo)

        _cantidades.value = _cantidades.value.toMutableMap().also {
            if (nuevo <= 0.0) it.remove(servicioId) else it[servicioId] = nuevo
        }
        _mensaje.value = null
    }

    fun confirmar() {
        if (!recibiendo.value) {
            _mensaje.value = "La lavandería no está recibiendo pedidos"
            return
        }
        if (_guardando.value) return
        val lineas = resumen.value.map { LineaPedido(it.servicio.id, it.cantidad) }
        val fecha = fechaEntrega.value
        if (lineas.isEmpty() || fecha == null) return

        viewModelScope.launch {
            _guardando.value = true
            try {
                // Regla: máximo 3 pedidos activos por estudiante
                if (repo.contarActivos(usuarioId) >= MAX_PEDIDOS_ACTIVOS) {
                    _mensaje.value = "Ya tienes 3 pedidos activos"
                    return@launch
                }
                repo.crearPedido(usuarioId, lineas, fecha.toString(), _nota.value.trim())
                _pedidoCreado.value = true
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _mensaje.value = "No se pudo crear el pedido. Intenta de nuevo"
            } finally {
                _guardando.value = false
            }
        }
    }
}