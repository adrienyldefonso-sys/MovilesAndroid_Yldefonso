package com.yldefonso.clinicasaludplus.ui.components.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminosScreen(
    onVolver: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Términos y Condiciones") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Términos y Condiciones de Uso",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Bienvenido a SaludPlus. Al utilizar nuestra aplicación para el agendamiento de citas médicas, aceptas cumplir con las siguientes condiciones:\n\n" +
                        "1. Uso de Datos Personales: Sus datos personales y de contacto se emplean exclusivamente para procesar las citas médicas solicitadas.\n\n" +
                        "2. Cancelación de Citas: Podrá cancelar sus citas registradas en la sección 'Mis Citas' con anticipación.\n\n" +
                        "3. Responsabilidad: Asegúrese de ingresar información verídica para el correcto registro de su historial en la clínica.",
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}