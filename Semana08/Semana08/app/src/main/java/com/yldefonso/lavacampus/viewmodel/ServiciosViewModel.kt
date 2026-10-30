package com.yldefonso.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yldefonso.lavacampus.data.local.entity.Servicio
import com.yldefonso.lavacampus.data.repository.LavaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ServiciosViewModel(private val repo: LavaRepository) : ViewModel() {

    val servicios: StateFlow<List<Servicio>> = repo.observarServicios()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activos: StateFlow<List<Servicio>> = repo.observarServiciosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun limpiarMensaje() { _mensaje.value = null }

    fun alternar(s: Servicio) {
        viewModelScope.launch { repo.cambiarActivo(s.id, !s.activo) }
    }

    /** Nuevo (id = 0) o editado. Valida nombre y precio; al terminar avisa con [onGuardado]. */
    fun guardar(servicio: Servicio, onGuardado: () -> Unit) {
        when {
            servicio.nombre.isBlank() -> { _mensaje.value = "El nombre es obligatorio"; return }
            servicio.precio <= 0.0 -> { _mensaje.value = "El precio debe ser mayor que 0"; return }
        }
        viewModelScope.launch {
            repo.guardarServicio(servicio)
            _mensaje.value = null
            onGuardado()
        }
    }

    /** Un servicio que ya aparece en algún detalle no se elimina: solo se desactiva. */
    fun eliminar(servicio: Servicio) {
        viewModelScope.launch {
            if (repo.servicioTieneDetalles(servicio.id)) {
                _mensaje.value = "«${servicio.nombre}» ya aparece en pedidos: no se elimina, solo se desactiva"
            } else {
                repo.eliminarServicio(servicio)
                _mensaje.value = "Servicio eliminado"
            }
        }
    }
}