package com.yldefonso.lavacampus.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yldefonso.lavacampus.model.Rol

val LavaShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun LavaTheme(
    rol: Rol = Rol.CLIENTE,
    modoOscuro: Boolean = false,
    content: @Composable () -> Unit
) {
    val admin = rol == Rol.ADMIN
    val colores = if (modoOscuro) {
        if (admin) {
            darkColorScheme(
                primary = VioletaClaro,
                onPrimary = Color(0xFF2E1065),
                primaryContainer = VioletaContenedorOscuro,
                onPrimaryContainer = Color.White,
                secondary = VioletaSecundarioOscuro,
                onSecondary = Color(0xFF3B0764),
                secondaryContainer = VioletaSecundarioContenedorOscuro,
                onSecondaryContainer = Color(0xFFF3E8FF),
                tertiary = VioletaTerciarioOscuro,
                onTertiary = Color(0xFF4A044E),
                tertiaryContainer = VioletaTerciarioContenedorOscuro,
                onTertiaryContainer = Color(0xFFFAE8FF),
                background = FondoOscuro,
                onBackground = Color(0xFFE2E8F0),
                surface = SuperficieOscura,
                onSurface = Color(0xFFE2E8F0),
                surfaceVariant = VioletaSurfaceVariantOscuro,
                onSurfaceVariant = VioletaOnSurfaceVariantOscuro,
                outline = VioletaOutlineOscuro,
                outlineVariant = VioletaOutlineVariantOscuro,
                error = RojoErrorOscuro,
                onError = Color(0xFF450A0A),
                errorContainer = RojoErrorContenedorOscuro,
                onErrorContainer = OnRojoErrorContenedorOscuro
            )
        } else {
            darkColorScheme(
                primary = TurquesaClaro,
                onPrimary = Color(0xFF083344),
                primaryContainer = TurquesaContenedorOscuro,
                onPrimaryContainer = Color.White,
                secondary = TurquesaSecundarioOscuro,
                onSecondary = Color(0xFF0C4A6E),
                secondaryContainer = TurquesaSecundarioContenedorOscuro,
                onSecondaryContainer = Color(0xFFE0F2FE),
                tertiary = TurquesaTerciarioOscuro,
                onTertiary = Color(0xFF042F2C),
                tertiaryContainer = TurquesaTerciarioContenedorOscuro,
                onTertiaryContainer = Color(0xFFCCFBF1),
                background = FondoOscuro,
                onBackground = Color(0xFFE2E8F0),
                surface = SuperficieOscura,
                onSurface = Color(0xFFE2E8F0),
                surfaceVariant = TurquesaSurfaceVariantOscuro,
                onSurfaceVariant = TurquesaOnSurfaceVariantOscuro,
                outline = TurquesaOutlineOscuro,
                outlineVariant = TurquesaOutlineVariantOscuro,
                error = RojoErrorOscuro,
                onError = Color(0xFF450A0A),
                errorContainer = RojoErrorContenedorOscuro,
                onErrorContainer = OnRojoErrorContenedorOscuro
            )
        }
    } else {
        if (admin) {
            lightColorScheme(
                primary = VioletaPrimario,
                onPrimary = Color.White,
                primaryContainer = VioletaContenedor,
                onPrimaryContainer = Color(0xFF3B0764),
                secondary = VioletaSecundario,
                onSecondary = Color.White,
                secondaryContainer = VioletaSecundarioContenedor,
                onSecondaryContainer = VioletaOnSecundarioContenedor,
                tertiary = VioletaTerciario,
                onTertiary = Color.White,
                tertiaryContainer = VioletaTerciarioContenedor,
                onTertiaryContainer = VioletaOnTerciarioContenedor,
                background = VioletaFondo,
                onBackground = Color(0xFF0F172A),
                surface = Color.White,
                onSurface = Color(0xFF0F172A),
                surfaceVariant = VioletaSurfaceVariant,
                onSurfaceVariant = VioletaOnSurfaceVariant,
                outline = VioletaOutline,
                outlineVariant = VioletaOutlineVariant,
                error = RojoError,
                onError = Color.White,
                errorContainer = RojoErrorContenedor,
                onErrorContainer = OnRojoErrorContenedor
            )
        } else {
            lightColorScheme(
                primary = TurquesaPrimario,
                onPrimary = Color.White,
                primaryContainer = TurquesaContenedor,
                onPrimaryContainer = Color(0xFF164E63),
                secondary = TurquesaSecundario,
                onSecondary = Color.White,
                secondaryContainer = TurquesaSecundarioContenedor,
                onSecondaryContainer = TurquesaOnSecundarioContenedor,
                tertiary = TurquesaTerciario,
                onTertiary = Color.White,
                tertiaryContainer = TurquesaTerciarioContenedor,
                onTertiaryContainer = TurquesaOnTerciarioContenedor,
                background = TurquesaFondo,
                onBackground = Color(0xFF0F172A),
                surface = Color.White,
                onSurface = Color(0xFF0F172A),
                surfaceVariant = TurquesaSurfaceVariant,
                onSurfaceVariant = TurquesaOnSurfaceVariant,
                outline = TurquesaOutline,
                outlineVariant = TurquesaOutlineVariant,
                error = RojoError,
                onError = Color.White,
                errorContainer = RojoErrorContenedor,
                onErrorContainer = OnRojoErrorContenedor
            )
        }
    }

    MaterialTheme(
        colorScheme = colores,
        typography = LavaTypography,
        shapes = LavaShapes,
        content = content
    )
}
