package com.yldefonso.murocampus.model

import kotlinx.serialization.Serializable

// [E] Ejemplo resuelto (ya conectado en el proyecto base)
@Serializable
data class Usuario(
    val id: Int,
    val name: String,
    val email: String
)

// [G] Completa las clases respetando los nombres del JSON (sección 5 de la guía)
@Serializable
data class Post(
    // TODO: userId, id, title, body
)

@Serializable
data class Comentario(
    // TODO: postId, id, name, email, body
)

@Serializable
data class NuevoPost(
    // TODO: userId, title, body (sin id)
)
