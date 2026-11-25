package com.yldefonso.murocampus.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yldefonso.murocampus.model.Post

// Tabla "posts" de Room: una fila por publicación guardada.
@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: Int, // el id de JSONPlaceholder es único, sirve como clave primaria
    val userId: Int,
    val title: String,
    val body: String
)

// Conversión entre el modelo de red (Post) y la tabla (PostEntity).
fun Post.toEntity(): PostEntity = PostEntity(id, userId, title, body)
fun PostEntity.toPost(): Post = Post(userId, id, title, body)