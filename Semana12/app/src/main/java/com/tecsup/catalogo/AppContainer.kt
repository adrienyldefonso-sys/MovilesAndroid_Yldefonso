package com.tecsup.catalogo

import android.content.Context

/**
 * Contenedor de dependencias (inyección manual). Aquí se crean:
 *  - H6  : ApiService (RetrofitClient) e ItemRepository
 *  - H8  : AppDatabase y ItemDao
 *  - H13 : AuthRepository (FirebaseAuthRepository)
 *  - H15 : Notificador (NotificadorAndroid)
 *
 * El ViewModelProvider.Factory (que recibe este AppContainer) también va en este archivo,
 * para no agregar archivos fuera de la estructura del laboratorio.
 */
class AppContainer(private val context: Context)
