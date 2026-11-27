// Mejora 3 (commit 3): receptor del botón Posponer 10 s.
package com.yldefonso.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationManagerCompat
import com.yldefonso.notificacion.Notificador

class PosponerReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_ID = "posponer_id"
        const val EXTRA_TITULO = "posponer_titulo"
        const val EXTRA_TEXTO = "posponer_texto"
        const val EXTRA_URGENTE = "posponer_urgente"
        const val RETRASO_MS = 10_000L
    }

    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val titulo = intent.getStringExtra(EXTRA_TITULO) ?: return
        val texto = intent.getStringExtra(EXTRA_TEXTO) ?: return
        val urgente = intent.getBooleanExtra(EXTRA_URGENTE, false)

        // Quita la notificación actual para que no quede duplicada.
        NotificationManagerCompat.from(context).cancel(id)

        // onReceive debe ser corto: programamos la repetición y salimos.
        // applicationContext evita retener la Activity en memoria mientras espera.
        val appContext = context.applicationContext
        Handler(Looper.getMainLooper()).postDelayed({
            Notificador.mostrarPospuesto(appContext, id, titulo, texto, urgente)
        }, RETRASO_MS)
    }
}