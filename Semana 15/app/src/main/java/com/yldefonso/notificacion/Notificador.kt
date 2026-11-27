// [G] GUIADO: en el commit 2 se completa crearCanal. Mostrar y tienePermiso siguen como TODO.
// Solo esta capa (y la View) toca el sistema de notificaciones de Android.
package com.yldefonso.notificacion

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.yldefonso.model.Aviso

object Notificador {

    // Id del canal: debe ser fijo, porque se usa al crear el canal y al notificar.
    const val CANAL = "avisos"

    // Paso 1: crea el canal "Avisos". Desde Android 8 (API 26) todo aviso necesita un canal.
    fun crearCanal(context: Context) {
        // En versiones anteriores a API 26 los canales no existen, así que no hace nada.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // IMPORTANCE_DEFAULT: el aviso suena, pero no aparece emergente en pantalla
            val canal = NotificationChannel(CANAL, "Avisos", NotificationManager.IMPORTANCE_DEFAULT)
            // Obtiene el servicio del sistema que administra las notificaciones
            val manager = context.getSystemService(NotificationManager::class.java)
            // Registra el canal. Si ya existe, Android lo actualiza y no lo duplica.
            manager.createNotificationChannel(canal)
        }
    }

    // Paso 2: true si la app puede mostrar notificaciones (se completa en el commit 3).
    fun tienePermiso(context: Context): Boolean {
        // TODO Paso 2: si SDK_INT < 33 devolver true; si no, ContextCompat.checkSelfPermission(POST_NOTIFICATIONS)
        return false
    }

    // Paso 3: muestra la notificación (se completa en el commit 4).
    fun mostrar(context: Context, aviso: Aviso): Boolean {
        // TODO 1 (Paso 3): si !tienePermiso(context) -> return false
        // TODO 2 (Paso 3): construir NotificationCompat.Builder(context, CANAL)
        // TODO 3 (Paso 3): NotificationManagerCompat.from(context).notify(aviso.id, notificacion); return true
        return false
    }
}