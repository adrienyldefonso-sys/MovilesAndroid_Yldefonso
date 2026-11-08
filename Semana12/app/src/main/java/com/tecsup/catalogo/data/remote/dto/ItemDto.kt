package com.tecsup.catalogo.data.remote.dto

import kotlinx.serialization.Serializable

/** Producto tal como lo responde DummyJSON (lo mismo para la lista y el detalle). */
@Serializable
data class ItemDto(
    val id: Int,
    val title: String,
    val description: String? = null,
    val price: Double,
    val rating: Double? = null,
    val stock: Int? = null,
    val brand: String? = null,
    val thumbnail: String? = null
)

/** Envoltorio de la lista: { "products": [...], "total": 16, "skip": 0, "limit": 16 } */
@Serializable
data class ListaResponseDto(
    val products: List<ItemDto>,
    val total: Int = 0,
    val skip: Int = 0,
    val limit: Int = 0
)