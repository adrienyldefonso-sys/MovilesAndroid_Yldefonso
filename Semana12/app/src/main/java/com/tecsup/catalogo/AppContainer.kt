package com.tecsup.catalogo

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.tecsup.catalogo.data.auth.AuthRepository
import com.tecsup.catalogo.data.auth.FirebaseAuthRepository
import com.tecsup.catalogo.data.local.AppDatabase
import com.tecsup.catalogo.data.remote.RetrofitClient
import com.tecsup.catalogo.data.repository.ItemRepository
import com.tecsup.catalogo.data.repository.ItemRepositoryImpl
import com.tecsup.catalogo.notification.Notificador
import com.tecsup.catalogo.ui.detalle.DetalleViewModel
import com.tecsup.catalogo.ui.favoritos.FavoritosViewModel
import com.tecsup.catalogo.ui.login.LoginViewModel
import com.tecsup.catalogo.ui.lista.ListaViewModel

/**
 * Contenedor de dependencias (inyección manual).
 *  - H15 : Notificador real (NotificadorAndroid)
 */
class AppContainer(private val context: Context) {

    private val database: AppDatabase by lazy { AppDatabase.crear(context) }

    val itemRepository: ItemRepository by lazy {
        ItemRepositoryImpl(RetrofitClient.api, database.itemDao())
    }

    val authRepository: AuthRepository by lazy { FirebaseAuthRepository() }

    // Notificador temporal que no hace nada. En H15 se reemplaza por NotificadorAndroid(context).
    val notificador: Notificador = object : Notificador {
        override fun mostrar(titulo: String, texto: String) { }
    }
}

/** Crea los ViewModel con las dependencias del AppContainer. Cada hito agrega su rama. */
class ViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T = when {
        modelClass.isAssignableFrom(ListaViewModel::class.java) ->
            ListaViewModel(container.itemRepository, container.notificador) as T
        modelClass.isAssignableFrom(DetalleViewModel::class.java) ->
            DetalleViewModel(container.itemRepository, extras.createSavedStateHandle()) as T
        modelClass.isAssignableFrom(FavoritosViewModel::class.java) ->
            FavoritosViewModel(container.itemRepository) as T
        modelClass.isAssignableFrom(LoginViewModel::class.java) ->
            LoginViewModel(container.authRepository) as T
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}