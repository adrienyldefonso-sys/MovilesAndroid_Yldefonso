// [G] GUIADO: esqueleto con TODO. Lo completas en los pasos 2 a 8.
// Regla de oro: en este archivo NO va Context, NotificationManager ni BroadcastReceiver.
package com.yldefonso.viewmodel

import androidx.lifecycle.ViewModel
import com.yldefonso.data.AvisosRepository
import com.yldefonso.model.Aviso
import com.yldefonso.model.PermisoUi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class AvisosViewModel(private val repo: AvisosRepository) : ViewModel() {

    // El ViewModel expone la lista del Repository; la View solo la observa.
    val avisos: StateFlow<List<Aviso>> = repo.avisos

    // Evento de una sola vez: "muestra esta notificación".
    // El ViewModel NO tiene Context: la pantalla lo recibe y llama al Notificador.
    private val _eventos = MutableSharedFlow<Aviso>()
    val eventos: SharedFlow<Aviso> = _eventos.asSharedFlow()

    // Estado del permiso (Sin pedir / Concedido / Denegado) para la etiqueta de color
    private val _permiso = MutableStateFlow<PermisoUi>(PermisoUi.SinPedir)
    val permiso: StateFlow<PermisoUi> = _permiso.asStateFlow()

    // Mensaje de error visible (Paso 8); null = no hay mensaje
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    // TODO Paso 2: fun permisoRespondido(concedido: Boolean)
    // TODO Paso 3: fun avisarAhora()  -> repo.registrar + _eventos.emit (con viewModelScope.launch)
    // TODO Paso 5: faltan (StateFlow<Int?>), recordarEn(segundos = 10), cancelarRecordatorio()
    // TODO Paso 6: escuchandoAvion (StateFlow<Boolean>), cambiarEscucha(valor), alCambiarModoAvion(activo)
    // TODO Paso 8: fun noSePudoMostrar()
}
