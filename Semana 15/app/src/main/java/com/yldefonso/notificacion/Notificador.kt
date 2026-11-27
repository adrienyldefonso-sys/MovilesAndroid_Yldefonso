// Commit 4: se implementa mostrar con NotificationCompat. crearCanal (commit 2) y tienePermiso (commit 3) ya estaban.
package com.yldefonso.notificacion

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
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

    // Paso 3 (commit 4): construye y muestra la notificación.
    // SuppressLint: el permiso ya se comprueba en la línea de abajo, antes de llamar a notify.
    @SuppressLint("MissingPermission")
    fun mostrar(context: Context, aviso: Aviso): Boolean {
        // Sin permiso no se lanza nada: se devuelve false y la pantalla lo informa.
        if (!tienePermiso(context)) return false

        // Builder: ícono pequeño, título, texto y prioridad. setAutoCancel quita la notificación al tocarla.
        val notificacion = NotificationCompat.Builder(context, CANAL)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle(aviso.titulo)
            .setContentText(aviso.texto)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        // El id del aviso distingue cada notificación: dos avisos = dos notificaciones.
        NotificationManagerCompat.from(context).notify(aviso.id, notificacion)
        return true
    }
}