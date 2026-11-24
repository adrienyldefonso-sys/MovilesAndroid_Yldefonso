package com.yldefonso.murocampus.data.remote

import com.yldefonso.murocampus.model.Comentario
import com.yldefonso.murocampus.model.NuevoPost
import com.yldefonso.murocampus.model.Post
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// Interface de Retrofit: describe las llamadas al servicio. Solo firmas, sin lógica.
// Todas son suspend para ejecutarse dentro de una corrutina.
interface PostApi {

    // GET posts: lista de 100 publicaciones.
    // La ruta NO empieza con "/": la barra reemplazaría el camino de la URL base.
    @GET("posts")
    suspend fun listar(): List<Post>

    // GET posts/{id}: @Path reemplaza {id} en la ruta.
    @GET("posts/{id}")
    suspend fun detalle(@Path("id") id: Int): Post

    // GET posts/{id}/comments: 5 comentarios por publicación.
    @GET("posts/{id}/comments")
    suspend fun comentarios(@Path("id") id: Int): List<Comentario>

    // GET posts?userId=2: @Query agrega ?userId=2 al final de la URL.
    @GET("posts")
    suspend fun porUsuario(@Query("userId") userId: Int): List<Post>

    // POST posts: @Body convierte NuevoPost en JSON dentro del cuerpo de la petición.
    @POST("posts")
    suspend fun crear(@Body nuevo: NuevoPost): Post
}