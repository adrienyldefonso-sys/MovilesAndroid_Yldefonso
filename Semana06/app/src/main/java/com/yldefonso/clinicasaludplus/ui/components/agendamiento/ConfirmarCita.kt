package com.yldefonso.clinicasaludplus.ui.components.agendamiento

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yldefonso.clinicasaludplus.navigation.Rutas
import com.yldefonso.clinicasaludplus.repository.Repositorio

@Composable
fun ConfirmarCitaScreen(navController: NavController) {
    val context = LocalContext.current
    val usuario = Repositorio.usuarioActual
    val especialidad = Repositorio.especialidadSeleccionada
    val medico = Repositorio.medicoSeleccionado
    val fecha = Repositorio.fechaSeleccionada
    val hora = Repositorio.horaSeleccionada

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Resumen de la Cita",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Paciente: ${usuario?.nombre ?: "N/A"}", fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Especialidad: ${especialidad?.nombre ?: "N/A"}")
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Médico: ${medico?.nombre ?: "N/A"} (${medico?.cmp ?: ""})")
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Fecha: $fecha")
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Hora: $hora")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                val nuevaCita = Repositorio.agendarCitaActual()
                if (nuevaCita != null) {
                    navController.navigate(Rutas.CitaExitosa.ruta)
                } else {
                    Toast.makeText(context, "Error al procesar la reserva", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirmar Reserva")
        }
    }
}