package com.yldefonso.murocampus.data

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

class PostRepositoryRemote(private val api: PostApi) : PostRepository {

    private suspend fun <T> llamar(bloque: suspend () -> T): Result<T> =
        try {
            Result.success(bloque())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun listar(): Result<List<Post>> = llamar { api.listar() }

    // Pasa inicio y limite tal cual a la API: el Repository no decide cuántas páginas hay.
    override suspend fun listarPagina(inicio: Int, limite: Int): Result<List<Post>> =
        llamar { api.listarPagina(inicio, limite) }

    override suspend fun detalle(id: Int): Result<Post> = llamar { api.detalle(id) }
    override suspend fun comentarios(id: Int): Result<List<Comentario>> = llamar { api.comentarios(id) }
    override suspend fun porUsuario(userId: Int): Result<List<Post>> = llamar { api.porUsuario(userId) }
    override suspend fun crear(nuevo: NuevoPost): Result<Post> = llamar { api.crear(nuevo) }
    override suspend fun editar(id: Int, nuevoTitulo: String): Result<Post> =
        llamar { api.editar(id, EditarPost(nuevoTitulo)) }
}