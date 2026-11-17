package com.yldefonso.campusgps.util

import android.location.Location
import com.yldefonso.campusgps.model.Lugar
import com.yldefonso.campusgps.model.Posicion

/**
 * Distancia en METROS entre la posición del usuario y un lugar del campus.
 * Función pura: no guarda estado ni toca la UI (por eso vive en util/ y no en un Composable).
 */
fun distanciaMetros(desde: Posicion, hasta: Lugar): Float {
    // distanceBetween no devuelve el valor: lo escribe en la posición [0] de este arreglo.
    val resultado = FloatArray(1)
    // ORDEN OBLIGATORIO: (lat1, lon1, lat2, lon2, resultado). Si inviertes lat y lon,
    // las distancias salen enormes (error frecuente del laboratorio).
    Location.distanceBetween(
        desde.latitud, desde.longitud,
        hasta.latitud, hasta.longitud,
        resultado
    )
    return resultado[0]
}