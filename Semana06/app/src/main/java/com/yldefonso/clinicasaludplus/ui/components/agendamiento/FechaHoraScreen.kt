package com.yldefonso.clinicasaludplus.ui.components.agendamiento

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yldefonso.clinicasaludplus.navigation.Rutas
import com.yldefonso.clinicasaludplus.repository.Repositorio
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@Composable
fun FechaHoraScreen(navController: NavController) {
    val medico = Repositorio.medicoSeleccionado

    var semanaOffset by remember { mutableIntStateOf(0) }
    val hoy = remember { LocalDate.now() }

    // Lunes de la semana seleccionada
    val lunesSemana = remember(semanaOffset) {
        hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).plusWeeks(semanaOffset.toLong())
    }

    // 5 días hábiles (Lunes a Viernes)
    val diasHabiles = remember(lunesSemana) {
        (0L..4L).map { lunesSemana.plusDays(it) }
    }

    // Día seleccionado por defecto: primer día no pasado de la semana visible, o el lunes
    var diaSeleccionado by remember(lunesSemana) {
        mutableStateOf(diasHabiles.firstOrNull { !it.isBefore(hoy) } ?: diasHabiles.first())
    }

    var horaSeleccionadaLocal by remember { mutableStateOf(Repositorio.horaSeleccionada) }

    val localeEs = remember { Locale.Builder().setLanguage("es").setRegion("ES").build() }
    val formatoIso = remember { DateTimeFormatter.ISO_LOCAL_DATE }
    val formatoTexto = remember { DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", localeEs) }

    val fechaIsoActual = diaSeleccionado.format(formatoIso)

    LaunchedEffect(diaSeleccionado) {
        val textoLimpio = diaSeleccionado.format(formatoTexto).replaceFirstChar { it.uppercase() }
        Repositorio.fechaSeleccionadaIso = fechaIsoActual
        Repositorio.fechaSeleccionadaTexto = textoLimpio
        Repositorio.fechaSeleccionada = textoLimpio
    }

    // Obtener horarios disponibles de manera reactiva para la fecha elegida
    val horarios = remember(medico?.id, fechaIsoActual, Repositorio.citasReservadas.size) {
        if (medico != null) {
            Repositorio.horariosDisponibles(medico.id, fechaIsoActual)
        } else emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Cabecera con botón de retroceso
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver"
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Seleccionar Fecha y Hora",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tarjeta del médico seleccionado
        medico?.let { med ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = med.fotoUrl,
                        contentDescription = "Foto de ${med.nombre}",
                        placeholder = rememberVectorPainter(Icons.Default.Person),
                        error = rememberVectorPainter(Icons.Default.Person),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = med.nombre,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        val especialidadNombre = Repositorio.especialidades.find { it.id == med.especialidadId }?.nombre ?: ""
                        if (especialidadNombre.isNotEmpty()) {
                            Text(
                                text = especialidadNombre,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = med.cmp,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Navegación por semanas (Mes y Año en español)
        val mesAnoTexto = remember(lunesSemana) {
            lunesSemana.format(DateTimeFormatter.ofPattern("MMMM yyyy", localeEs)).replaceFirstChar { it.uppercase() }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = mesAnoTexto,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Row {
                IconButton(
                    onClick = { if (semanaOffset > 0) semanaOffset-- },
                    enabled = semanaOffset > 0
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Semana anterior"
                    )
                }
                IconButton(
                    onClick = { semanaOffset++ }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Siguiente semana"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Días hábiles de la semana (Lunes a Viernes)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(diasHabiles.size) { index ->
                val dia = diasHabiles[index]
                val esPasado = dia.isBefore(hoy)
                val esSeleccionado = dia == diaSeleccionado

                val nombreDiaCorto = dia.format(DateTimeFormatter.ofPattern("EEE", localeEs))
                    .take(3)
                    .replaceFirstChar { it.uppercase() }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            esSeleccionado -> MaterialTheme.colorScheme.primary
                            esPasado -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        contentColor = when {
                            esSeleccionado -> MaterialTheme.colorScheme.onPrimary
                            esPasado -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    ),
                    modifier = Modifier
                        .width(64.dp)
                        .height(72.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(enabled = !esPasado) {
                            diaSeleccionado = dia
                            horaSeleccionadaLocal = ""
                            Repositorio.horaSeleccionada = ""
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = nombreDiaCorto,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = dia.dayOfMonth.toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Horarios Disponibles",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (horarios.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay horarios disponibles para la fecha seleccionada.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(horarios.size) { index ->
                    val hora = horarios[index]
                    val esSeleccionado = hora == horaSeleccionadaLocal
                    Button(
                        onClick = {
                            horaSeleccionadaLocal = hora
                            Repositorio.horaSeleccionada = hora
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (esSeleccionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (esSeleccionado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        contentPadding = PaddingValues(vertical = 10.dp, horizontal = 4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = hora,
                            fontSize = 13.sp,
                            fontWeight = if (esSeleccionado) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (horaSeleccionadaLocal.isNotBlank()) {
                    navController.navigate(Rutas.ConfirmarCita.ruta)
                }
            },
            enabled = horaSeleccionadaLocal.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Continuar a Confirmación",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}