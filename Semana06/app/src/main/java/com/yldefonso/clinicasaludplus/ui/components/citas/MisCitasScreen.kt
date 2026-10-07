package com.yldefonso.clinicasaludplus.ui.components.citas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yldefonso.clinicasaludplus.repository.Repositorio

@Composable
fun MisCitasScreen(navController: NavController) {
    val citas = Repositorio.citasReservadas

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mis Citas Agendadas",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (citas.isEmpty()) {
            Text(
                text = "No tienes citas agendadas en este momento.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(citas) { cita ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = cita.codigoReserva,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Especialidad: ${cita.especialidad.nombre}")
                            Text(text = "Médico: ${cita.medico.nombre}")
                            Text(text = "Fecha y Hora: ${cita.fecha} - ${cita.hora}")
                        }
                    }
                }
            }
        }
    }
}