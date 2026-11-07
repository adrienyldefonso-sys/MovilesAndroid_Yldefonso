package com.tecsup.catalogo.domain.model

/**
 * Modelo que usa la UI (Item). Es distinto de ItemDto (API) e ItemEntity (Room).
 * Tema Productos (DummyJSON, categoría smartphones): id entero, así los contratos no cambian.
 * Los campos con valor por defecto null solo llegan con el endpoint de detalle.
 */
data class Item(
    val id: Int,
    val titulo: String,
    val subtitulo: String,          // marca del producto
    val imagenUrl: String?,
    val precio: Double,
    val esFavorito: Boolean = false,
    val descripcion: String? = null, // solo en el detalle
    val stock: Int? = null,          // solo en el detalle
    val rating: Double? = null       // solo en el detalle
)
