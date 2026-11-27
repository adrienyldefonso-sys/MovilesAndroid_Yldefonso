// Commit 3 (mejora 3): acción Posponer 10 s en la notificación.
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
import com.yldefonso.receiver.PosponerReceiver

object Notificador {

    const val CANAL = "avisos"
    const val CANAL_URGENTE = "urgentes"

    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val avisos = NotificationChannel(CANAL, "Avisos", NotificationManager.IMPORTANCE_DEFAULT)
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

    // Muestra un aviso nuevo. Sin permiso devuelve false sin lanzar error.
    fun mostrar(context: Context, aviso: Aviso): Boolean {
        if (!tienePermiso(context)) return false
        construirYMostrar(context, aviso.id, aviso.titulo, aviso.texto, aviso.urgente)
        return true
    }

    // Mejora 3: vuelve a mostrar una notificación pospuesta. Lo llama PosponerReceiver.
    fun mostrarPospuesto(context: Context, id: Int, titulo: String, texto: String, urgente: Boolean) {
        if (!tienePermiso(context)) return
        construirYMostrar(context, id, titulo, texto, urgente)
    }

    @SuppressLint("MissingPermission")
    private fun construirYMostrar(
        context: Context,
        id: Int,
        titulo: String,
        texto: String,
        urgente: Boolean
    ) {
        // PendingIntent para abrir la app al tocar la notificación (mejora del commit 5 del lab).
        val intent = Intent(context, MainActivity::class.java)
        val pendiente = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        // Mejora 3: el botón Posponer lleva los datos del aviso al receptor.
        val intentPosponer = Intent(context, PosponerReceiver::class.java).apply {
            putExtra(PosponerReceiver.EXTRA_ID, id)
            putExtra(PosponerReceiver.EXTRA_TITULO, titulo)
            putExtra(PosponerReceiver.EXTRA_TEXTO, texto)
            putExtra(PosponerReceiver.EXTRA_URGENTE, urgente)
        }
        // requestCode = id: cada notificación tiene su propio PendingIntent de posponer.
        val pendientePosponer = PendingIntent.getBroadcast(
            context, id, intentPosponer, PendingIntent.FLAG_IMMUTABLE
        )

        val canal = if (urgente) CANAL_URGENTE else CANAL

        val notificacion = NotificationCompat.Builder(context, canal)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle(titulo)
            .setContentText(texto)
            .setStyle(NotificationCompat.BigTextStyle().bigText(texto))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendiente)
            .addAction(R.drawable.ic_notificacion, "Posponer 10 s", pendientePosponer)
            .build()

        NotificationManagerCompat.from(context).notify(id, notificacion)
    }
}