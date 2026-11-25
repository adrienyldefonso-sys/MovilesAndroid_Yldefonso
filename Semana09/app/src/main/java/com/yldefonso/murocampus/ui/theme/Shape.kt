package com.yldefonso.murocampus.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Esquinas: un solo sistema para toda la app.
val MuroShapes = Shapes(
    small = RoundedCornerShape(12.dp),  // campos y tarjetas de comentarios
    medium = RoundedCornerShape(16.dp), // tarjetas de publicación
    large = RoundedCornerShape(20.dp)
)

// Píldora: chips, etiquetas y botones principales.
val FormaPildora = RoundedCornerShape(percent = 50)

// Espaciados: usa siempre estos valores, no números sueltos.
object Medidas {
    val XS = 4.dp
    val S = 8.dp
    val M = 12.dp
    val L = 16.dp
}