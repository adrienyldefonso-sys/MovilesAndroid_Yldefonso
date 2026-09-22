package com.yldefonso.navlab.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Paleta de color para el tema "Portal Académico"
val PurpleDark = Color(0xFF3D2266)  // Morado MUY oscuro para el tope del degradado
val PurpleMid = Color(0xFF6A4C9C)   // Morado medio
val PurpleLight = Color(0xFFB39DDB) // Morado claro/lavanda para la base del degradado

// Gradiente morado de 3 paradas para alto contraste visual (oscuro arriba, medio centro, lavanda abajo)
val BrandGradient = Brush.verticalGradient(
    colors = listOf(PurpleDark, PurpleMid, PurpleLight)
)

// Colores de fondo para tarjetas y pantallas
val CardGray = Color(0xFFF2F2F2)        // Fondo de cada card individual del directorio
val DetailCardGray = Color(0xFFE0E0E0)  // Fondo gris medio para la card de expediente
val BackgroundLilac = Color(0xFFF5F2FB) // Fondo general de pantallas claras como Login y List
val ErrorRed = Color(0xFFD32F2F)        // Color de error/salida para el boton de cerrar sesion

// URL del avatar del usuario logueado (Becker Yldefonso), reutilizada en ProfileScreen y StudentRepository
const val BECKER_AVATAR_URL = "https://randomuser.me/api/portraits/men/45.jpg"
