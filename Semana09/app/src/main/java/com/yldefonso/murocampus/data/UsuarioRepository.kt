package com.yldefonso.murocampus.data

import com.yldefonso.murocampus.data.remote.UsuarioApi
import com.yldefonso.murocampus.model.Usuario
import kotlinx.coroutines.CancellationException

// [E] Ejemplo resuelto: el Repository nunca deja escapar una excepción
interface UsuarioRepository {
    suspend fun listar(): Result<List<Usuario>>
}

class UsuarioRepositoryRemote(private val api: UsuarioApi) : UsuarioRepository {
    override suspend fun listar(): Result<List<Usuario>> =
        try {
            Result.success(api.listar())
        } catch (e: CancellationException) {
            throw e // nunca se traga la cancelación
        } catch (e: Exception) {
            Result.failure(e)
        }
}
