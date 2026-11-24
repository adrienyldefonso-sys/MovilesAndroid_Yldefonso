package com.yldefonso.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yldefonso.murocampus.data.PostRepository
import com.yldefonso.murocampus.model.Comentario
import com.yldefonso.murocampus.model.Post
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Datos que necesita la pantalla de detalle: la publicación y sus comentarios.
data class DetalleUi(val post: Post, val comentarios: List<Comentario>)

// Prohibido aquí: Retrofit, PostApi, HttpException y Context.
class PostsViewModel(private val repo: PostRepository) : ViewModel() {

    // ---- Lista ----
    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista: StateFlow<UiState<List<Post>>> = _lista.asStateFlow()

    // ---- Filtro ----
    // null = Todos; un número = solo las publicaciones de ese usuario (1 a 10).
    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro: StateFlow<Int?> = _usuarioFiltro.asStateFlow()

    // Guarda la última carga. Si el usuario cambia el filtro rápido, cancelamos la anterior
    // para que una respuesta vieja no pise a la nueva.
    private var trabajoLista: Job? = null

    init { cargar() }

    fun cargar() {
        trabajoLista?.cancel()
        trabajoLista = viewModelScope.launch {
            _lista.value = UiState.Cargando
            // Si hay filtro usa porUsuario (@Query); si no, listar. El ?: solo se usa cuando el filtro es null.
            val resultado = _usuarioFiltro.value?.let { repo.porUsuario(it) } ?: repo.listar()
            resultado
                .onSuccess { _lista.value = UiState.Exito(it) }
                .onFailure { _lista.value = UiState.Error(mensajeDe(it)) }
        }
    }

    // Guarda el filtro y recarga la lista con él.
    fun filtrar(userId: Int?) {
        _usuarioFiltro.value = userId
        cargar()
    }

    // ---- Detalle ----
    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle: StateFlow<UiState<DetalleUi>> = _detalle.asStateFlow()

    fun abrir(id: Int) {
        viewModelScope.launch {
            _detalle.value = UiState.Cargando
            // async: pide el post y los comentarios AL MISMO TIEMPO, no uno tras otro.
            val (postRes, comRes) = coroutineScope {
                val p = async { repo.detalle(id) }
                val c = async { repo.comentarios(id) }
                p.await() to c.await()
            }
            // Si cualquiera de las dos falló, el detalle completo muestra el error.
            val error = postRes.exceptionOrNull() ?: comRes.exceptionOrNull()
            _detalle.value = if (error != null) {
                UiState.Error(mensajeDe(error))
            } else {
                UiState.Exito(DetalleUi(postRes.getOrThrow(), comRes.getOrThrow()))
            }
        }
    }
}