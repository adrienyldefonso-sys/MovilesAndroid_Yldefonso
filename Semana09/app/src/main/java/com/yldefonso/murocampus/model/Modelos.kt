package com.yldefonso.murocampus.model

import kotlinx.serialization.Serializable

// [E] Ejemplo resuelto (ya conectado en el proyecto base)
@Serializable
data class Usuario(
    val id: Int,
    val name: String,
    val email: String
)

// Publicación de JSONPlaceholder (GET posts y GET posts/{id}).
// @Serializable permite que kotlinx.serialization convierta el JSON en este objeto.
// Los nombres deben ser IGUALES a las claves del JSON; si no, la app falla con SerializationException.
@Serializable
data class Post(
    val userId: Int,   // autor de la publicación (1 a 10)
    val id: Int,       // identificador (1 a 100)
    val title: String, // título de la publicación
    val body: String   // texto de la publicación
)

// Comentario de una publicación (GET posts/{id}/comments).
@Serializable
data class Comentario(
    val postId: Int,   // publicación a la que pertenece
    val id: Int,       // identificador del comentario
    val name: String,  // nombre de quien comenta
    val email: String, // correo (en la pantalla de detalle se muestra en azul)
    val body: String   // texto del comentario
)

// Lo que ENVIAMOS con POST: no lleva id, porque el servidor lo asigna.
@Serializable
data class NuevoPost(
    val userId: Int,
    val title: String,
    val body: String
)