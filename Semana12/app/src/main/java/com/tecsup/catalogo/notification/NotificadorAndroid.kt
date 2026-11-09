package com.tecsup.catalogo.notification

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
import com.tecsup.catalogo.MainActivity
import com.tecsup.catalogo.R

/** Implementación real de Notificador con NotificationCompat (canal + permiso POST_NOTIFICATIONS). */
class NotificadorAndroid(private val context: Context) : Notificador {

    init {
        crearCanal()
    }

    // minSdk = 26, así que los canales de notificación siempre existen.
    private fun crearCanal() {
        val canal = NotificationChannel(
            CANAL_ID,
            "Sincronización",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Avisa cuando termina de sincronizar los smartphones"
        }
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(canal)
    }

    @SuppressLint("MissingPermission") // el permiso se comprueba en tienePermiso()
    override fun mostrar(titulo: String, texto: String) {
        // Si el permiso está negado, no se muestra nada y la app sigue funcionando normal (PA-14).
        if (!tienePermiso()) return
        val manager = NotificationManagerCompat.from(context)
        if (!manager.areNotificationsEnabled()) return

        // Al tocar la notificación se abre la app.
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificacion = NotificationCompat.Builder(context, CANAL_ID)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle(titulo)
            .setContentText(texto)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        try {
            manager.notify(NOTIFICACION_ID, notificacion)
        } catch (e: SecurityException) {
            // El permiso se retiró justo ahora: no se hace nada.
        }
    }

    // El permiso POST_NOTIFICATIONS solo se pide desde Android 13 (TIRAMISU).
    private fun tienePermiso(): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED

    private companion object {
        const val CANAL_ID = "sincronizacion"
        const val NOTIFICACION_ID = 1001 // fijo: una nueva notificación reemplaza a la anterior
    }
}