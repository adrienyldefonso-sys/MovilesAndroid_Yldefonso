package com.yldefonso.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yldefonso.campusgps.data.LugaresRepository
import com.yldefonso.campusgps.data.UbicacionRepository
import com.yldefonso.campusgps.model.Lugar
import com.yldefonso.campusgps.model.LugarConDistancia
import com.yldefonso.campusgps.model.Posicion
import com.yldefonso.campusgps.model.UbicacionUi
import com.yldefonso.campusgps.util.distanciaMetros
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * [G] Guiado. Recibe SOLO interfaces (nunca un Context ni Composables).
 * Paso 5: calcula distancias y ordena. Paso 6: guarda el lugar seleccionado.
 * Paso 7: deriva cuál es el lugar más cercano.
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

    // Id del lugar tocado en la lista (null = ninguno).
    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado: StateFlow<Int?> = _seleccionado.asStateFlow()

    /**
     * Paso 7: lugar más cercano, DERIVADO de _lugares (no se guarda aparte, así nunca se desfasa).
     * Como la lista ya está ordenada de menor a mayor, basta el primero con distancia calculada.
     * Es null mientras no haya ubicación (todas las distancias son null).
     * stateIn convierte el Flow en StateFlow; WhileSubscribed(5000) lo mantiene activo 5 s
     * tras rotar la pantalla para no recalcular.
     */
    val masCercano: StateFlow<Lugar?> = _lugares
        .map { lista -> lista.firstOrNull { it.metros != null }?.lugar }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /** Se llama al pulsar "Obtener mi ubicación" (ya con permiso concedido). */
    fun buscar() {
        _estado.value = UbicacionUi.Buscando          // la View deshabilita el botón
        viewModelScope.launch {                       // corrutina atada a la vida del ViewModel
            ubicacionRepo.obtenerPosicion()
                .onSuccess {
                    _estado.value = UbicacionUi.Lista(it)
                    actualizarDistancias(it)          // SOLO se llama cuando la ubicación llega con éxito
                }
                .onFailure { _estado.value = UbicacionUi.Error(mensajeDe(it)) }
        }
    }

    /** La View avisa que el usuario negó el permiso. */
    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error("Sin permiso de ubicación no puedo mostrar dónde estás")
    }

    /** La View avisa qué lugar tocó el usuario (null = quitar la selección). */
    fun seleccionar(id: Int?) {
        _seleccionado.value = id
    }

    /**
     * Crea la lista con la distancia de cada lugar, la ordena de menor a mayor
     * y la publica en _lugares. La View solo recibe el resultado ya calculado.
     */
    private fun actualizarDistancias(pos: Posicion) {
        _lugares.value = lugaresRepo.obtenerLugares()
            .map { LugarConDistancia(it, distanciaMetros(pos, it)) }   // lugar + su distancia en metros
            .sortedBy { it.metros }                                    // más cercano primero
    }

    // Traduce una excepción técnica a un mensaje entendible (se mejorará en el Paso 8).
    private fun mensajeDe(e: Throwable): String = when (e) {
        is SecurityException -> "Falta el permiso de ubicación"
        else -> e.message ?: "No se pudo obtener la ubicación"
    }
}