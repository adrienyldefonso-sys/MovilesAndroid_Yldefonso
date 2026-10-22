package com.yldefonso.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yldefonso.lavacampus.data.prefs.PreferenciasStore
import com.yldefonso.lavacampus.data.repository.LavaRepository

class LavaViewModelFactory(
    val repo: LavaRepository,
    private val prefs: PreferenciasStore
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(SesionViewModel::class.java) -> SesionViewModel(repo, prefs) as T
        modelClass.isAssignableFrom(ServiciosViewModel::class.java) -> ServiciosViewModel(repo) as T
        modelClass.isAssignableFrom(PanelViewModel::class.java) -> PanelViewModel(repo) as T
        modelClass.isAssignableFrom(BandejaViewModel::class.java) -> BandejaViewModel(repo) as T
        modelClass.isAssignableFrom(ClientesViewModel::class.java) -> ClientesViewModel(repo) as T
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }

    /** Para ViewModels que reciben el repositorio y un id (del usuario o del pedido). */
    fun <VM : ViewModel> paraUsuario(
        id: Int,
        crear: (LavaRepository, Int) -> VM
    ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = crear(repo, id) as T
    }

    /** Nuevo pedido necesita además la preferencia "Recibir pedidos". */
    fun paraNuevoPedido(usuarioId: Int, pedidoARepetir: Int? = null): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            NuevoPedidoViewModel(repo, usuarioId, prefs.recibirPedidos, pedidoARepetir) as T
    }
}