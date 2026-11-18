package com.yldefonso.campusgps.data

import com.yldefonso.campusgps.model.Posicion

/**
 * Se lanza cuando Google responde pero NO puede determinar la ubicación
 * (típico con el GPS apagado). Permite al ViewModel distinguir este caso de otros fallos.
 */
class UbicacionNoDisponibleException : Exception("No se pudo obtener la ubicación")

/**
 * Contrato para obtener la ubicación. El ViewModel solo conoce ESTA interfaz,
 * nunca a Google: así podríamos cambiar de proveedor (o hacer pruebas con un falso)
 * sin tocar el ViewModel.
 */
interface UbicacionRepository {
    // suspend = función que puede esperar (corrutina) sin bloquear la pantalla.
    // Result<T> encapsula éxito o fallo sin lanzar excepciones hacia arriba.
    suspend fun obtenerPosicion(): Result<Posicion>

    // Paso 8: el ViewModel pregunta esto ANTES de buscar. Así no necesita un Context:
    // el Context vive dentro de la implementación.
    fun playServicesDisponible(): Boolean
}