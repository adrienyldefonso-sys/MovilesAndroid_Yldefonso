package com.yldefonso.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yldefonso.lavacampus.data.local.entity.Usuario
import com.yldefonso.lavacampus.data.prefs.PreferenciasStore
import com.yldefonso.lavacampus.data.repository.LavaRepository
import com.yldefonso.lavacampus.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repo: LavaRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val notificaciones: StateFlow<Boolean> = prefs.notificaciones
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val recibirPedidos: StateFlow<Boolean> = prefs.recibirPedidos
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    fun cambiarModoOscuro(valor: Boolean) { viewModelScope.launch { prefs.guardarModoOscuro(valor) } }
    fun cambiarNotificaciones(valor: Boolean) { viewModelScope.launch { prefs.guardarNotificaciones(valor) } }
    fun cambiarRecibirPedidos(valor: Boolean) { viewModelScope.launch { prefs.guardarRecibirPedidos(valor) } }

    /** Guarda el otro perfil y avisa para que la navegación limpie la pila. */
    fun cambiarPerfil(onListo: (Rol) -> Unit) {
        val actual = _usuarioActual.value ?: return
        val otroRol = if (actual.rol == Rol.ADMIN.name) Rol.CLIENTE else Rol.ADMIN
        viewModelScope.launch {
            val usuario = repo.obtenerUsuarioPorRol(otroRol.name) ?: return@launch
            prefs.guardarSesion(otroRol, usuario.id)
            _usuarioActual.value = usuario
            onListo(otroRol)
        }
    }

    fun limpiarError() { _error.value = null }

    /** Regla de login: campos, credenciales y coincidencia con la pestaña elegida. */
    fun login(correo: String, clave: String, rolSeleccionado: Rol) {
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Completa tu correo y contraseña"
            return
        }
        viewModelScope.launch {
            val usuario = repo.login(correo.trim().lowercase(), clave)
            when {
                usuario == null ->
                    _error.value = "Correo o contraseña incorrectos"
                usuario.rol != rolSeleccionado.name ->
                    _error.value = if (rolSeleccionado == Rol.ADMIN)
                        "Esta cuenta es de estudiante. Elige la pestaña Estudiante"
                    else
                        "Esta cuenta es de administrador. Elige la pestaña Administrador"
                else -> {
                    prefs.guardarSesion(rolSeleccionado, usuario.id)
                    _error.value = null
                    _usuarioActual.value = usuario
                }
            }
        }
    }

    /** Splash: si hay perfil guardado, restaura al usuario y devuelve su rol; si no, null. */
    suspend fun restaurarSesion(): Rol? {
        val perfil = prefs.perfilActivo.first()
        val id = prefs.usuarioId.first()
        if (perfil.isBlank() || id < 0) return null
        val usuario = repo.obtenerUsuario(id) ?: return null
        if (usuario.rol != perfil) return null
        _usuarioActual.value = usuario
        return Rol.valueOf(usuario.rol)
    }

    fun cerrarSesion() {
        // Primero el estado (síncrono): así el login no ve un usuario viejo y no rebota al Inicio
        _usuarioActual.value = null
        _error.value = null
        viewModelScope.launch { prefs.cerrarSesion() }
    }
}