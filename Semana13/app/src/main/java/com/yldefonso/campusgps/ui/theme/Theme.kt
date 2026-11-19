package com.yldefonso.campusgps.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val EsquemaClaro = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = Secondary.copy(alpha = 0.2f),
    onPrimaryContainer = PrimaryDark,
    secondary = Secondary,
    onSecondary = Color.White,
    background = BackgroundApp,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariantColor,
    onSurfaceVariant = TextSecondary,
    outline = OutlineColor,
    error = RojoError
)

private val EsquemaOscuro = darkColorScheme(
    primary = Secondary,
    onPrimary = Color.White,
    secondary = Primary,
    onSecondary = Color.White,
    background = Color(0xFF121019),
    onBackground = Color(0xFFE8E1F5),
    surface = Color(0xFF1F1B2E),
    onSurface = Color(0xFFE8E1F5),
    error = RojoError
)

private val AppShapes = Shapes(
    small = RoundedCornerShape(Dimens.espacioS),
    medium = RoundedCornerShape(Dimens.radioEsquina),
    large = RoundedCornerShape(24.dp)
)

/** Envuelve toda la app: elige colores claros u oscuros según el tema del sistema. */
@Composable
fun CampusGPSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) EsquemaOscuro else EsquemaClaro,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}
