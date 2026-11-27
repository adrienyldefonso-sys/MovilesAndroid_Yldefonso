// Commit 6: se implementa el recordatorio con corrutina y cancelación. Lo demás sigue como TODO.
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

    // Paso 5 (commit 6): referencia al trabajo de la cuenta regresiva.
    // Sirve para no iniciar dos recordatorios a la vez y para poder cancelar.
    private var trabajo: Job? = null

    // Segundos que faltan: null = no hay recordatorio activo.
    private val _faltan = MutableStateFlow<Int?>(null)
    val faltan: StateFlow<Int?> = _faltan.asStateFlow()

    // Inicia la cuenta regresiva de "segundos" a 1 y, al terminar, registra el aviso.
    fun recordarEn(segundos: Int = 10) {
        // Si ya hay una cuenta en curso, no se inicia otra.
        if (trabajo?.isActive == true) return

        trabajo = viewModelScope.launch {
            try {
                // Cuenta regresiva: actualiza "faltan" cada segundo (10, 9, ... 1).
                for (s in segundos downTo 1) {
                    _faltan.value = s
                    delay(1000)   // suspende la corrutina 1 segundo; no bloquea la pantalla
                }
                // Llegó a 0: se registra y se emite el aviso del recordatorio.
                val aviso = repo.registrar("Recordatorio", "Revisa tu laboratorio pendiente", Origen.RECORDATORIO)
                _eventos.emit(aviso)
            } finally {
                // Se ejecuta siempre: terminó bien o fue cancelado. Así la pantalla vuelve a su estado normal.
                _faltan.value = null
            }
        }
    }

    // Cancela la corrutina. Como se cancela antes de llegar al emit, no se muestra ningún aviso.
    fun cancelarRecordatorio() {
        trabajo?.cancel()
    }

    // TODO Paso 6: escuchandoAvion, cambiarEscucha(valor), alCambiarModoAvion(activo)
    // TODO Paso 8: fun noSePudoMostrar()
}