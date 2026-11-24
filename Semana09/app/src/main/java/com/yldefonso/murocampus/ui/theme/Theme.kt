package com.yldefonso.murocampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Esquema de colores: define cómo se ven los componentes de Material 3 en toda la app
private val EsquemaMuro = lightColorScheme(
    primary = AzulMuro,
    onPrimary = Color.White,
    primaryContainer = AzulMuroClaro,
    background = Color.White,
    surface = Color.White,
    surfaceVariant = FondoTarjeta // fondo que usan las Card de la app
)

@Composable
fun MuroCampusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaMuro,
        typography = MuroTypography,
        content = content
    )
}