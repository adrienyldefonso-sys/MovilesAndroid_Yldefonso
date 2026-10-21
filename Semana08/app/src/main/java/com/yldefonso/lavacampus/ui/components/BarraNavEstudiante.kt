package com.yldefonso.lavacampus.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.yldefonso.lavacampus.ui.navigation.Rutas

private enum class DestinoEstudiante(val ruta: String, val etiqueta: String, val icono: ImageVector) {
    INICIO(Rutas.INICIO, "Inicio", Icons.Filled.Home),
    PEDIDOS(Rutas.MIS_PEDIDOS, "Pedidos", Icons.Filled.Menu),
    TARIFAS(Rutas.TARIFAS, "Tarifas", Icons.Filled.LocalOffer),
    PERFIL(Rutas.PERFIL, "Perfil", Icons.Filled.Person)
}

/** NavigationBar del estudiante: Inicio, Pedidos, Tarifas y Perfil. */
@Composable
fun BarraNavEstudiante(rutaActual: String?, onIr: (String) -> Unit) {
    val primario = MaterialTheme.colorScheme.primary
    val apagado = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)

    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        DestinoEstudiante.entries.forEach { d ->
            NavigationBarItem(
                selected = rutaActual == d.ruta,
                onClick = { onIr(d.ruta) },
                icon = { Icon(d.icono, contentDescription = d.etiqueta) },
                label = { Text(d.etiqueta) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = primario,
                    selectedTextColor = primario,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = apagado,
                    unselectedTextColor = apagado
                )
            )
        }
    }
}