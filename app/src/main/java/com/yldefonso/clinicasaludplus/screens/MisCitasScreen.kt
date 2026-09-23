package com.yldefonso.clinicasaludplus.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yldefonso.clinicasaludplus.model.Cita
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisCitasScreen(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    citas: List<Cita>   //estado de lectura
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis citas") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                    }
                }
            )
        }
    ) { padding ->
        if (citas.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("Aún no tienes citas agendadas")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(citas) { cita ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
                            Text(cita.medicoNombre, style = MaterialTheme.typography.titleSmall)
                            Text("${cita.fecha}, ${cita.hora}", style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(6.dp))
                            // Estado diferenciado visualmente:verde es confirmada y gris es completada
                            AssistChip(
                                onClick = {},
                                label = { Text(cita.estado) },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = if (cita.estado == "Confirmada")
                                        Color(0xFFDFF5E1) else Color(0xFFE0E0E0),
                                    labelColor = if (cita.estado == "Confirmada")
                                        Color(0xFF2E7D32) else Color(0xFF616161)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}