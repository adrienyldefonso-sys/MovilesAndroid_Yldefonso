package com.yldefonso.campusgps.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yldefonso.campusgps.data.LugaresRepository
import com.yldefonso.campusgps.data.UbicacionRepository
import com.yldefonso.campusgps.model.LugarConDistancia
import com.yldefonso.campusgps.model.Posicion
import com.yldefonso.campusgps.model.UbicacionUi
import com.yldefonso.campusgps.util.distanciaMetros
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * [G] Guiado. Recibe SOLO interfaces (nunca un Context ni Composables).
 * Paso 5: calcula la distancia a cada lugar y ordena la lista de menor a mayor.
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

    // Id del lugar tocado en la lista (null = ninguno). Se usará en el Paso 6.
    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado: StateFlow<Int?> = _seleccionado.asStateFlow()

    /** Se llama al pulsar "Obtener mi ubicación" (ya con permiso concedido). */
    fun buscar() {
        _estado.value = UbicacionUi.Buscando          // la View deshabilita el botón
        viewModelScope.launch {                       // corrutina atada a la vida del ViewModel
            ubicacionRepo.obtenerPosicion()
                .onSuccess {
                    _estado.value = UbicacionUi.Lista(it)
                    actualizarDistancias(it)          // Paso 5: SOLO se llama cuando la ubicación llega con éxito
                }
                .onFailure { _estado.value = UbicacionUi.Error(mensajeDe(it)) }
        }
    }

    /** La View avisa que el usuario negó el permiso. */
    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error("Sin permiso de ubicación no puedo mostrar dónde estás")
    }

    /**
     * Paso 5: crea la lista con la distancia de cada lugar, la ordena de menor a mayor
     * y la publica en _lugares. La View solo recibe el resultado ya calculado.
     */
    private fun actualizarDistancias(pos: Posicion) {
        val ordenada = lugaresRepo.obtenerLugares()
            .map { LugarConDistancia(it, distanciaMetros(pos, it)) }   // lugar + su distancia en metros
            .sortedBy { it.metros }                                    // más cercano primero
        _lugares.value = ordenada

        // Verificación temporal del Paso 5 (la pantalla aún no muestra la lista): mira Logcat, filtro "CampusGPS".
        ordenada.forEach { Log.d("CampusGPS", "${it.lugar.nombre}: ${it.metros} m") }
    }

    // TODO Paso 6: fun seleccionar(id: Int?)
    // TODO Paso 7: val masCercano: StateFlow<Lugar?>

    // Traduce una excepción técnica a un mensaje entendible (se mejorará en el Paso 8).
    private fun mensajeDe(e: Throwable): String = when (e) {
        is SecurityException -> "Falta el permiso de ubicación"
        else -> e.message ?: "No se pudo obtener la ubicación"
    }
}