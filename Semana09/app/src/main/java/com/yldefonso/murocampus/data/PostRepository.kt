package com.yldefonso.murocampus.data

import com.yldefonso.murocampus.data.remote.PostApi
import com.yldefonso.murocampus.model.Comentario
import com.yldefonso.murocampus.model.NuevoPost
import com.yldefonso.murocampus.model.Post
import kotlinx.coroutines.CancellationException

// Contrato que usa el ViewModel. No conoce Retrofit ni excepciones de red.
interface PostRepository {
    suspend fun listar(): Result<List<Post>>
    suspend fun detalle(id: Int): Result<Post>
    suspend fun comentarios(id: Int): Result<List<Comentario>>
    suspend fun porUsuario(userId: Int): Result<List<Post>>
    suspend fun crear(nuevo: NuevoPost): Result<Post>
}

// Implementación real: llama a PostApi y convierte todo en Result.
class PostRepositoryRemote(private val api: PostApi) : PostRepository {

    // Función privada: evita repetir el try-catch en las cinco funciones.
    private suspend fun <T> llamar(bloque: suspend () -> T): Result<T> =
        try {
            Result.success(bloque())     // éxito: envuelve el dato
        } catch (e: CancellationException) {
            throw e                      // NUNCA atrapar la cancelación: rompe las corrutinas
        } catch (e: Exception) {
            Result.failure(e)            // error de red o servidor: se devuelve, no se lanza
        }

    override suspend fun listar(): Result<List<Post>> = llamar { api.listar() }
    override suspend fun detalle(id: Int): Result<Post> = llamar { api.detalle(id) }
    override suspend fun comentarios(id: Int): Result<List<Comentario>> = llamar { api.comentarios(id) }
    override suspend fun porUsuario(userId: Int): Result<List<Post>> = llamar { api.porUsuario(userId) }
    override suspend fun crear(nuevo: NuevoPost): Result<Post> = llamar { api.crear(nuevo) }
}