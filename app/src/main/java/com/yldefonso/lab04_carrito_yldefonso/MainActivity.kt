package com.yldefonso.lab04_carrito_yldefonso

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.yldefonso.lab04_carrito_yldefonso.ui.screens.PantallaCarrito
import com.yldefonso.lab04_carrito_yldefonso.ui.theme.Lab04carritoyldefonsoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab04carritoyldefonsoTheme {
                PantallaCarrito()
            }
        }
    }
}