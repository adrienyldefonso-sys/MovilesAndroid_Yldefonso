package com.yldefonso.murocampus.data.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

// Cliente único de Retrofit: se crea una sola vez y se reutiliza en toda la app.
object RetrofitClient {

    // Convierte JSON en data class. ignoreUnknownKeys = true: si el servidor agrega campos, no falla.
    private val json = Json { ignoreUnknownKeys = true }

    // OkHttp hace la conexión real. El interceptor muestra las peticiones en Logcat (solo desarrollo).
    private val cliente = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // BODY muestra también el JSON
        })
        .build()

    // by lazy: Retrofit se crea la primera vez que se usa api, no antes.
    val api: PostApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/") // DEBE terminar en "/"
            .client(cliente)
            // Conversor: JSON <-> data class, con el tipo de contenido application/json
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(PostApi::class.java) // Retrofit implementa la interface PostApi
    }
}