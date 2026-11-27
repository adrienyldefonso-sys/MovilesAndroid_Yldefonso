// Tema Material 3 oscuro: los colores del esquema salen de la paleta de Color.kt.
package com.yldefonso.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun AvisosTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Indigo,
            onPrimary = Fondo,
            primaryContainer = SuperficieAlta,
            onPrimaryContainer = Indigo,
            secondary = Cian,
            onSecondary = Fondo,
            tertiary = Ambar,
            onTertiary = Fondo,
            background = Fondo,
            onBackground = Texto,
            surface = Superficie,
            onSurface = Texto,
            surfaceVariant = SuperficieAlta,
            onSurfaceVariant = TextoSuave,
            error = Coral,
            onError = Fondo,
            errorContainer = Color(0x33F87171),
            onErrorContainer = Coral
        ),
        content = content
    )
}