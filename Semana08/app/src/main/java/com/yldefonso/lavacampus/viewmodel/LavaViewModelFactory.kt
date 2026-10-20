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
        // TODO: agrega aquí los ViewModels que no necesitan parámetros (Pasos 12 y 13)
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }

    /** Para ViewModels del estudiante: reciben el repositorio y el id del usuario actual. */
    fun <VM : ViewModel> paraUsuario(
        usuarioId: Int,
        crear: (LavaRepository, Int) -> VM
    ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = crear(repo, usuarioId) as T
    }
}