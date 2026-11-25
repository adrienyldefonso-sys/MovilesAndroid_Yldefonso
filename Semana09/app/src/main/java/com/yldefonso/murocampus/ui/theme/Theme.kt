package com.yldefonso.murocampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// El esquema define cómo se ven TODOS los componentes de Material 3.
// Por eso cambiar un color aquí cambia toda la app.
private val EsquemaMuro = lightColorScheme(
    primary = IndigoProfundo,
    onPrimary = Color.White,
    primaryContainer = IndigoSuave,
    secondary = Ambar,
    onSecondary = AmbarOscuro,
    background = Color.White,
    surface = Color.White,
    surfaceVariant = FondoTarjeta,
    onSurface = TextoPrimario,
    onSurfaceVariant = TextoSecundario
)

@Composable
fun MuroCampusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaMuro,
        typography = MuroTypography,
        shapes = MuroShapes, // las esquinas también vienen del tema
        content = content
    )
}