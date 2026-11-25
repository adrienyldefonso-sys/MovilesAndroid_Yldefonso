package com.yldefonso.murocampus.data

import com.yldefonso.murocampus.data.local.PostDao
import com.yldefonso.murocampus.data.local.toEntity
import com.yldefonso.murocampus.data.local.toPost
import com.yldefonso.murocampus.data.remote.PostApi
import com.yldefonso.murocampus.model.Comentario
import com.yldefonso.murocampus.model.EditarPost
import com.yldefonso.murocampus.model.NuevoPost
import com.yldefonso.murocampus.model.Post
import kotlinx.coroutines.CancellationException

interface PostRepository {
    suspend fun listar(): Result<List<Post>>
    suspend fun listarPagina(inicio: Int, limite: Int): Result<List<Post>>
    suspend fun detalle(id: Int): Result<Post>
    suspend fun comentarios(id: Int): Result<List<Comentario>>
    suspend fun porUsuario(userId: Int): Result<List<Post>>
    suspend fun crear(nuevo: NuevoPost): Result<Post>
    suspend fun editar(id: Int, nuevoTitulo: String): Result<Post>
}

// Repository con caché: la red es la fuente principal y Room guarda una copia para usarla sin internet.
class PostRepositoryRemote(
    private val api: PostApi,
    private val dao: PostDao
) : PostRepository {

    private suspend fun <T> llamar(bloque: suspend () -> T): Result<T> =
        try {
            Result.success(bloque())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun listar(): Result<List<Post>> = llamar { api.listar() }

    // Decide entre red y base local: intenta la red y, si falla, usa la copia guardada.
    override suspend fun listarPagina(inicio: Int, limite: Int): Result<List<Post>> {
        val red = llamar { api.listarPagina(inicio, limite) }
        return red.fold(
            onSuccess = { posts ->
                // Éxito: guardamos la página para poder mostrarla después sin internet.
                llamar { dao.guardar(posts.map { it.toEntity() }) }
                Result.success(posts)
            },
            onFailure = { error ->
                // Fallo: si esa página está en Room, la mostramos. Si no, devolvemos el error.
                val copia = llamar { dao.paginaDesde(inicio, limite).map { it.toPost() } }
                    .getOrDefault(emptyList())
                if (copia.isNotEmpty()) Result.success(copia) else Result.failure(error)
            }
        )
    }

    override suspend fun detalle(id: Int): Result<Post> = llamar { api.detalle(id) }
    override suspend fun comentarios(id: Int): Result<List<Comentario>> = llamar { api.comentarios(id) }
    override suspend fun porUsuario(userId: Int): Result<List<Post>> = llamar { api.porUsuario(userId) }
    override suspend fun crear(nuevo: NuevoPost): Result<Post> = llamar { api.crear(nuevo) }
    override suspend fun editar(id: Int, nuevoTitulo: String): Result<Post> =
        llamar { api.editar(id, EditarPost(nuevoTitulo)) }
}