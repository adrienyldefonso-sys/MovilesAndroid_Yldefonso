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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmarCitaScreen(navController: NavController) {
    val context = LocalContext.current
    val especialidad = Repositorio.especialidadSeleccionada
    val medico = Repositorio.medicoSeleccionado
    val fecha = Repositorio.fechaSeleccionada
    val hora = Repositorio.horaSeleccionada

    Scaffold(
        topBar = { TopAppBar(title = { Text("Resumen de Cita") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            Text(text = "Confirmación de Reserva", fontSize = 22.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(24.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Paciente: ${Repositorio.usuarioActual?.nombre ?: "Sin Registro"}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Especialidad: ${especialidad?.nombre}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Médico: ${medico?.nombre}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Fecha: $fecha")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Hora: $hora")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val citaGuardada = Repositorio.agendarCitaActual()
                    if (citaGuardada != null) {
                        navController.navigate(Rutas.CitaExitosa.ruta) {
                            popUpTo(Rutas.Home.ruta)
                        }
                    } else {
                        Toast.makeText(context, "Error al confirmar cita", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar Reserva")
            }
        }
    }
}