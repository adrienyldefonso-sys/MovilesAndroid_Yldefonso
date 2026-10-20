package com.yldefonso.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yldefonso.lavacampus.data.prefs.PreferenciasStore
import com.yldefonso.lavacampus.data.repository.LavaRepository

class LavaViewModelFactory(
    private val repo: LavaRepository,
    private val prefs: PreferenciasStore
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(SesionViewModel::class.java) -> SesionViewModel(repo, prefs) as T
        modelClass.isAssignableFrom(ServiciosViewModel::class.java) -> ServiciosViewModel(repo) as T
        // TODO: agrega aquí cada ViewModel nuevo (Pasos 8 al 13)
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}