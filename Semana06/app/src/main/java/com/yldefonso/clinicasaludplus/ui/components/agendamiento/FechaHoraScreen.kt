package com.yldefonso.clinicasaludplus.ui.components.agendamiento

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yldefonso.clinicasaludplus.navigation.Rutas
import com.yldefonso.clinicasaludplus.repository.Repositorio

@Composable
fun FechaHoraScreen(navController: NavController) {
    val context = LocalContext.current
    val medico = Repositorio.medicoSeleccionado

    var fechaText by remember { mutableStateOf("2026-10-15") }
    var horaSeleccionadaLocal by remember { mutableStateOf(Repositorio.horaSeleccionada) }

    val horarios = medico?.disponibilidad ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Seleccionar Fecha y Hora",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Médico: ${medico?.nombre ?: "No seleccionado"}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = fechaText,
            onValueChange = { fechaText = it },
            label = { Text("Fecha de la cita (AAAA-MM-DD)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Horarios Disponibles",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (horarios.isEmpty()) {
            Text("No hay horarios disponibles para este médico.")
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(horarios) { hora ->
                    val esSeleccionado = hora == horaSeleccionadaLocal
                    Button(
                        onClick = {
                            horaSeleccionadaLocal = hora
                            Repositorio.horaSeleccionada = hora
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (esSeleccionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (esSeleccionado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Text(text = hora, fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (fechaText.isBlank() || horaSeleccionadaLocal.isBlank()) {
                    Toast.makeText(context, "Por favor seleccione fecha y hora", Toast.LENGTH_SHORT).show()
                } else {
                    Repositorio.fechaSeleccionada = fechaText
                    navController.navigate(Rutas.ConfirmarCita.ruta)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continuar a Confirmación")
        }
    }
}