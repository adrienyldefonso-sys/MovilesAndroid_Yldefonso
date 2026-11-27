// [E] ENTREGADO: no modificar.
package com.yldefonso.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yldefonso.data.AvisosRepository

// Permite pasar el Repository al ViewModel al crearlo.
class AvisosViewModelFactory(private val repo: AvisosRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AvisosViewModel(repo) as T
    }
}
