package com.yldefonso.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yldefonso.campusgps.data.LugaresRepository
import com.yldefonso.campusgps.data.UbicacionNoDisponibleException
import com.yldefonso.campusgps.data.UbicacionRepository
import com.yldefonso.campusgps.model.Lugar
import com.yldefonso.campusgps.model.LugarConDistancia
import com.yldefonso.campusgps.model.Posicion
import com.yldefonso.campusgps.model.TipoError
import com.yldefonso.campusgps.model.UbicacionUi
import com.yldefonso.campusgps.util.distanciaMetros
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * [G] Guiado. Recibe SOLO interfaces (nunca un Context ni Composables).
 * Paso 5: distancias. Paso 6: selección. Paso 7: más cercano. Paso 8: errores con tipo.
 * Fase 2: Radio de cercanía y Filtrado por categoría.
 */
class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModel() {

    // Patrón estándar: _estado es privado y mutable; "estado" es público y de solo lectura.
    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado: StateFlow<UbicacionUi> = _estado.asStateFlow()

    // Lista inicial: los 5 lugares con metros = null (aún no sabemos dónde está el usuario).
    private val _lugares = MutableStateFlow(
        lugaresRepo.obtenerLugares().map { LugarConDistancia(it, null) }
    )
    val lugares: StateFlow<List<LugarConDistancia>> = _lugares.asStateFlow()

    // Categorías disponibles obtenidas directamente de los datos (sin escribir a mano en la vista).
    val categoriasDisponibles: List<String> = lugaresRepo.obtenerLugares().map { it.categoria }.distinct()

    // Categorías actualmente activas en el filtro (vacío = se muestran todos los lugares).
    private val _categoriasActivas = MutableStateFlow<Set<String>>(emptySet())
    val categoriasActivas: StateFlow<Set<String>> = _categoriasActivas.asStateFlow()

    // Lista de lugares visible según el filtro de categorías, DERIVADA de _lugares y _categoriasActivas.
    val lugaresVisibles: StateFlow<List<LugarConDistancia>> = combine(_lugares, _categoriasActivas) { lista, activas ->
        if (activas.isEmpty()) lista else lista.filter { activas.contains(it.lugar.categoria) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _lugares.value)

    // Id del lugar tocado en la lista (null = ninguno).
    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado: StateFlow<Int?> = _seleccionado.asStateFlow()

    // Radio de cercanía seleccionado (por defecto 200 metros).
    private val _radioMetros = MutableStateFlow(200)
    val radioMetros: StateFlow<Int> = _radioMetros.asStateFlow()

    // Ids de los lugares dentro del radio y visibles, DERIVADO de lugaresVisibles y radioMetros.
    val idsDentroDelRadio: StateFlow<Set<Int>> = combine(lugaresVisibles, _radioMetros) { lista, radio ->
        lista.filter { it.metros != null && it.metros <= radio }.map { it.lugar.id }.toSet()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    // Lugar más cercano, DERIVADO de lugaresVisibles (calculado SOBRE LOS LUGARES FILTRADOS).
    val masCercano: StateFlow<Lugar?> = lugaresVisibles
        .map { lista -> lista.firstOrNull { it.metros != null }?.lugar }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        // Si el lugar seleccionado queda oculto por el filtro de categorías, limpia la selección.
        viewModelScope.launch {
            lugaresVisibles.collect { visibles ->
                val actual = _seleccionado.value
                if (actual != null && visibles.none { it.lugar.id == actual }) {
                    _seleccionado.value = null
                }
            }
        }
    }

    /** Se llama al pulsar "Obtener mi ubicación" (ya con permiso concedido). */
    fun buscar() {
        // Paso 8: antes de buscar, verifica Play Services a través de la interface (sin Context).
        if (!ubicacionRepo.playServicesDisponible()) {
            _estado.value = UbicacionUi.Error(
                "Este dispositivo no tiene Google Play Services",
                TipoError.SIN_PLAY_SERVICES
            )
            return                                    // no sigue: no tiene sentido reintentar
        }

        _estado.value = UbicacionUi.Buscando          // la View deshabilita el botón
        viewModelScope.launch {                       // corrutina atada a la vida del ViewModel
            ubicacionRepo.obtenerPosicion()
                .onSuccess {
                    _estado.value = UbicacionUi.Lista(it)
                    actualizarDistancias(it)          // SOLO se llama cuando la ubicación llega con éxito
                }
                .onFailure { _estado.value = errorDe(it) }
        }
    }

    /** La View avisa que el usuario negó el permiso. */
    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error(
            "Sin permiso de ubicación no puedo mostrar dónde estás",
            TipoError.SIN_PERMISO                     // la View mostrará el botón "Abrir ajustes"
        )
    }

    /** La View avisa qué lugar tocó el usuario (null = quitar la selección). */
    fun seleccionar(id: Int?) {
        _seleccionado.value = id
    }

    /** Cambia el radio de cercanía seleccionado por el usuario (100m, 200m, 500m). */
    fun cambiarRadio(metros: Int) {
        _radioMetros.value = metros
    }

    /** Alterna la activación de una categoría en el filtro de la vista. */
    fun alternarCategoria(categoria: String) {
        val actual = _categoriasActivas.value
        _categoriasActivas.value = if (actual.contains(categoria)) {
            actual - categoria
        } else {
            actual + categoria
        }
    }

    /** Crea la lista con la distancia de cada lugar, la ordena de menor a mayor y la publica. */
    private fun actualizarDistancias(pos: Posicion) {
        _lugares.value = lugaresRepo.obtenerLugares()
            .map { LugarConDistancia(it, distanciaMetros(pos, it)) }   // lugar + su distancia en metros
            .sortedBy { it.metros }                                    // más cercano primero
    }

    /**
     * Paso 8: traduce una excepción técnica a un Error con mensaje claro y un tipo.
     * El tipo decide qué acción ofrece la pantalla.
     */
    private fun errorDe(e: Throwable): UbicacionUi.Error = when (e) {
        is SecurityException -> UbicacionUi.Error(
            "Sin permiso de ubicación no puedo mostrar dónde estás", TipoError.SIN_PERMISO
        )
        is UbicacionNoDisponibleException -> UbicacionUi.Error(
            "No se pudo obtener la ubicación. Activa el GPS e intenta de nuevo", TipoError.GPS_APAGADO
        )
        else -> UbicacionUi.Error(
            e.message ?: "No se pudo obtener la ubicación", TipoError.OTRO
        )
    }
}
