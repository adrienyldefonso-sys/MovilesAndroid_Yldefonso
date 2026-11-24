package com.yldefonso.murocampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaMuro = lightColorScheme(
    primary = AzulMuro,
    onPrimary = Color.White,
    primaryContainer = AzulMuroClaro
)

@Composable
fun MuroCampusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaMuro,
        typography = MuroTypography,
        content = content
    )
}
