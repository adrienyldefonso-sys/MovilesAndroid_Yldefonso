package com.yldefonso.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tecsupfit.ui.theme.TealPrimary
import com.yldefonso.tecsupfit.model.Reserva
import com.yldefonso.tecsupfit.navigation.Screen
@Composable
fun ConfirmationScreen(navController: NavController, reserva: Reserva?) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            tint = TealPrimary,
            modifier = Modifier.size(72.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text("¡Cupo reservado!", style = MaterialTheme.typography.headlineSmall)

        reserva?.let {
            Spacer(Modifier.height(8.dp))
            Text(it.claseNombre, style = MaterialTheme.typography.titleMedium)
            Text("Hoy, ${it.horario} · ${it.sala}", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.height(24.dp))
        Button(onClick = {
            // Limpia el flujo de reserva y deja el back stack en Home + reservas
            navController.navigate(Screen.Reservas.route) {
                popUpTo(Screen.Home.route)
            }
        }) { Text("Ver mis reservas") }
    }
}