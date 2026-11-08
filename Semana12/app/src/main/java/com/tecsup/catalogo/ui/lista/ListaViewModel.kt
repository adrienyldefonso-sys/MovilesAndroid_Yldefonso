package com.tecsup.catalogo.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.catalogo.data.repository.ItemRepository
import com.tecsup.catalogo.domain.model.Item
import com.tecsup.catalogo.notification.Notificador
import com.tecsup.catalogo.ui.UiState
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import retrofit2.HttpException

class ListaViewModel(
    private val repo: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    // true mientras se sincroniza con la API (la UI muestra una barra de progreso).
    private val _actualizando = MutableStateFlow(false)
    val actualizando: StateFlow<Boolean> = _actualizando.asStateFlow()

    private var refrescoTerminado = false

    init {
        viewModelScope.launch {
            repo.items.collect { lista ->
                // Se mantiene Cargando hasta que termine el primer refresco (evita el "vacío" falso).
                if (lista.isNotEmpty() || refrescoTerminado) _estado.value = UiState.Exito(lista)
            }
        }
        refrescar()
    }

    fun refrescar() {
        if (_actualizando.value) return // evita lanzar dos sincronizaciones a la vez
        if (_estado.value is UiState.Error) _estado.value = UiState.Cargando
        _actualizando.value = true
        viewModelScope.launch {
            val resultado = repo.refrescar()
            refrescoTerminado = true
            _actualizando.value = false
            resultado
                .onSuccess { n ->
                    if (n == 0) _estado.value = UiState.Exito(emptyList())
                    _aviso.value = null
                    notificador.mostrar("Datos actualizados", "Se guardaron $n elementos")
                }
                .onFailure { e ->
                    // Sin nada guardado: pantalla de error con Reintentar.
                    // Con datos guardados: se muestra la lista (sale de Room) con un aviso.
                    if (repo.cantidadGuardada() == 0) _estado.value = UiState.Error(mensajeDe(e))
                    else _aviso.value = avisoDe(e)
                }
        }
    }

    fun alternarFavorito(id: Int) {
        viewModelScope.launch { repo.alternarFavorito(id) }
    }

    private fun mensajeDe(e: Throwable) = when (e) {
        is IOException -> "No hay conexión a internet"
        is HttpException -> "El servidor respondió con error ${e.code()}"
        is SerializationException -> "La respuesta del servidor no tiene el formato esperado"
        else -> "Ocurrió un error inesperado"
    }

    private fun avisoDe(e: Throwable) = when (e) {
        is IOException -> "Sin conexión: mostrando datos guardados"
        is HttpException -> "No se pudo actualizar (error ${e.code()}): mostrando datos guardados"
        else -> "No se pudo actualizar: mostrando datos guardados"
    }
}