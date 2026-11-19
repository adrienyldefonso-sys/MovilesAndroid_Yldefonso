package com.yldefonso.campusgps.util

import java.util.Locale
import kotlin.math.ceil
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

/**
 * Calcula el tiempo estimado a pie a razón de 80 metros por minuto.
 * Redondea hacia arriba y asegura un mínimo de 1 minuto.
 */
fun formatearTiempoAPie(metros: Float): String {
    val minutos = maxOf(1, ceil(metros / 80f).toInt())
    return "~$minutos min a pie"
}
