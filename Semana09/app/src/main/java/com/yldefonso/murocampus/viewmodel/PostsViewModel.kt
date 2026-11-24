package com.yldefonso.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yldefonso.murocampus.data.PostRepository
import com.yldefonso.murocampus.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ViewModel: pide datos al Repository y traduce el Result a un UiState para la pantalla.
// Prohibido aquí: Retrofit, PostApi, HttpException y Context.
class PostsViewModel(private val repo: PostRepository) : ViewModel() {

    // Estado mutable: solo el ViewModel puede cambiarlo.
    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)

    // Versión de solo lectura: la pantalla la observa.
    val lista: StateFlow<UiState<List<Post>>> = _lista.asStateFlow()

    // init: se ejecuta al crear el ViewModel, así la lista carga sola.
    init { cargar() }

    fun cargar() {
        // viewModelScope: la corrutina se cancela si el ViewModel se destruye.
        viewModelScope.launch {
            _lista.value = UiState.Cargando
            // Result -> UiState: éxito => Exito, fallo => Error con mensaje legible.
            repo.listar()
                .onSuccess { _lista.value = UiState.Exito(it) }
                .onFailure { _lista.value = UiState.Error(mensajeDe(it)) }
        }
    }
}