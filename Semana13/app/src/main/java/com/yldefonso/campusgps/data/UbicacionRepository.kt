package com.yldefonso.campusgps.data

import com.yldefonso.campusgps.model.Posicion

/**
 * Contrato para obtener la ubicación. El ViewModel solo conoce ESTA interfaz,
 * nunca a Google: así podríamos cambiar de proveedor (o hacer pruebas con un falso)
 * sin tocar el ViewModel.
 */
interface UbicacionRepository {
    // suspend = función que puede esperar (corrutina) sin bloquear la pantalla.
    // Result<T> encapsula éxito o fallo sin lanzar excepciones hacia arriba.
    suspend fun obtenerPosicion(): Result<Posicion>
}
