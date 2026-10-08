package com.yldefonso.clinicasaludplus.ui.components.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yldefonso.clinicasaludplus.navigation.Rutas
import com.yldefonso.clinicasaludplus.repository.Repositorio

@Composable
fun HomeScreen(navController: NavController) {
    val usuario = Repositorio.usuarioActual
    val destacadas = Repositorio.especialidadesDestacadas()
    val ultimaCita = Repositorio.citasReservadas.lastOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Bienvenido, ${usuario?.nombre ?: "Paciente"}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // === CUADRO AZUL DESTACADO DE PRÓXIMA CITA / AGENDAMIENTO ===
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (ultimaCita != null) {
                        navController.navigate(Rutas.MisCitas.ruta)
                    } else {
                        navController.navigate(Rutas.Especialidades.ruta)
                    }
                },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    if (ultimaCita != null) {
                        Text(
                            text = "¡Próxima Cita Agendada!",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${ultimaCita.especialidad.nombre} — ${ultimaCita.medico.nombre}",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${ultimaCita.fecha} a las ${ultimaCita.hora}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    } else {
                        Text(
                            text = "Reserva tu Cita Médica",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Encuentra especialistas disponibles y agenda en pocos pasos.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Ver Citas",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Especialidades Destacadas",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(destacadas) { esp ->
                Card(
                    modifier = Modifier
                        .width(160.dp)
                        .clickable {
                            Repositorio.especialidadSeleccionada = esp
                            navController.navigate(Rutas.Medicos.crearRuta(esp.id))
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = esp.nombre,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = esp.descripcion,
                            fontSize = 12.sp,
                            maxLines = 2
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { navController.navigate(Rutas.Especialidades.ruta) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver Todas las Especialidades")
        }
    }
}