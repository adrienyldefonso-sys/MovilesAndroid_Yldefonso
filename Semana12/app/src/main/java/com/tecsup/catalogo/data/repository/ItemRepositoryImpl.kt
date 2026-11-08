package com.tecsup.catalogo.data.repository

import com.tecsup.catalogo.data.mapper.aModelo
import com.tecsup.catalogo.data.mapper.aModeloDetalle
import com.tecsup.catalogo.data.remote.ApiService
import com.tecsup.catalogo.domain.model.Item
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.serialization.SerializationException
import retrofit2.HttpException

class ItemRepositoryImpl(
    private val api: ApiService
) : ItemRepository {

    // H6: almacenamiento TEMPORAL en memoria. En H9 se reemplaza por Room (ItemDao).
    private val guardados = MutableStateFlow<List<Item>>(emptyList())

    override val items: Flow<List<Item>> = guardados
    override val favoritos: Flow<List<Item>> = guardados.map { lista -> lista.filter { it.esFavorito } }

    override suspend fun refrescar(): Result<Int> = capturar {
        // Se conservan los favoritos antes de reemplazar la lista.
        val idsFavoritos = guardados.value.filter { it.esFavorito }.map { it.id }.toSet()
        val nuevos = api.listar().products
            .map { it.aModelo(esFavorito = it.id in idsFavoritos) }
            .sortedBy { it.titulo }
        guardados.value = nuevos
        nuevos.size
    }

    override suspend fun obtenerDetalle(id: Int): Result<Item> = capturar {
        val esFavorito = guardados.value.any { it.id == id && it.esFavorito }
        api.detalle(id).aModeloDetalle(esFavorito)
    }

    override suspend fun alternarFavorito(id: Int) {
        guardados.update { lista ->
            lista.map { if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it }
        }
    }

    override suspend fun cantidadGuardada(): Int = guardados.value.size

    override suspend fun cantidadFavoritos(): Int = guardados.value.count { it.esFavorito }

    override suspend fun limpiarCache() {
        guardados.value = emptyList()
    }

    /** Convierte los errores de red, HTTP y de lectura del JSON en Result.failure (la app no se cae). */
    private inline fun <T> capturar(bloque: () -> T): Result<T> = try {
        Result.success(bloque())
    } catch (e: IOException) {           // sin internet o tiempo agotado
        Result.failure(e)
    } catch (e: HttpException) {         // 404, 500...
        Result.failure(e)
    } catch (e: SerializationException) { // el JSON no coincide con el DTO
        Result.failure(e)
    }
}