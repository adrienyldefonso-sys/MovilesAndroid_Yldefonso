// [E] ENTREGADO: no modificar.
package com.yldefonso.data

import com.yldefonso.model.Aviso
import com.yldefonso.model.Origen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Contrato del Repository: la capa ViewModel solo conoce esta interface.
interface AvisosRepository {
    val avisos: StateFlow<List<Aviso>>
    fun registrar(titulo: String, texto: String, origen: Origen): Aviso
}

// Implementación en memoria: el historial vive mientras la app esté abierta.
class AvisosRepositoryMemoria : AvisosRepository {

    private val _avisos = MutableStateFlow<List<Aviso>>(emptyList())
    override val avisos: StateFlow<List<Aviso>> = _avisos.asStateFlow()

    // Contador para dar un id distinto a cada aviso
    private var siguiente = 1

    override fun registrar(titulo: String, texto: String, origen: Origen): Aviso {
        // Hora local en formato HH:mm:ss
        val hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val aviso = Aviso(siguiente++, titulo, texto, origen, hora)
        // Lo más reciente va arriba: nueva lista = [aviso] + lista anterior
        _avisos.value = listOf(aviso) + _avisos.value
        return aviso
    }
}
