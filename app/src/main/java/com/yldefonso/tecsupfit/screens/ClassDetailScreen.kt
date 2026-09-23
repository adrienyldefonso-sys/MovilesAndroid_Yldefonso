package com.yldefonso.tecsupfit.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tecsupfit.ui.theme.TealLight
import com.example.tecsupfit.ui.theme.TealPrimary
import com.yldefonso.tecsupfit.model.Clase
import com.yldefonso.tecsupfit.model.Reserva
import com.yldefonso.tecsupfit.navigation.Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassDetailScreen(
    navController: NavController,
    clase: Clase,
    reservas: MutableList<Reserva>   // estado global recibido por parámetro
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de clase") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {

            // Banner superior con ícono de la clase
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(TealLight, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.FitnessCenter,
                    contentDescription = null,
                    tint = TealPrimary,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(Modifier.height(16.dp))
            Text(clase.nombre, style = MaterialTheme.typography.headlineSmall)
            Text(
                "${clase.horario} · ${clase.sala} · ${clase.duracionMin} min",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(12.dp))
            Text(clase.descripcion, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            Text(
                "${clase.cuposDisponibles} de ${clase.cuposTotales} cupos disponibles",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    // Crea la reserva y la agrega al estado global ANTES de navegar
                    val nuevaReserva = Reserva(
                        id = (reservas.maxOfOrNull { it.id } ?: 0) + 1,
                        claseId = clase.id,
                        claseNombre = clase.nombre,
                        horario = clase.horario,
                        sala = clase.sala,
                        estado = "Confirmada"
                    )
                    reservas.add(nuevaReserva)
                    navController.navigate(Screen.Confirmation.createRoute(nuevaReserva.id))
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
            ) { Text("Reservar cupo") }
        }
    }
}