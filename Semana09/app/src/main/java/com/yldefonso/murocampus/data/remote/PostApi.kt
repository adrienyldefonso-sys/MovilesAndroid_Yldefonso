package com.yldefonso.murocampus.data.remote

import com.yldefonso.murocampus.model.Comentario
import com.yldefonso.murocampus.model.EditarPost
import com.yldefonso.murocampus.model.NuevoPost
import com.yldefonso.murocampus.model.Post
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PostApi {

    @GET("posts")
    suspend fun listar(): List<Post>

    @GET("posts/{id}")
    suspend fun detalle(@Path("id") id: Int): Post

    @GET("posts/{id}/comments")
    suspend fun comentarios(@Path("id") id: Int): List<Comentario>

    @GET("posts")
    suspend fun porUsuario(@Query("userId") userId: Int): List<Post>

    @POST("posts")
    suspend fun crear(@Body nuevo: NuevoPost): Post

    // PATCH posts/{id}: cambia solo los campos enviados.
    // Es el verbo correcto cuando se modifica una parte del recurso. PUT reemplazaría todo.
    @PATCH("posts/{id}")
    suspend fun editar(@Path("id") id: Int, @Body cambios: EditarPost): Post
}