package com.tecsup.catalogo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Tabla "items" de Room. Es distinta de ItemDto (API) y de Item (UI). */
@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val id: Int,
    val titulo: String,
    val subtitulo: String,
    val imagenUrl: String?,
    val precio: Double,
    val esFavorito: Boolean = false
)