package com.yldefonso.lavacampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.yldefonso.lavacampus.model.Rol
import com.yldefonso.lavacampus.ui.navigation.NavLava
import com.yldefonso.lavacampus.ui.theme.LavaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Pasos 6 y 13: el rol y el modo oscuro vendrán de DataStore
            LavaTheme(rol = Rol.CLIENTE, modoOscuro = false) {
                NavLava()
            }
        }
    }
}