//Menu lateral desplegable de la apliacacion para acceder a las diferentes funciones que designemos
package com.yldefonso.clinicasaludplus.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yldefonso.clinicasaludplus.model.Cita
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//Muestra únicamente las citas ya completadas,es decir el historial de atenciones
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialMedicoScreen(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    citas: List<Cita>
) {
    val completadas = citas.filter { it.estado == "Completada" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial médico") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                    }
                }
            )
        }
    ) { padding ->
        if (completadas.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("Todavía no tienes atenciones registradas")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
                items(completadas) { cita ->
                    ListItem(
                        headlineContent = { Text(cita.medicoNombre) },
                        supportingContent = { Text("${cita.especialidad} · ${cita.fecha}") }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

