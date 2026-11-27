// Commit 2: la clase Application crea el canal al iniciar la app.
package com.yldefonso

import android.app.Application
import com.yldefonso.notificacion.Notificador

// Subclase de Application: Android la crea una sola vez, antes que cualquier pantalla.
// Debe estar registrada en el Manifest con android:name=".AvisosApp".
class AvisosApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // El canal existe desde el arranque, así que cualquier aviso posterior ya tiene dónde mostrarse.
        Notificador.crearCanal(this)
    }
}