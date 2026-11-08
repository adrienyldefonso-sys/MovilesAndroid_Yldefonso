package com.tecsup.catalogo.data.mapper

import com.tecsup.catalogo.data.local.ItemEntity
import com.tecsup.catalogo.data.remote.dto.ItemDto
import com.tecsup.catalogo.domain.model.Item

// Cadena de conversión: ItemDto (API) -> ItemEntity (Room) -> Item (UI)

fun ItemDto.aEntity(esFavorito: Boolean = false): ItemEntity = ItemEntity(
    id = id,
    titulo = title,
    subtitulo = brand ?: "Sin marca",
    imagenUrl = thumbnail,
    precio = price,
    esFavorito = esFavorito
)

fun ItemEntity.aModelo(): Item = Item(
    id = id,
    titulo = titulo,
    subtitulo = subtitulo,
    imagenUrl = imagenUrl,
    precio = precio,
    esFavorito = esFavorito
)

/** Detalle: viene directo de la API, con los campos extra del segundo endpoint. */
fun ItemDto.aModeloDetalle(esFavorito: Boolean = false): Item = Item(
    id = id,
    titulo = title,
    subtitulo = brand ?: "Sin marca",
    imagenUrl = thumbnail,
    precio = price,
    esFavorito = esFavorito,
    descripcion = description,
    stock = stock,
    rating = rating
)