// [G] GUIADO: esqueleto. Se completa en el Paso 1.
package com.yldefonso

import android.app.Application
import com.yldefonso.notificacion.Notificador

// Subclase de Application: se ejecuta una sola vez al iniciar el proceso de la app.
// Debe registrarse en el Manifest con android:name=".AvisosApp" (Paso 1).
class AvisosApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // TODO Paso 1: Notificador.crearCanal(this)  -> el canal existe antes de cualquier aviso
    }
}
