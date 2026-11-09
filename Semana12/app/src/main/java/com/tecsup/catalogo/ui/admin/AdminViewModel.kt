package com.tecsup.catalogo.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.catalogo.data.repository.ItemRepository
import com.tecsup.catalogo.notification.Notificador
import com.tecsup.catalogo.ui.UiState
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import retrofit2.HttpException

/** Totales que muestra el panel. Vive en este archivo para no agregar uno fuera de la estructura. */
data class AdminResumen(val totalGuardado: Int, val totalFavoritos: Int)

class AdminViewModel(
    private val repo: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<AdminResumen>>(UiState.Cargando)
    val estado: StateFlow<UiState<AdminResumen>> = _estado.asStateFlow()

    // true mientras se sincroniza o se limpia (deshabilita los botones).
    private val _ocupado = MutableStateFlow(false)
    val ocupado: StateFlow<Boolean> = _ocupado.asStateFlow()

    // Mensaje de una sola vez (resultado de sincronizar o limpiar).
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    init {
        cargarTotales()
    }

    fun cargarTotales() {
        _estado.value = UiState.Cargando
        viewModelScope.launch { leerTotales() }
    }

    fun sincronizar() {
        if (_ocupado.value) return
        _ocupado.value = true
        viewModelScope.launch {
            repo.refrescar()
                .onSuccess { n ->
                    notificador.mostrar("Datos actualizados", "Se guardaron $n elementos")
                    _mensaje.value = "Se sincronizaron $n elementos"
                }
                .onFailure { e -> _mensaje.value = mensajeDe(e) }
            leerTotales()
            _ocupado.value = false
        }
    }

    fun limpiarCache() {
        if (_ocupado.value) return
        _ocupado.value = true
        viewModelScope.launch {
            try {
                repo.limpiarCache()
                _mensaje.value = "Caché limpiada"
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _mensaje.value = "No se pudo limpiar la caché"
            }
            leerTotales()
            _ocupado.value = false
        }
    }

    fun mensajeMostrado() {
        _mensaje.value = null
    }

    private suspend fun leerTotales() {
        val resultado: UiState<AdminResumen> = try {
            UiState.Exito(AdminResumen(repo.cantidadGuardada(), repo.cantidadFavoritos()))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            UiState.Error("No se pudieron leer los totales guardados")
        }
        _estado.value = resultado
    }

    private fun mensajeDe(e: Throwable) = when (e) {
        is IOException -> "No hay conexión a internet"
        is HttpException -> "El servidor respondió con error ${e.code()}"
        is SerializationException -> "La respuesta del servidor no tiene el formato esperado"
        else -> "Ocurrió un error inesperado"
    }
}