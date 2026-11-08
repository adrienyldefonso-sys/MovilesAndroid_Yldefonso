package com.tecsup.catalogo

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.catalogo.data.remote.RetrofitClient
import com.tecsup.catalogo.data.repository.ItemRepository
import com.tecsup.catalogo.data.repository.ItemRepositoryImpl
import com.tecsup.catalogo.notification.Notificador
import com.tecsup.catalogo.ui.lista.ListaViewModel

/**
 * Contenedor de dependencias (inyección manual).
 *  - H8  : AppDatabase y ItemDao
 *  - H13 : AuthRepository
 *  - H15 : Notificador real (NotificadorAndroid)
 */
class AppContainer(private val context: Context) {

    val itemRepository: ItemRepository by lazy { ItemRepositoryImpl(RetrofitClient.api) }

    // H6: Notificador temporal que no hace nada. En H15 se reemplaza por NotificadorAndroid(context).
    val notificador: Notificador = object : Notificador {
        override fun mostrar(titulo: String, texto: String) { }
    }
}

/** Crea los ViewModel con las dependencias del AppContainer. Cada hito agrega su rama. */
class ViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(ListaViewModel::class.java) ->
            ListaViewModel(container.itemRepository, container.notificador) as T
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}