package com.yldefonso.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yldefonso.campusgps.data.LugaresRepository
import com.yldefonso.campusgps.data.UbicacionRepository

/**
 * Android no sabe crear un ViewModel con parámetros. Esta fábrica le entrega
 * los repositorios (inyección de dependencias manual).
 */
class CampusViewModelFactory(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapaViewModel::class.java)) {
            return MapaViewModel(ubicacionRepo, lugaresRepo) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}
