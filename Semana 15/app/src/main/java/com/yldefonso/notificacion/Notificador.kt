// Commit 2 (mejora 2): dos canales. Avisos (DEFAULT) y Urgentes (HIGH). El recordatorio usa Urgentes.
// Mejora 1 (BigTextStyle) se mantiene.
package com.yldefonso.notificacion

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.yldefonso.MainActivity
import com.yldefonso.R
import com.yldefonso.model.Aviso

object Notificador {

    const val CANAL = "avisos"
    // Mejora 2: id del canal urgente. Debe ser fijo, igual que CANAL.
    const val CANAL_URGENTE = "urgentes"

    // Crea los dos canales. Android no cambia la importancia de un canal ya creado,
    // así que el canal avisos de la versión anterior se conserva como estaba.
    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val avisos = NotificationChannel(CANAL, "Avisos", NotificationManager.IMPORTANCE_DEFAULT)
            // IMPORTANCE_HIGH: el canal urgente muestra la notificación emergente (heads-up).
            val urgentes = NotificationChannel(CANAL_URGENTE, "Urgentes", NotificationManager.IMPORTANCE_HIGH)
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannels(listOf(avisos, urgentes))
        }
    }

    fun tienePermiso(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    fun mostrar(context: Context, aviso: Aviso): Boolean {
        if (!tienePermiso(context)) return false

        val intent = Intent(context, MainActivity::class.java)
        val pendiente = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        // Elige el canal según el aviso: urgente va a Urgentes, lo demás a Avisos.
        val canal = if (aviso.urgente) CANAL_URGENTE else CANAL

        val notificacion = NotificationCompat.Builder(context, canal)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle(aviso.titulo)
            .setContentText(aviso.texto)
            .setStyle(NotificationCompat.BigTextStyle().bigText(aviso.texto))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendiente)
            .build()

        NotificationManagerCompat.from(context).notify(aviso.id, notificacion)
        return true
    }
}