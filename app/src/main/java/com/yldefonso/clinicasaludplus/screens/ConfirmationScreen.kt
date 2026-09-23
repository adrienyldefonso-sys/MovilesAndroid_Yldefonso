package com.yldefonso.clinicasaludplus.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yldefonso.clinicasaludplus.model.Cita
import com.yldefonso.clinicasaludplus.navigation.Screen

@Composable
fun ConfirmationScreen(navController: NavController, cita: Cita?) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(72.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text("¡Cita agendada!", style = MaterialTheme.typography.headlineSmall)

        // Resumen de la cita: médico, fecha y hora — tal como pide el requisito
        cita?.let {
            Spacer(Modifier.height(8.dp))
            Text(it.medicoNombre, style = MaterialTheme.typography.titleMedium)
            Text("${it.fecha}, ${it.hora}", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.height(24.dp))
        Button(onClick = {
            // Limpia el flujo de agendado (DoctorProfile/BookAppointment/Confirmation)
            // y deja el back stack en Home + MisCitas
            navController.navigate(Screen.MisCitas.route) {
                popUpTo(Screen.Home.route)
            }
        }) { Text("Ver mis citas") }
    }
}