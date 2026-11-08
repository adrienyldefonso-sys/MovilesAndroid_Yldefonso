package com.tecsup.catalogo.data.remote

import com.tecsup.catalogo.data.remote.dto.ItemDto
import com.tecsup.catalogo.data.remote.dto.ListaResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    // Endpoint 1 (lista). La variante del grupo G01 (smartphones) vive aquí.
    @GET("products/category/smartphones")
    suspend fun listar(): ListaResponseDto

    // Endpoint 2 (detalle)
    @GET("products/{id}")
    suspend fun detalle(@Path("id") id: Int): ItemDto
}