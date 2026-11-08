package com.tecsup.catalogo.ui.detalle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.catalogo.data.repository.ItemRepository
import com.tecsup.catalogo.domain.model.Item
import com.tecsup.catalogo.ui.UiState
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import retrofit2.HttpException

class DetalleViewModel(
    private val repo: ItemRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // El id llega como argumento de navegación ("detalle/{id}").
    private val id: Int = checkNotNull(savedStateHandle["id"])

    private val _estado = MutableStateFlow<UiState<Item>>(UiState.Cargando)
    val estado: StateFlow<UiState<Item>> = _estado.asStateFlow()

    init {
        cargar()
    }

    fun cargar() {
        _estado.value = UiState.Cargando
        viewModelScope.launch {
            repo.obtenerDetalle(id)
                .onSuccess { _estado.value = UiState.Exito(it) }
                .onFailure { _estado.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun alternarFavorito() {
        viewModelScope.launch {
            repo.alternarFavorito(id)
            val actual = _estado.value
            if (actual is UiState.Exito) {
                _estado.value = UiState.Exito(actual.datos.copy(esFavorito = !actual.datos.esFavorito))
            }
        }
    }

    private fun mensajeDe(e: Throwable) = when (e) {
        is IOException -> "No hay conexión a internet"
        is HttpException -> "El servidor respondió con error ${e.code()}"
        is SerializationException -> "La respuesta del servidor no tiene el formato esperado"
        else -> "Ocurrió un error inesperado"
    }
}