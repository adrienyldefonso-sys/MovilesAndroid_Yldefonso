package com.yldefonso.clinicasaludplus.ui.components.agendamiento

import android.widget.Toast
import androidx.compose.foundation.layout.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FechaHoraScreen(navController: NavController) {
    val context = LocalContext.current
    var fecha by remember { mutableStateOf("15/10/2026") }
    var horaSeleccionada by remember { mutableStateOf("") }
    val horariosDisponibles = Repositorio.medicoSeleccionado?.disponibilidad ?: emptyList()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Fecha y Hora") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(text = "Seleccione Fecha:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha (DD/MM/AAAA)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Horarios Disponibles:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            horariosDisponibles.forEach { hora ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    RadioButton(
                        selected = (horaSeleccionada == hora),
                        onClick = { horaSeleccionada = hora }
                    )
                    Text(
                        text = hora,
                        modifier = Modifier.padding(start = 8.dp),
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (fecha.isBlank() || horaSeleccionada.isBlank()) {
                        Toast.makeText(context, "Seleccione fecha y hora", Toast.LENGTH_SHORT).show()
                    } else {
                        Repositorio.fechaSeleccionada = fecha
                        Repositorio.horaSeleccionada = horaSeleccionada
                        navController.navigate(Rutas.ConfirmarCita.ruta)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continuar a Confirmación")
            }
        }
    }
}