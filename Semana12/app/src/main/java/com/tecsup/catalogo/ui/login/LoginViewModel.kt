package com.tecsup.catalogo.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.tecsup.catalogo.data.auth.AuthRepository
import com.tecsup.catalogo.domain.model.Sesion
import com.tecsup.catalogo.ui.UiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estado del login:
 *  - Cargando        : revisando la sesión guardada o esperando la respuesta de Firebase.
 *  - Exito(null)     : no hay sesión, se muestra el formulario.
 *  - Exito(sesion)   : hay sesión, la pantalla navega al catálogo.
 *  - Error(mensaje)  : validación fallida o credenciales incorrectas.
 */
class LoginViewModel(
    private val auth: AuthRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Sesion?>>(UiState.Cargando)
    val estado: StateFlow<UiState<Sesion?>> = _estado.asStateFlow()

    private var sesionRevisada = false

    /** R11: al abrir la app se revisa si ya hay una sesión guardada (se ejecuta una sola vez). */
    fun restaurarSesion() {
        if (sesionRevisada) return
        sesionRevisada = true
        viewModelScope.launch {
            val sesion = try {
                auth.sesionActual()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                null // si no se pudo leer el rol, se pide el login de nuevo
            }
            _estado.value = UiState.Exito(sesion)
        }
    }

    fun iniciarSesion(correo: String, clave: String) {
        if (_estado.value is UiState.Cargando) return // evita pulsar dos veces

        val correoLimpio = correo.trim()
        val errorValidacion = validar(correoLimpio, clave)
        if (errorValidacion != null) {
            // PA-04: si hay campos vacíos o inválidos NO se llama a Firebase.
            _estado.value = UiState.Error(errorValidacion)
            return
        }

        _estado.value = UiState.Cargando
        viewModelScope.launch {
            auth.iniciarSesion(correoLimpio, clave)
                .onSuccess { _estado.value = UiState.Exito(it) }
                .onFailure { _estado.value = UiState.Error(mensajeDe(it)) }
        }
    }

    /** R11: cierra la sesión de Firebase. */
    fun cerrarSesion() = auth.cerrarSesion()

    private fun validar(correo: String, clave: String): String? = when {
        correo.isEmpty() -> "Ingresa tu correo"
        !CORREO_VALIDO.matches(correo) -> "El correo no tiene un formato válido"
        clave.isEmpty() -> "Ingresa tu contraseña"
        else -> null
    }

    private fun mensajeDe(e: Throwable) = when (e) {
        // Mensaje único para correo o contraseña mal escritos: no revela si el correo existe.
        is FirebaseAuthInvalidCredentialsException,
        is FirebaseAuthInvalidUserException -> "Correo o contraseña incorrectos"
        is FirebaseTooManyRequestsException -> "Demasiados intentos. Inténtalo más tarde"
        is FirebaseNetworkException -> "No hay conexión a internet"
        is FirebaseFirestoreException -> "No se pudo leer tu perfil. Revisa tu conexión e inténtalo de nuevo"
        else -> "No se pudo iniciar sesión. Inténtalo de nuevo"
    }

    private companion object {
        val CORREO_VALIDO = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    }
}