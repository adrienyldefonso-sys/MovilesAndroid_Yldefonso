package com.yldefonso.clinicasaludplus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminosScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Términos y Condiciones") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Términos y Condiciones de Uso - Clínica Salud Plus",
                fontSize = 20.sp,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "1. Aceptación de los Términos\n" +
                        "Al registrarse y utilizar la aplicación Clínica Salud Plus, el usuario acepta cumplir con todos los términos y políticas de privacidad establecidas.\n\n" +
                        "2. Uso de Datos Personales\n" +
                        "Sus datos de registro (nombre y correo electrónico) se utilizarán exclusivamente para la autenticación y gestión de servicios médicos dentro del aplicativo.\n\n" +
                        "3. Responsabilidad del Usuario\n" +
                        "El usuario es responsable de mantener la confidencialidad de sus credenciales de acceso.\n\n" +
                        "4. Modificaciones del Servicio\n" +
                        "La clínica se reserva el derecho de actualizar la aplicación para mejorar la experiencia del usuario.",
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Entendido / Volver")
            }
        }
    }
}