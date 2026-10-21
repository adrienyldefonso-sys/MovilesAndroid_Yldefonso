package com.yldefonso.lavacampus.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

/** [claro] = true: barra sin fondo de color, con título oscuro (como en el mockup). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    titulo: String,
    onAtras: (() -> Unit)? = null,
    claro: Boolean = false,
    acciones: @Composable RowScope.() -> Unit = {}
) {
    val fondo = if (claro) Color.Transparent else MaterialTheme.colorScheme.primary
    val contenido = if (claro) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onPrimary

    CenterAlignedTopAppBar(
        title = { Text(titulo, fontWeight = FontWeight.SemiBold) },
        navigationIcon = {
            if (onAtras != null) {
                IconButton(onClick = onAtras) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Volver")
                }
            }
        },
        actions = acciones,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = fondo,
            titleContentColor = contenido,
            navigationIconContentColor = contenido,
            actionIconContentColor = contenido
        )
    )
}