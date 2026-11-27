// Commit 3: se implementa tienePermiso. crearCanal ya estaba (commit 2); mostrar sigue en TODO (commit 4).
package com.yldefonso.notificacion

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
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
        // Antes de Android 13 (API 33) no existe el permiso en tiempo de ejecución: se considera concedido.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        // Desde API 33 hay que consultar el permiso actual del usuario.
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Paso 3 (commit 4): muestra la notificación.
    fun mostrar(context: Context, aviso: Aviso): Boolean {
        // TODO 1 (Paso 3): si !tienePermiso(context) -> return false
        // TODO 2 (Paso 3): construir NotificationCompat.Builder(context, CANAL)
        // TODO 3 (Paso 3): NotificationManagerCompat.from(context).notify(aviso.id, notificacion); return true
        return false
    }
}