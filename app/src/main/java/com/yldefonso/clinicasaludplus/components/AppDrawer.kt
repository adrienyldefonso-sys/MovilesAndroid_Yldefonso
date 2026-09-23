package com.yldefonso.clinicasaludplus.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.yldefonso.clinicasaludplus.navigation.Screen
data class DrawerItem(val label: String, val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private val drawerItems = listOf(
    DrawerItem("Inicio", Screen.Home.route, Icons.Default.Home),
    DrawerItem("Mis citas", Screen.MisCitas.route, Icons.Default.Event),
    DrawerItem("Historial médico", Screen.HistorialMedico.route, Icons.Default.History)
)
// Contenido del menú lateral. Se declara envolviendo el Scaffold (en
// AppNavigation) y no como parámetro del Scaffold,ya que este estara permanentemente en la app
@Composable
fun AppDrawer(
    currentRoute: String?,
    onDestinationClick: (String) -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet {
        Column(Modifier.padding(16.dp)) {
            // Encabezado con el usuario
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) { Text("JP") }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Juan Pérez", style = MaterialTheme.typography.titleSmall)
                    Text("Paciente", style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))

            drawerItems.forEach { item ->
                NavigationDrawerItem(
                    label = { Text(item.label) },
                    icon = { Icon(item.icon, contentDescription = item.label) },
                    selected = currentRoute == item.route,
                    onClick = {
                        onDestinationClick(item.route)
                        onCloseDrawer()
                    }
                )
            }
        }
    }
}