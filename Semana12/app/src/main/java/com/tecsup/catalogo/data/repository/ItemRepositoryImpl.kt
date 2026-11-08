package com.tecsup.catalogo.data.repository

import android.database.sqlite.SQLiteException
import com.tecsup.catalogo.data.local.ItemDao
import com.tecsup.catalogo.data.mapper.aEntity
import com.tecsup.catalogo.data.mapper.aModelo
import com.tecsup.catalogo.data.mapper.aModeloDetalle
import com.tecsup.catalogo.data.remote.ApiService
import com.tecsup.catalogo.domain.model.Item
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import retrofit2.HttpException

class ItemRepositoryImpl(
    private val api: ApiService,
    private val dao: ItemDao
) : ItemRepository {

    // La UI siempre lee de Room: Retrofit solo la actualiza.
    override val items: Flow<List<Item>> =
        dao.todos().map { lista -> lista.map { it.aModelo() } }

    override val favoritos: Flow<List<Item>> =
        dao.favoritos().map { lista -> lista.map { it.aModelo() } }

    override suspend fun refrescar(): Result<Int> = capturar {
        // Si se guarda sin cuidado, el Upsert sobrescribe esFavorito y se pierden los favoritos.
        // Por eso se leen los ids favoritos ANTES y se vuelven a aplicar a los datos nuevos.
        val idsFavoritos = dao.idsFavoritos().toSet()
        val nuevos = api.listar().products
            .map { it.aEntity(esFavorito = it.id in idsFavoritos) }
        dao.guardarTodos(nuevos)
        nuevos.size
    }

    override suspend fun obtenerDetalle(id: Int): Result<Item> = capturar {
        val esFavorito = id in dao.idsFavoritos()
        api.detalle(id).aModeloDetalle(esFavorito)
    }

    override suspend fun alternarFavorito(id: Int) = dao.alternarFavorito(id)

    override suspend fun cantidadGuardada(): Int = dao.contar()

    override suspend fun cantidadFavoritos(): Int = dao.contarFavoritos()

    override suspend fun limpiarCache() = dao.borrarTodo()

    /** Convierte los errores de red, HTTP, JSON y base de datos en Result.failure (la app no se cae). */
    private inline fun <T> capturar(bloque: () -> T): Result<T> = try {
        Result.success(bloque())
    } catch (e: IOException) {            // sin internet o tiempo agotado
        Result.failure(e)
    } catch (e: HttpException) {          // 404, 500...
        Result.failure(e)
    } catch (e: SerializationException) { // el JSON no coincide con el DTO
        Result.failure(e)
    } catch (e: SQLiteException) {        // error al leer o escribir en Room
        Result.failure(e)
    }
}