// [E] ENTREGADO: no modificar.
package com.yldefonso.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Tema Material 3 de la app: envuelve toda la UI.
@Composable
fun AvisosTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = AzulPrimario,
            onPrimary = Color.White
        ),
        content = content
    )
}
