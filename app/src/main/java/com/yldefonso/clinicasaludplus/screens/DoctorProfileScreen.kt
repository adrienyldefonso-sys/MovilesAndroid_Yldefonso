package com.yldefonso.clinicasaludplus.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yldefonso.clinicasaludplus.model.Medico
import com.yldefonso.clinicasaludplus.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorProfileScreen(navController: NavController, medico: Medico) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil del médico") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.MedicalServices,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text(medico.nombre, style = MaterialTheme.typography.headlineSmall)
            Text(
                "${medico.especialidad} · ${medico.experienciaAnios} años exp.",
                style = MaterialTheme.typography.bodyMedium
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107))
                Text(" ${medico.calificacion} (${medico.resenas} reseñas)")
            }
            Spacer(Modifier.height(16.dp))
            Text(medico.descripcion, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    // Sigue pasando solo el doctorId — parte 2 del flujo secuencial
                    navController.navigate(Screen.BookAppointment.createRoute(medico.id))
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Agendar cita") }
        }
    }
}