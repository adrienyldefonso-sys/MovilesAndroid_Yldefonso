package com.tecsup.catalogo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.tecsup.catalogo.ui.navigation.AppNav
import com.tecsup.catalogo.ui.theme.CatalogoTheme

class MainActivity : ComponentActivity() {

    // Si la persona niega el permiso, la app funciona igual: solo no se muestran avisos.
    private val pedirPermisoNotificaciones =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Solo se pregunta al abrir la app, no al girar la pantalla.
        if (savedInstanceState == null) pedirPermisoNotificacionesSiHaceFalta()

        val factory = ViewModelFactory(AppContainer(applicationContext))
        setContent {
            CatalogoTheme {
                AppNav(factory)
            }
        }
    }

    // POST_NOTIFICATIONS es un permiso en tiempo de ejecución desde Android 13.
    private fun pedirPermisoNotificacionesSiHaceFalta() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            pedirPermisoNotificaciones.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}