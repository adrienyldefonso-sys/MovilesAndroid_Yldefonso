// [A] Commit 7: receptor de anuncios del sistema para el modo avión.
package com.yldefonso.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

// El receptor recibe el anuncio de Android y solo reenvía el estado (true = activado).
// onReceive debe ser corto: no hace trabajo largo ni usa Context para notificar.
class ModoAvionReceiver(private val alCambiar: (Boolean) -> Unit) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Filtro de seguridad: solo procesa el anuncio de cambio de modo avión.
        if (intent.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            // El extra "state" indica si el modo avión quedó activado.
            alCambiar(intent.getBooleanExtra("state", false))
        }
    }
}