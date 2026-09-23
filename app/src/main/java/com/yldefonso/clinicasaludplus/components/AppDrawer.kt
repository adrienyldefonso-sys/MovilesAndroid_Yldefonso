package com.yldefonso.clinicasaludplus.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yldefonso.clinicasaludplus.navigation.Screen
import com.yldefonso.clinicasaludplus.ui.theme.PurpleLight
import com.yldefonso.clinicasaludplus.ui.theme.PurpleMid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppDrawer(
    navController: NavController,
    currentRoute: String?,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    ModalDrawerSheet {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(PurpleLight),
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

            fun navegarA(route: String) {
                navController.navigate(route) {
                    popUpTo(Screen.Home.route)
                    launchSingleTop = true
                }
                scope.launch { drawerState.close() }
            }

            DrawerRadioItem(
                label = "Inicio",
                selected = currentRoute == Screen.Home.route,
                onClick = { navegarA(Screen.Home.route) }
            )
            DrawerRadioItem(
                label = "Mis citas",
                selected = currentRoute == Screen.MisCitas.route,
                onClick = { navegarA(Screen.MisCitas.route) }
            )
            DrawerRadioItem(
                label = "Historial médico",
                selected = currentRoute == Screen.HistorialMedico.route,
                onClick = { navegarA(Screen.HistorialMedico.route) }
            )
            DrawerRadioItem(
                label = "Perfil",
                selected = currentRoute == Screen.Perfil.route,
                onClick = { navegarA(Screen.Perfil.route) }
            )
        }
    }
}
@Composable
private fun DrawerRadioItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(label) },
        icon = {
            Icon(
                imageVector = if (selected) Icons.Filled.RadioButtonChecked
                else Icons.Outlined.RadioButtonUnchecked,
                contentDescription = label,
                tint = if (selected) PurpleMid else Color(0xFF9E9E9E)
            )
        },
        selected = selected,
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = PurpleLight
        )
    )
}