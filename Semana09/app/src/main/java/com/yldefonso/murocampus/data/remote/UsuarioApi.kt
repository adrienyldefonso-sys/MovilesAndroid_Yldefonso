package com.yldefonso.murocampus.data.remote

import com.yldefonso.murocampus.model.Usuario
import retrofit2.http.GET

// [E] Ejemplo resuelto: estúdialo antes de hacer lo mismo con Post
interface UsuarioApi {
    @GET("users")
    suspend fun listar(): List<Usuario>
}
