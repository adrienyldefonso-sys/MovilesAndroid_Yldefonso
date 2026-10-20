package com.yldefonso.lavacampus.ui.components

import com.yldefonso.lavacampus.model.Unidad
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.yldefonso.lavacampus.data.local.entity.Servicio

private val localeEs: Locale = Locale.forLanguageTag("es-PE")

fun soles(monto: Double): String = "S/ " + String.format(Locale.US, "%.2f", monto)

fun formatoCantidad(cantidad: Double, unidad: String): String {
    val n = if (cantidad % 1.0 == 0.0) String.format(Locale.US, "%.0f", cantidad)
    else String.format(Locale.US, "%.1f", cantidad)
    return when (Unidad.desde(unidad)) {
        Unidad.KG -> "$n kg"
        Unidad.PRENDA -> if (cantidad == 1.0) "$n prenda" else "$n prendas"
        Unidad.UNIDAD -> if (cantidad == 1.0) "$n unidad" else "$n unidades"
    }
}

/** "2026-10-21" -> "Miércoles 21 de octubre" */
fun fechaLarga(iso: String): String =
    LocalDate.parse(iso)
        .format(DateTimeFormatter.ofPattern("EEEE d 'de' MMMM", localeEs))
        .replaceFirstChar { it.uppercase() }

/** "2026-10-21" -> "21/10/2026" */
fun fechaCorta(iso: String): String =
    LocalDate.parse(iso).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

fun Servicio.textoPrecio(): String =
    "${soles(precio)} por ${Unidad.desde(unidad).etiqueta}"