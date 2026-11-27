// [G] GUIADO: esqueleto. Se completa en los pasos 1, 2, 3, 4 (y 2 para tienePermiso).
// Solo esta capa (y la View) toca el sistema de notificaciones de Android.
package com.yldefonso.notificacion

import android.content.Context
import com.yldefonso.model.Aviso

object Notificador {

    // Id del canal: debe ser fijo, ya que se usa al crear el canal y al notificar.
    const val CANAL = "avisos"

    // Paso 1: crea el canal "Avisos" (obligatorio desde Android 8 / API 26).
    fun crearCanal(context: Context) {
        // TODO Paso 1: si Build.VERSION.SDK_INT >= O, crear NotificationChannel(CANAL, "Avisos", IMPORTANCE_DEFAULT)
        //              y registrarlo con NotificationManager.createNotificationChannel
    }

    // Paso 2: true si la app puede mostrar notificaciones.
    fun tienePermiso(context: Context): Boolean {
        // TODO Paso 2: si SDK_INT < 33 devolver true; si no, ContextCompat.checkSelfPermission(POST_NOTIFICATIONS)
        return false
    }

    // Paso 3: muestra la notificación. Devuelve false si no hay permiso (sin lanzar error).
    fun mostrar(context: Context, aviso: Aviso): Boolean {
        // TODO 1 (Paso 3): si !tienePermiso(context) -> return false
        // TODO 2 (Paso 3): construir NotificationCompat.Builder(context, CANAL)
        // TODO 3 (Paso 3): NotificationManagerCompat.from(context).notify(aviso.id, notificacion); return true
        return false
    }
}
