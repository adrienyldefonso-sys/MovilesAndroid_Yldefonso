package com.yldefonso.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yldefonso.murocampus.data.PostRepository
import com.yldefonso.murocampus.model.Comentario
import com.yldefonso.murocampus.model.NuevoPost
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

    // Guarda la última carga para cancelarla si el usuario cambia el filtro rápido.
    private var trabajoLista: Job? = null

    init { cargar() }

    fun cargar() {
        trabajoLista?.cancel()
        trabajoLista = viewModelScope.launch {
            _lista.value = UiState.Cargando
            // Con filtro usa porUsuario (@Query); sin filtro, listar.
            val resultado = _usuarioFiltro.value?.let { repo.porUsuario(it) } ?: repo.listar()
            resultado
                .onSuccess { _lista.value = UiState.Exito(it) }
                .onFailure { _lista.value = UiState.Error(mensajeDe(it)) }
        }
    }

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
            // async: pide el post y los comentarios AL MISMO TIEMPO.
            val (postRes, comRes) = coroutineScope {
                val p = async { repo.detalle(id) }
                val c = async { repo.comentarios(id) }
                p.await() to c.await()
            }
            val error = postRes.exceptionOrNull() ?: comRes.exceptionOrNull()
            _detalle.value = if (error != null) {
                UiState.Error(mensajeDe(error))
            } else {
                UiState.Exito(DetalleUi(postRes.getOrThrow(), comRes.getOrThrow()))
            }
        }
    }

    // ---- Crear ----
    // null = todavía no se envió nada; Cargando / Exito / Error según el POST.
    private val _crear = MutableStateFlow<UiState<Post>?>(null)
    val crear: StateFlow<UiState<Post>?> = _crear.asStateFlow()

    fun publicar(titulo: String, texto: String) {
        viewModelScope.launch {
            _crear.value = UiState.Cargando
            // userId = 1 es el usuario que publica; JSONPlaceholder responde con id 101.
            repo.crear(NuevoPost(userId = 1, title = titulo, body = texto))
                .onSuccess { _crear.value = UiState.Exito(it) }
                .onFailure { _crear.value = UiState.Error(mensajeDe(it)) }
        }
    }

    // Vuelve el estado de crear a null (al abrir o cerrar la pantalla).
    fun limpiarCrear() {
        _crear.value = null
    }
}