// Commit 4: se implementa avisarAhora. Lo demás sigue como TODO.
// Regla de oro: en este archivo NO va Context, NotificationManager ni BroadcastReceiver.
package com.yldefonso.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yldefonso.data.AvisosRepository
import com.yldefonso.model.Aviso
import com.yldefonso.model.Origen
import com.yldefonso.model.PermisoUi
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

    // Paso 3 (commit 4): registra el aviso en el Repository y emite el evento.
    // La pantalla recibe el evento y llama al Notificador (el ViewModel no toca el sistema).
    fun avisarAhora() {
        viewModelScope.launch {
            // Se guarda en el historial primero, aunque después no se pueda mostrar la notificación.
            val aviso = repo.registrar("Tarea lista", "Tu entrega fue registrada", Origen.BOTON)
            _eventos.emit(aviso)
        }
    }

    // TODO Paso 5: faltan, recordarEn(segundos = 10), cancelarRecordatorio()
    // TODO Paso 6: escuchandoAvion, cambiarEscucha(valor), alCambiarModoAvion(activo)
    // TODO Paso 8: fun noSePudoMostrar()
}