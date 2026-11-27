// [G] Commit 3: se implementa permisoRespondido. Lo demás sigue como TODO.
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

    val avisos: StateFlow<List<Aviso>> = repo.avisos

    private val _eventos = MutableSharedFlow<Aviso>()
    val eventos: SharedFlow<Aviso> = _eventos.asSharedFlow()

    private val _permiso = MutableStateFlow<PermisoUi>(PermisoUi.SinPedir)
    val permiso: StateFlow<PermisoUi> = _permiso.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    // Paso 2 (commit 3): la pantalla informa el resultado del cuadro de permiso.
    // "concedido" viene del lanzador de Android o de tienePermiso en API < 33.
    fun permisoRespondido(concedido: Boolean) {
        _permiso.value = if (concedido) PermisoUi.Concedido else PermisoUi.Denegado
    }

    // TODO Paso 3: fun avisarAhora()
    // TODO Paso 5: faltan, recordarEn(segundos = 10), cancelarRecordatorio()
    // TODO Paso 6: escuchandoAvion, cambiarEscucha(valor), alCambiarModoAvion(activo)
    // TODO Paso 8: fun noSePudoMostrar()
}