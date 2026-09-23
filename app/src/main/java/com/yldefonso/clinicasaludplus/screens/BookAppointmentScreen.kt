package com.yldefonso.clinicasaludplus.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yldefonso.clinicasaludplus.model.Cita
import com.yldefonso.clinicasaludplus.model.Medico
import com.yldefonso.clinicasaludplus.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookAppointmentScreen(
    navController: NavController,
    medico: Medico,
    citas: MutableList<Cita>   //estado global recibido por parámetro
) {
    val fechas = listOf("Jue 26", "Vie 27", "Sáb 28")   // mínimo 3 opciones
    val horas = listOf("9:00", "10:30", "3:00")         // mínimo 3 opciones

    // Selección única de fecha y hora — cada una es como un "grupo de RadioButton ademas es
    // implementado visualmente con FilterChip.
    var fechaSeleccionada by remember { mutableStateOf(fechas[1]) }
    var horaSeleccionada by remember { mutableStateOf(horas[1]) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar cita") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {

            Text("Selecciona fecha", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(fechas) { fecha ->
                    FilterChip(
                        selected = fecha == fechaSeleccionada,
                        onClick = { fechaSeleccionada = fecha },   // solo un valor activo a la vez
                        label = { Text(fecha) }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Text("Selecciona hora", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(horas) { hora ->
                    FilterChip(
                        selected = hora == horaSeleccionada,
                        onClick = { horaSeleccionada = hora },
                        label = { Text(hora) }
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    // Crea la cita y la agrega al estado global ANTES de navegar
                    val nuevaCita = Cita(
                        id = citas.size + 1,
                        medicoId = medico.id,
                        medicoNombre = medico.nombre,
                        especialidad = medico.especialidad,
                        fecha = fechaSeleccionada,
                        hora = horaSeleccionada,
                        estado = "Confirmada"
                    )
                    citas.add(nuevaCita)
                    // Ahora sí navega, pasando el id de la cita recién creada
                    navController.navigate(Screen.Confirmation.createRoute(nuevaCita.id))
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Confirmar cita") }
        }
    }
}