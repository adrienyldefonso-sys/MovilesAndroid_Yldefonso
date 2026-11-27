// Commit 5: al tocar la notificación se abre MainActivity mediante un PendingIntent.
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

    // Paso 1 (commit 2): crea el canal "Avisos".
    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(CANAL, "Avisos", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }
    }

    // Paso 2 (commit 3): true si la app puede mostrar notificaciones.
    fun tienePermiso(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Paso 3 (commit 4) y Paso 4 (commit 5): construye y muestra la notificación.
    @SuppressLint("MissingPermission")
    fun mostrar(context: Context, aviso: Aviso): Boolean {
        if (!tienePermiso(context)) return false

        // Intent: la acción de abrir MainActivity cuando se toque la notificación.
        val intent = Intent(context, MainActivity::class.java)

        // PendingIntent: Android lo ejecuta después, desde la notificación.
        // FLAG_IMMUTABLE es obligatorio en Android 12 (API 31) o superior, para que nadie
        // pueda cambiar el intent después de creado.
        val pendiente = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val notificacion = NotificationCompat.Builder(context, CANAL)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle(aviso.titulo)
            .setContentText(aviso.texto)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)                // al tocarla, la notificación desaparece
            .setContentIntent(pendiente)        // al tocarla, se abre la app
            .build()

        NotificationManagerCompat.from(context).notify(aviso.id, notificacion)
        return true
    }
}