package com.yldefonso.murocampus.data.remote

import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit

// [G] Completa los TODO. Crear el cliente una sola vez.
object RetrofitClient {
    private val json = Json { ignoreUnknownKeys = true }

    private val cliente = OkHttpClient.Builder()
        // TODO 1: addInterceptor(HttpLoggingInterceptor().apply { level = ... BODY })
        .build()

    val api: PostApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(cliente)
            // TODO 2: addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(PostApi::class.java)
    }
}
