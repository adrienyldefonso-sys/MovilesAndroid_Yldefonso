package com.tecsup.catalogo.data.mapper

import com.tecsup.catalogo.data.remote.dto.ItemDto
import com.tecsup.catalogo.domain.model.Item

/** Lista: solo los datos básicos. La descripción, el stock y el rating llegan con el detalle. */
fun ItemDto.aModelo(esFavorito: Boolean = false): Item = Item(
    id = id,
    titulo = title,
    subtitulo = brand ?: "Sin marca",
    imagenUrl = thumbnail,
    precio = price,
    esFavorito = esFavorito
)

/** Detalle: incluye los campos extra del segundo endpoint. */
fun ItemDto.aModeloDetalle(esFavorito: Boolean = false): Item =
    aModelo(esFavorito).copy(descripcion = description, stock = stock, rating = rating)