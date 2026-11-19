package com.yldefonso.campusgps.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.yldefonso.campusgps.model.Lugar
import java.util.Locale

/**
 * [G] Utilidad para abrir la ruta a pie hacia un lugar utilizando Google Maps o un navegador web.
 */
fun abrirRutaAPie(contexto: Context, lugar: Lugar) {
    // Formatea la latitud y longitud usando Locale.US para asegurar el punto decimal.
    val destino = String.format(Locale.US, "%.6f,%.6f", lugar.latitud, lugar.longitud)
    val uri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$destino&travelmode=walking")
    val intent = Intent(Intent.ACTION_VIEW, uri)

    try {
        contexto.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // Si no hay ninguna app instalada para manejar la URI, muestra un Toast informativo.
        Toast.makeText(contexto, "No se pudo abrir la ruta", Toast.LENGTH_SHORT).show()
    }
}
