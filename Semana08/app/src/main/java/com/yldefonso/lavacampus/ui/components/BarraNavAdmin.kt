package com.yldefonso.lavacampus.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.yldefonso.lavacampus.ui.navigation.Rutas

private enum class DestinoAdmin(
    val ruta: String,
    val etiqueta: String,
    val activo: ImageVector,
    val inactivo: ImageVector
) {
    PANEL(Rutas.PANEL, "Panel", Icons.Filled.BarChart, Icons.Outlined.BarChart),
    PEDIDOS(Rutas.BANDEJA, "Pedidos", Icons.Filled.Menu, Icons.Outlined.Menu),
    SERVICIOS(Rutas.SERVICIOS, "Servicios", Icons.Filled.LocalOffer, Icons.Outlined.LocalOffer),
    CLIENTES(Rutas.CLIENTES, "Clientes", Icons.Filled.Person, Icons.Outlined.Person),
    AJUSTES(Rutas.AJUSTES, "Ajustes", Icons.Filled.Settings, Icons.Outlined.Settings)
}

@Composable
fun BarraNavAdmin(rutaActual: String?, onIr: (String) -> Unit) {
    val primario = MaterialTheme.colorScheme.primary
    val apagado = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)

    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        DestinoAdmin.entries.forEach { d ->
            val seleccionado = rutaActual == d.ruta
            NavigationBarItem(
                selected = seleccionado,
                onClick = { onIr(d.ruta) },
                icon = {
                    Icon(
                        imageVector = if (seleccionado) d.activo else d.inactivo,
                        contentDescription = d.etiqueta
                    )
                },
                label = { Text(d.etiqueta) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = primario,
                    selectedTextColor = primario,
                    indicatorColor = Color.Transparent,   // sin "píldora", como en el mockup
                    unselectedIconColor = apagado,
                    unselectedTextColor = apagado
                )
            )
        }
    }
}