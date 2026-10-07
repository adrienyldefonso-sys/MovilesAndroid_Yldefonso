package com.yldefonso.clinicasaludplus.ui.components.notificaciones

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class Notificacion(
    val id: Int,
    val titulo: String,
    val mensaje: String,
    val fecha: String
)

@Composable
fun NotificacionesScreen(navController: NavController) {
    val listaNotificaciones = listOf(
        Notificacion(1, "Recordatorio de Cita", "Tienes una cita médica agendada para mañana.", "Hace 2 horas"),
        Notificacion(2, "Resultados Disponibles", "Tus exámenes de laboratorio ya están listos.", "Ayer"),
        Notificacion(3, "Bienvenido", "Gracias por registrarte en Clínica Salud Plus.", "Hace 3 días")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Notificaciones",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(listaNotificaciones) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.titulo,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = item.fecha,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = item.mensaje, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}