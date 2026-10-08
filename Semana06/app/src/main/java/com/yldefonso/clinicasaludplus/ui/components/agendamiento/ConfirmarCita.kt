package com.yldefonso.clinicasaludplus.ui.components.agendamiento

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yldefonso.clinicasaludplus.navigation.Rutas
import com.yldefonso.clinicasaludplus.repository.Repositorio

private val CitaAzul = Color(0xFF2F6BEA)
private val CitaAzulClaro = Color(0xFFEAF1FF)
private val CitaFondoBloque = Color(0xFFF3F6FC)
private val CitaTexto = Color(0xFF1B2540)
private val CitaGris = Color(0xFF6B7690)
private val CitaBorde = Color(0xFFE1E7F3)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmarCitaScreen(navController: NavController) {
    val context = LocalContext.current
    val especialidad = Repositorio.especialidadSeleccionada
    val medico = Repositorio.medicoSeleccionado

    var fechaTexto = when {
        Repositorio.fechaSeleccionadaTexto.isNotBlank() -> Repositorio.fechaSeleccionadaTexto
        Repositorio.fechaSeleccionada.isNotBlank() -> Repositorio.fechaSeleccionada
        else -> Repositorio.fechaSeleccionadaIso
    }
    if (fechaTexto.isNotBlank() && !fechaTexto.contains("2026")) {
        fechaTexto = "$fechaTexto 2026"
    }

    val horaTexto = Repositorio.horaSeleccionada
    var motivoConsulta by remember { mutableStateOf("") }
    var mostrarDialogo by remember { mutableStateOf(false) }

    // ALERT DIALOG DE CONFIRMACIÓN AL AGENDAR CITA
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Text(
                    text = "¡Cita asignada con éxito!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = CitaTexto
                )
            },
            text = {
                Text(
                    text = "Se ha reservado tu cita para el $fechaTexto a las $horaTexto. Se ha generado una nueva notificación con el estado 'Pendiente'.",
                    fontSize = 14.sp,
                    color = CitaGris
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarDialogo = false
                        navController.navigate(Rutas.CitaExitosa.ruta)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CitaAzul)
                ) {
                    Text(
                        text = "Entendido",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Confirmar cita",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = CitaTexto
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = CitaTexto
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Button(
                    onClick = {
                        val nuevaCita = Repositorio.agendarCitaActual()
                        if (nuevaCita != null) {
                            mostrarDialogo = true
                        } else {
                            Toast.makeText(context, "Error al procesar la reserva", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CitaAzul,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Agendar cita",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            medico?.let { med ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CitaFondoBloque),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = med.fotoUrl,
                            contentDescription = "Foto de ${med.nombre}",
                            placeholder = rememberVectorPainter(Icons.Default.Person),
                            error = rememberVectorPainter(Icons.Default.Person),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(CitaAzulClaro)
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = med.nombre,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = CitaTexto
                            )
                            val espNombre = especialidad?.nombre
                                ?: Repositorio.especialidades.find { it.id == med.especialidadId }?.nombre
                                ?: ""
                            if (espNombre.isNotEmpty()) {
                                Text(
                                    text = espNombre,
                                    fontSize = 14.sp,
                                    color = CitaGris
                                )
                            }
                            Text(
                                text = med.cmp,
                                fontSize = 13.sp,
                                color = CitaGris
                            )
                        }
                    }
                }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                FilaDetalleAnimada(
                    icono = Icons.Default.CalendarToday,
                    etiqueta = "Fecha",
                    valor = if (fechaTexto.isNotBlank()) fechaTexto else "No seleccionada"
                )
                HorizontalDivider(thickness = 1.dp, color = CitaBorde)

                FilaDetalleAnimada(
                    icono = Icons.Default.AccessTime,
                    etiqueta = "Hora",
                    valor = if (horaTexto.isNotBlank()) horaTexto else "No seleccionada"
                )
                HorizontalDivider(thickness = 1.dp, color = CitaBorde)

                FilaDetalleAnimada(
                    icono = Icons.Default.MedicalServices,
                    etiqueta = "Tipo de atención",
                    valor = "Consulta presencial"
                )
                HorizontalDivider(thickness = 1.dp, color = CitaBorde)

                FilaDetalleAnimada(
                    icono = Icons.Default.LocationOn,
                    etiqueta = "Dirección",
                    valor = "Av. Los Olivos 123 - Piso 4"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, color = CitaTexto, fontSize = 14.sp)) {
                        append("Motivos de consulta ")
                    }
                    withStyle(SpanStyle(fontWeight = FontWeight.Normal, color = CitaGris, fontSize = 12.sp)) {
                        append("(opcional)")
                    }
                }
            )
            OutlinedTextField(
                value = motivoConsulta,
                onValueChange = { motivoConsulta = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Consulta de rutina", color = CitaGris, fontSize = 14.sp)
                },
                minLines = 3,
                maxLines = 4,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = CitaBorde,
                    focusedBorderColor = CitaAzul,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    cursorColor = CitaAzul
                )
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun FilaDetalleAnimada(
    icono: ImageVector,
    etiqueta: String,
    valor: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(CitaAzulClaro),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = etiqueta,
                tint = CitaAzul,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(
                text = etiqueta,
                fontSize = 12.sp,
                color = CitaGris
            )
            Text(
                text = valor,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = CitaTexto
            )
        }
    }
}