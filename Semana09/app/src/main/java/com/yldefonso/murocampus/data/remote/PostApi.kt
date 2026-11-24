package com.yldefonso.murocampus.data.remote

// [A] TÚ LO HACES: interface de Retrofit con suspend fun y sin "/" al inicio de las rutas.
// listar()              -> @GET("posts")                 List<Post>
// detalle(id)           -> @GET("posts/{id}") @Path      Post
// comentarios(id)       -> @GET("posts/{id}/comments")   List<Comentario>
// porUsuario(userId)    -> @GET("posts") @Query          List<Post>
// crear(nuevo)          -> @POST("posts") @Body          Post
