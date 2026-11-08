package com.tecsup.catalogo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tecsup.catalogo.ui.lista.ListaScreen
import com.tecsup.catalogo.ui.theme.CatalogoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val factory = ViewModelFactory(AppContainer(applicationContext))
        setContent {
            CatalogoTheme {
                // H13/H14: se reemplaza por AppNav().
                ListaScreen(factory)
            }
        }
    }
}