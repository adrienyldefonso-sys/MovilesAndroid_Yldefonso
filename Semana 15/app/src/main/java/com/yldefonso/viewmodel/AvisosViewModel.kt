// Commit 7: se implementa la escucha del modo avión. El recordatorio (commit 6) ya estaba.
// Regla de oro: en este archivo NO va Context, NotificationManager ni BroadcastReceiver.
package com.yldefonso.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yldefonso.data.AvisosRepository
import com.yldefonso.model.Aviso
import com.yldefonso.model.Origen
import com.yldefonso.model.PermisoUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AvisosViewModel(private val repo: AvisosRepository) : ViewModel() {

    val avisos: StateFlow<List<Aviso>> = repo.avisos

    private val _eventos = MutableSharedFlow<Aviso>()
    val eventos: SharedFlow<Aviso> = _eventos.asSharedFlow()

    private val _permiso = MutableStateFlow<PermisoUi>(PermisoUi.SinPedir)
    val permiso: StateFlow<PermisoUi> = _permiso.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun permisoRespondido(concedido: Boolean) {
        _permiso.value = if (concedido) PermisoUi.Concedido else PermisoUi.Denegado
    }

    fun avisarAhora() {
        viewModelScope.launch {
            val aviso = repo.registrar("Tarea lista", "Tu entrega fue registrada", Origen.BOTON)
            _eventos.emit(aviso)
        }
    }

    private var trabajo: Job? = null

    private val _faltan = MutableStateFlow<Int?>(null)
    val faltan: StateFlow<Int?> = _faltan.asStateFlow()

    fun recordarEn(segundos: Int = 10) {
        if (trabajo?.isActive == true) return

        trabajo = viewModelScope.launch {
            try {
                for (s in segundos downTo 1) {
                    _faltan.value = s
                    delay(1000)
                }
                val aviso = repo.registrar("Recordatorio", "Revisa tu laboratorio pendiente", Origen.RECORDATORIO)
                _eventos.emit(aviso)
            } finally {
                _faltan.value = null
            }
        }
    }

    fun cancelarRecordatorio() {
        trabajo?.cancel()
    }

    // Paso 6 (commit 7): estado del switch. Arranca en false.
    // Se guarda en el ViewModel para que sobreviva a la rotación de pantalla.
    private val _escuchandoAvion = MutableStateFlow(false)
    val escuchandoAvion: StateFlow<Boolean> = _escuchandoAvion.asStateFlow()

    // Lo llama el Switch de la pantalla.
    fun cambiarEscucha(valor: Boolean) {
        _escuchandoAvion.value = valor
    }

    // Lo llama la pantalla cuando el receptor informa un cambio de modo avión.
    // El ViewModel decide qué avisar y lo registra; la pantalla muestra la notificación.
    fun alCambiarModoAvion(activo: Boolean) {
        viewModelScope.launch {
            val texto = if (activo) "Activado" else "Desactivado"
            val aviso = repo.registrar("Modo avión", texto, Origen.MODO_AVION)
            _eventos.emit(aviso)
        }
    }

    // TODO Paso 8: fun noSePudoMostrar()
}