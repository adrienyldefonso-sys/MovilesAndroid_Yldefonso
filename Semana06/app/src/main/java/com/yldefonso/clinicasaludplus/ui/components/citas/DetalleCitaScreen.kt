package com.yldefonso.clinicasaludplus.ui.components.citas

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yldefonso.clinicasaludplus.repository.Repositorio

@Composable
fun DetalleCitaScreen(navController: NavController) {
    val context = LocalContext.current
    val cita = Repositorio.citasReservadas.lastOrNull()
    var mostrarDialogo by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = "Detalle de Cita",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (cita == null) {
            Text("No se encontró la información de la cita.")
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Código: ${cita.codigoReserva}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Paciente: ${cita.usuario.nombre}")
                    Text(text = "Especialidad: ${cita.especialidad.nombre}")
                    Text(text = "Médico: ${cita.medico.nombre} (${cita.medico.cmp})")
                    Text(text = "Fecha: ${cita.fecha}")
                    Text(text = "Hora: ${cita.hora}")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { mostrarDialogo = true },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar Cita")
            }
        }
    }

    // Requisito de Rúbrica: AlertDialog con remove
    if (mostrarDialogo && cita != null) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Cancelar Cita") },
            text = { Text("¿Está seguro de que desea cancelar esta cita médica?") },
            confirmButton = {
                TextButton(onClick = {
                    Repositorio.cancelarCita(cita.id)
                    mostrarDialogo = false
                    Toast.makeText(context, "Cita cancelada correctamente", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }) {
                    Text("Sí, Cancelar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("No")
                }
            }
        )
    }
}