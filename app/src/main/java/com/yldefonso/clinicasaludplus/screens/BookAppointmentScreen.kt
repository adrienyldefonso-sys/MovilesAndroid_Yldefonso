package com.yldefonso.clinicasaludplus.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yldefonso.clinicasaludplus.model.Cita
import com.yldefonso.clinicasaludplus.model.Medico
import com.yldefonso.clinicasaludplus.navigation.Screen
import com.yldefonso.clinicasaludplus.ui.theme.ChipUnselected
import com.yldefonso.clinicasaludplus.ui.theme.PurpleMid

// Representa cada opción de fecha como par (día abreviado, número)
private data class FechaOption(val dia: String, val numero: String) {
    val display: String get() = "$dia $numero"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookAppointmentScreen(
    navController: NavController,
    medico: Medico,
    citas: MutableList<Cita>
) {
    val fechas = listOf(
        FechaOption("Jue", "26"),
        FechaOption("Vie", "27"),
        FechaOption("Sáb", "28")
    )
    val horas = listOf("9:00", "10:30", "3:00")

    var fechaSeleccionada by remember { mutableStateOf(fechas[1]) }
    var horaSeleccionada by remember { mutableStateOf(horas[1]) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar cita") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {

            Text("Selecciona fecha", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(fechas) { fecha ->
                    FechaChip(
                        dia = fecha.dia,
                        numero = fecha.numero,
                        selected = fecha == fechaSeleccionada,
                        onClick = { fechaSeleccionada = fecha }
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            Text("Selecciona hora", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(horas) { hora ->
                    HoraChip(
                        texto = hora,
                        selected = hora == horaSeleccionada,
                        onClick = { horaSeleccionada = hora }
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    val nuevaCita = Cita(
                        id = citas.size + 1,
                        medicoId = medico.id,
                        medicoNombre = medico.nombre,
                        especialidad = medico.especialidad,
                        fecha = fechaSeleccionada.display,
                        hora = horaSeleccionada,
                        estado = "Confirmada"
                    )
                    citas.add(nuevaCita)
                    navController.navigate(Screen.Confirmation.createRoute(nuevaCita.id))
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = PurpleMid)
            ) { Text("Confirmar cita") }
        }
    }
}

// Chip cuadrado: día arriba (pequeño) + número abajo (bold) — como el mockup
@Composable
private fun FechaChip(dia: String, numero: String, selected: Boolean, onClick: () -> Unit) {
    val bg = if (selected) PurpleMid else ChipUnselected
    val textColor = if (selected) Color.White else Color(0xFF444444)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(64.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .clickable { onClick() }
            .padding(vertical = 10.dp)
    ) {
        Text(
            dia,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) Color.White.copy(alpha = 0.8f) else Color.Gray
        )
        Spacer(Modifier.height(2.dp))
        Text(numero, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = textColor)
    }
}

// Chip rectangular para la hora — mismo patrón visual (filled si está seleccionado)
@Composable
private fun HoraChip(texto: String, selected: Boolean, onClick: () -> Unit) {
    val bg = if (selected) PurpleMid else ChipUnselected
    val textColor = if (selected) Color.White else Color(0xFF444444)
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 10.dp)
    ) {
        Text(texto, color = textColor, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
    }
}