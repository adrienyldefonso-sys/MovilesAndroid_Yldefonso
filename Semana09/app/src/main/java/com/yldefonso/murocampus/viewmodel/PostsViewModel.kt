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

data class DetalleUi(val post: Post, val comentarios: List<Comentario>)

// Cuántas publicaciones se piden en cada página.
private const val TAMANO_PAGINA = 20

class PostsViewModel(private val repo: PostRepository) : ViewModel() {

    // ---- Lista y paginación ----
    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista: StateFlow<UiState<List<Post>>> = _lista.asStateFlow()

    // true mientras queden páginas por pedir. Se apaga cuando llega una página incompleta.
    private val _hayMas = MutableStateFlow(false)
    val hayMas: StateFlow<Boolean> = _hayMas.asStateFlow()

    // true mientras se pide la siguiente página (desactiva el botón).
    private val _cargandoMas = MutableStateFlow(false)
    val cargandoMas: StateFlow<Boolean> = _cargandoMas.asStateFlow()

    // Error de la última página. La lista ya cargada se conserva.
    private val _errorMas = MutableStateFlow<String?>(null)
    val errorMas: StateFlow<String?> = _errorMas.asStateFlow()

    // ---- Filtro ----
    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro: StateFlow<Int?> = _usuarioFiltro.asStateFlow()

    private var trabajoLista: Job? = null

    init { cargar() }

    fun cargar() {
        trabajoLista?.cancel()
        trabajoLista = viewModelScope.launch {
            _lista.value = UiState.Cargando
            _errorMas.value = null
            val filtro = _usuarioFiltro.value
            if (filtro != null) {
                // Con filtro: cada usuario tiene pocas publicaciones, así que no se pagina.
                _hayMas.value = false
                repo.porUsuario(filtro)
                    .onSuccess { _lista.value = UiState.Exito(it) }
                    .onFailure { _lista.value = UiState.Error(mensajeDe(it)) }
            } else {
                // Sin filtro: solo la primera página (índice 0).
                repo.listarPagina(inicio = 0, limite = TAMANO_PAGINA)
                    .onSuccess {
                        // Si llegaron menos de 20, ya no hay más páginas.
                        _hayMas.value = it.size == TAMANO_PAGINA
                        _lista.value = UiState.Exito(it)
                    }
                    .onFailure { _lista.value = UiState.Error(mensajeDe(it)) }
            }
        }
    }

    // Pide la siguiente página y la añade al final de la lista que ya está en pantalla.
    fun cargarMas() {
        val actual = (_lista.value as? UiState.Exito)?.datos ?: return
        if (!_hayMas.value || _cargandoMas.value) return
        viewModelScope.launch {
            _cargandoMas.value = true
            _errorMas.value = null
            // inicio = cuántas publicaciones ya tenemos, así no se repiten.
            repo.listarPagina(inicio = actual.size, limite = TAMANO_PAGINA)
                .onSuccess { nuevos ->
                    _hayMas.value = nuevos.size == TAMANO_PAGINA
                    _lista.value = UiState.Exito(actual + nuevos)
                }
                .onFailure { _errorMas.value = mensajeDe(it) }
            _cargandoMas.value = false
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
    private val _crear = MutableStateFlow<UiState<Post>?>(null)
    val crear: StateFlow<UiState<Post>?> = _crear.asStateFlow()

    fun publicar(titulo: String, texto: String) {
        viewModelScope.launch {
            _crear.value = UiState.Cargando
            repo.crear(NuevoPost(userId = 1, title = titulo, body = texto))
                .onSuccess { _crear.value = UiState.Exito(it) }
                .onFailure { _crear.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun limpiarCrear() {
        _crear.value = null
    }

    // ---- Editar (PATCH) ----
    private val _editar = MutableStateFlow<UiState<Post>?>(null)
    val editar: StateFlow<UiState<Post>?> = _editar.asStateFlow()

    fun editarTitulo(id: Int, nuevoTitulo: String) {
        viewModelScope.launch {
            _editar.value = UiState.Cargando
            repo.editar(id, nuevoTitulo)
                .onSuccess { _editar.value = UiState.Exito(it) }
                .onFailure { _editar.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun limpiarEditar() {
        _editar.value = null
    }
}