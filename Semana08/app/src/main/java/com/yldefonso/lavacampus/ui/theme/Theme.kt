package com.yldefonso.lavacampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tecsup.lavacampus.ui.theme.FondoOscuro
import com.tecsup.lavacampus.ui.theme.SuperficieOscura
import com.tecsup.lavacampus.ui.theme.TurquesaClaro
import com.tecsup.lavacampus.ui.theme.TurquesaContenedor
import com.tecsup.lavacampus.ui.theme.TurquesaContenedorOscuro
import com.tecsup.lavacampus.ui.theme.TurquesaFondo
import com.tecsup.lavacampus.ui.theme.TurquesaPrimario
import com.tecsup.lavacampus.ui.theme.VioletaClaro
import com.tecsup.lavacampus.ui.theme.VioletaContenedor
import com.tecsup.lavacampus.ui.theme.VioletaContenedorOscuro
import com.tecsup.lavacampus.ui.theme.VioletaFondo
import com.tecsup.lavacampus.ui.theme.VioletaPrimario
import com.yldefonso.lavacampus.model.Rol

@Composable
fun LavaTheme(
    rol: Rol = Rol.CLIENTE,
    modoOscuro: Boolean = false,
    content: @Composable () -> Unit
) {
    val admin = rol == Rol.ADMIN
    val colores = if (modoOscuro) {
        darkColorScheme(
            primary = if (admin) VioletaClaro else TurquesaClaro,
            onPrimary = Color(0xFF0B1220),
            primaryContainer = if (admin) VioletaContenedorOscuro else TurquesaContenedorOscuro,
            onPrimaryContainer = Color.White,
            background = FondoOscuro,
            onBackground = Color(0xFFE2E8F0),
            surface = SuperficieOscura,
            onSurface = Color(0xFFE2E8F0)
        )
    } else {
        lightColorScheme(
            primary = if (admin) VioletaPrimario else TurquesaPrimario,
            onPrimary = Color.White,
            primaryContainer = if (admin) VioletaContenedor else TurquesaContenedor,
            onPrimaryContainer = if (admin) Color(0xFF3B0764) else Color(0xFF164E63),
            background = if (admin) VioletaFondo else TurquesaFondo,
            onBackground = Color(0xFF0F172A),
            surface = Color.White,
            onSurface = Color(0xFF0F172A)
        )
    }
    MaterialTheme(colorScheme = colores, typography = LavaTypography, content = content)
}
