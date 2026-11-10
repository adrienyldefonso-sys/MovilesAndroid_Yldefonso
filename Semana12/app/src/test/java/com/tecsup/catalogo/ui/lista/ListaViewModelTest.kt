package com.tecsup.catalogo.ui.lista

import com.tecsup.catalogo.data.repository.ItemRepository
import com.tecsup.catalogo.domain.model.Item
import com.tecsup.catalogo.notification.Notificador
import com.tecsup.catalogo.ui.UiState
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Regla de JUnit para configurar el Dispatcher principal de corrutinas en pruebas unitarias.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

/**
 * Implementación falsa de ItemRepository para pruebas unitarias de ListaViewModel.
 */
class FakeItemRepository : ItemRepository {
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    override val items: Flow<List<Item>> = _items.asStateFlow()

    private val _favoritos = MutableStateFlow<List<Item>>(emptyList())
    override val favoritos: Flow<List<Item>> = _favoritos.asStateFlow()

    // Resultado configurable para la función refrescar()
    var resultadoRefrescar: Result<Int> = Result.success(0)

    // Registro de IDs recibidos al alternar favorito
    val idsAlternados = mutableListOf<Int>()

    fun setItems(nuevaLista: List<Item>) {
        _items.value = nuevaLista
    }

    override suspend fun refrescar(): Result<Int> {
        return resultadoRefrescar
    }

    override suspend fun obtenerDetalle(id: Int): Result<Item> {
        val item = _items.value.find { it.id == id }
        return if (item != null) Result.success(item) else Result.failure(Exception("No encontrado"))
    }

    override suspend fun alternarFavorito(id: Int) {
        idsAlternados.add(id)
    }

    override suspend fun cantidadGuardada(): Int {
        return _items.value.size
    }

    override suspend fun cantidadFavoritos(): Int {
        return _favoritos.value.size
    }

    override suspend fun limpiarCache() {
        _items.value = emptyList()
    }
}

/**
 * Implementación falsa de Notificador para almacenar mensajes de notificación en pruebas.
 */
class FakeNotificador : Notificador {
    val mensajes = mutableListOf<Pair<String, String>>()

    override fun mostrar(titulo: String, texto: String) {
        mensajes.add(Pair(titulo, texto))
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class ListaViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @Test
    fun sincronizacion_exitosa_con_datos() = runTest {
        val fakeRepository = FakeItemRepository()
        val fakeNotificador = FakeNotificador()

        // Preparar datos y resultado exitoso con 2 elementos
        val itemsPrueba = listOf(
            Item(1, "Item 1", "Marca A", null, 10.0),
            Item(2, "Item 2", "Marca B", null, 20.0)
        )
        fakeRepository.setItems(itemsPrueba)
        fakeRepository.resultadoRefrescar = Result.success(2)

        // Inicializar ViewModel (ejecuta init con colección y refrescar())
        val viewModel = ListaViewModel(fakeRepository, fakeNotificador)

        // Verificar que el estado sea UiState.Exito con los items
        val estadoActual = viewModel.estado.value
        assertTrue(estadoActual is UiState.Exito)
        assertEquals(itemsPrueba, (estadoActual as UiState.Exito).datos)

        // Verificar que el notificador recibió el mensaje con "Se guardaron 2 elementos"
        assertTrue(fakeNotificador.mensajes.any { it.second == "Se guardaron 2 elementos" })
    }

    @Test
    fun sincronizacion_exitosa_con_cero_elementos() = runTest {
        val fakeRepository = FakeItemRepository()
        val fakeNotificador = FakeNotificador()

        // Preparar resultado exitoso con 0 elementos
        fakeRepository.setItems(emptyList())
        fakeRepository.resultadoRefrescar = Result.success(0)

        // Inicializar ViewModel
        val viewModel = ListaViewModel(fakeRepository, fakeNotificador)

        // Verificar que el estado sea UiState.Exito con lista vacía
        val estadoActual = viewModel.estado.value
        assertTrue(estadoActual is UiState.Exito)
        assertEquals(emptyList<Item>(), (estadoActual as UiState.Exito).datos)

        // Verificar que el notificador recibió el mensaje con "Se guardaron 0 elementos"
        assertTrue(fakeNotificador.mensajes.any { it.second == "Se guardaron 0 elementos" })
    }

    @Test
    fun falla_con_io_exception_y_sin_datos_guardados() = runTest {
        val fakeRepository = FakeItemRepository()
        val fakeNotificador = FakeNotificador()

        // Preparar repositorio sin datos (cantidadGuardada = 0) y falla IOException
        fakeRepository.setItems(emptyList())
        fakeRepository.resultadoRefrescar = Result.failure(IOException("Fallo de red"))

        // Inicializar ViewModel
        val viewModel = ListaViewModel(fakeRepository, fakeNotificador)

        // Verificar que el estado sea UiState.Error con "No hay conexión a internet"
        val estadoActual = viewModel.estado.value
        assertTrue(estadoActual is UiState.Error)
        assertEquals("No hay conexión a internet", (estadoActual as UiState.Error).mensaje)
    }

    @Test
    fun falla_con_io_exception_y_con_datos_guardados() = runTest {
        val fakeRepository = FakeItemRepository()
        val fakeNotificador = FakeNotificador()

        // Preparar repositorio con datos guardados y falla IOException
        val itemsGuardados = listOf(
            Item(1, "Item Guardado", "Marca X", null, 15.0)
        )
        fakeRepository.setItems(itemsGuardados)
        fakeRepository.resultadoRefrescar = Result.failure(IOException("Fallo de red"))

        // Inicializar ViewModel
        val viewModel = ListaViewModel(fakeRepository, fakeNotificador)

        // Verificar que el estado sea UiState.Exito con la lista guardada
        val estadoActual = viewModel.estado.value
        assertTrue(estadoActual is UiState.Exito)
        assertEquals(itemsGuardados, (estadoActual as UiState.Exito).datos)

        // Verificar que el aviso sea "Sin conexión: mostrando datos guardados"
        assertEquals("Sin conexión: mostrando datos guardados", viewModel.aviso.value)
    }

    @Test
    fun alternar_favorito_llama_al_repositorio_con_ese_id() = runTest {
        val fakeRepository = FakeItemRepository()
        val fakeNotificador = FakeNotificador()

        // Inicializar ViewModel
        val viewModel = ListaViewModel(fakeRepository, fakeNotificador)

        // Llamar a alternarFavorito con un ID específico
        val idPrueba = 99
        viewModel.alternarFavorito(idPrueba)

        // Verificar que el repositorio registró el ID recibido
        assertTrue(fakeRepository.idsAlternados.contains(idPrueba))
        assertEquals(listOf(idPrueba), fakeRepository.idsAlternados)
    }
}
