package com.yldefonso.tecsupfit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tecsupfit.ui.theme.TealLight
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
        // Círculo de fondo verde clarito con el check en verde (no relleno sólido)
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(TealLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Check,
                contentDescription = null,
                tint = TealPrimary,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(Modifier.height(16.dp))
        Text("¡Cupo reservado!", style = MaterialTheme.typography.headlineSmall)

        reserva?.let {
            Spacer(Modifier.height(8.dp))
            Text(it.claseNombre, style = MaterialTheme.typography.titleMedium)
            Text("Hoy, ${it.horario} · ${it.sala}", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.height(24.dp))

        // Botón gris claro/sutil con texto oscuro — no el botón primario sólido
        Button(
            onClick = {
                navController.navigate(Screen.Reservas.route) {
                    popUpTo(Screen.Home.route)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF2F2F2),
                contentColor = Color(0xFF333333)
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
        ) { Text("Ver mis reservas") }
    }
}