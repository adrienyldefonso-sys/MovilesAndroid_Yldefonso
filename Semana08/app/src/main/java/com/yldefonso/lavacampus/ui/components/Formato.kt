package com.yldefonso.lavacampus.ui.components

import com.yldefonso.lavacampus.data.local.entity.Servicio
import com.yldefonso.lavacampus.data.local.relation.DetalleConServicio
import com.yldefonso.lavacampus.model.Unidad
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

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

/** "3.5 kg + 5 prendas": suma las cantidades por unidad. */
fun resumenCantidades(detalles: List<DetalleConServicio>): String =
    detalles.groupBy { Unidad.desde(it.servicio.unidad) }
        .map { (unidad, filas) -> formatoCantidad(filas.sumOf { it.detalle.cantidad }, unidad.name) }
        .joinToString(" + ")

/** "Ana Torres" -> "Ana T." */
fun nombreCorto(nombre: String): String {
    val partes = nombre.trim().split(" ").filter { it.isNotBlank() }
    return if (partes.size >= 2) "${partes[0]} ${partes[1].first()}." else nombre
}

/** "2026-10-22" -> "jue 22" */
fun diaCorto(iso: String): String =
    LocalDate.parse(iso)
        .format(DateTimeFormatter.ofPattern("EEE d", localeEs))
        .lowercase()
        .replace(".", "")