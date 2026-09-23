package com.yldefonso.clinicasaludplus.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yldefonso.clinicasaludplus.data.MedicosRepository
import com.yldefonso.clinicasaludplus.navigation.Screen
import com.yldefonso.clinicasaludplus.ui.theme.PurpleLight
import com.yldefonso.clinicasaludplus.ui.theme.PurpleMid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    // Ya NO existe "Todos": solo las 3 especialidades reales
    val especialidades = listOf("Cardiología", "Pediatría", "Dermatología")
    var especialidadSeleccionada by remember { mutableStateOf(especialidades.first()) }

    val medicosFiltrados = MedicosRepository.medicos.filter {
        it.especialidad == especialidadSeleccionada
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Clínica Salud+")
                        Text("Hola, Juan", style = MaterialTheme.typography.labelSmall)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PurpleMid,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            LazyRow(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(especialidades) { especialidad ->
                    FilterChip(
                        selected = especialidad == especialidadSeleccionada,
                        onClick = { especialidadSeleccionada = especialidad },
                        label = { Text(especialidad) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PurpleMid,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Text(
                "Médicos disponibles",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(medicosFiltrados) { medico ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(Screen.DoctorProfile.createRoute(medico.id))
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Ícono cruz médica sobre círculo morado suave
                            Box(
                                modifier = Modifier.size(40.dp).clip(CircleShape).background(PurpleLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = PurpleMid)
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(medico.nombre, style = MaterialTheme.typography.titleSmall)
                                Text(medico.especialidad, style = MaterialTheme.typography.bodySmall)
                            }
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107))
                            Text(" ${medico.calificacion}")
                        }
                    }
                }
            }
        }
    }
}