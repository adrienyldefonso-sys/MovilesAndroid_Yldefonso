package com.yldefonso.clinicasaludplus.ui.components.citas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yldefonso.clinicasaludplus.repository.Repositorio

@Composable
fun DetalleCitaScreen(navController: NavController) {
    val cita = Repositorio.citasReservadas.lastOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Detalle de la Cita",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (cita == null) {
            Text(
                text = "No hay ninguna cita seleccionada o registrada.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline
            )
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Código: ${cita.codigoReserva}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Paciente: ${cita.usuario.nombre}")
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Especialidad: ${cita.especialidad.nombre}")
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Médico: ${cita.medico.nombre} (${cita.medico.cmp})")
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Fecha: ${cita.fecha}")
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Hora: ${cita.hora}")
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}