package com.yldefonso.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yldefonso.lavacampus.data.repository.LavaRepository

/** Crea los ViewModels pasándoles el Repository. Se agrega una rama por cada ViewModel nuevo. */
class LavaViewModelFactory(private val repo: LavaRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(ServiciosViewModel::class.java) -> ServiciosViewModel(repo) as T
        // TODO: agrega aquí cada ViewModel nuevo (Pasos 6 al 13)
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}
