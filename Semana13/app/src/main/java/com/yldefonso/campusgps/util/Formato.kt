package com.yldefonso.campusgps.util

import java.util.Locale
import kotlin.math.roundToInt

/**
 * Convierte metros a texto legible.
 *  - Menos de 1000 m  -> metros enteros:       "350 m"
 *  - Desde 1000 m     -> kilómetros con 1 decimal: "1.5 km"
 */
fun formatearDistancia(metros: Float): String =
    if (metros < 1000f) {
        "${metros.roundToInt()} m"                          // redondea al entero más cercano
    } else {
        // Locale.US garantiza punto decimal (1.5) aunque el celular esté en español.
        String.format(Locale.US, "%.1f km", metros / 1000f)
    }