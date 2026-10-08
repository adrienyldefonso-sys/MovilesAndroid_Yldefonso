package com.yldefonso.clinicasaludplus.ui.components.citas

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.EventBusy
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yldefonso.clinicasaludplus.repository.Repositorio

// Paleta de colores unificada
private val AzulPrimario = Color(0xFF2F6BEA)
private val TextoOscuro = Color(0xFF1B2540)
private val TextoGris = Color(0xFF6B7690)
private val FondoPantalla = Color(0xFFF8FAFC)
private val BordeCard = Color(0xFFE2E8F0)
private val FondoIcono = Color(0xFFEAF1FF)
private val VerdeFondo = Color(0xFFF0FDF4)
private val VerdeTexto = Color(0xFF166534)
private val RojoRojo = Color(0xFFDC2626)
private val FondoRojoSuave = Color(0xFFFEF2F2)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleCitaScreen(navController: NavController) {
    val context = LocalContext.current
    val cita = Repositorio.citaSeleccionada ?: Repositorio.citasReservadas.lastOrNull()
    var mostrarDialogo by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = FondoPantalla,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Detalle de Cita",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = TextoOscuro
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = TextoOscuro
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = FondoPantalla
                )
            )
        },
        bottomBar = {
            if (cita != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Button(
                        onClick = { mostrarDialogo = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RojoRojo,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Cancelar Cita",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.3.sp
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            if (cita == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.EventBusy,
                            contentDescription = "No encontrado",
                            tint = TextoGris,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No se encontró la información de la cita.",
                            fontSize = 15.sp,
                            color = TextoGris,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                // 1. TARJETA PRINCIPAL DEL RESUMEN DE LA CITA
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BordeCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {

                        // CABECERA CON CÓDIGO Y ESTADO
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Reserva",
                                    fontSize = 12.sp,
                                    color = TextoGris
                                )
                                Text(
                                    text = cita.codigoReserva,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AzulPrimario
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(VerdeFondo)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "Confirmada",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VerdeTexto
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))
                        HorizontalDivider(thickness = 1.dp, color = BordeCard)
                        Spacer(modifier = Modifier.height(18.dp))

                        // INFORMACIÓN DEL MÉDICO
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AsyncImage(
                                model = cita.medico.fotoUrl,
                                contentDescription = cita.medico.nombre,
                                placeholder = rememberVectorPainter(Icons.Default.Person),
                                error = rememberVectorPainter(Icons.Default.Person),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(FondoIcono)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = cita.medico.nombre,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = TextoOscuro
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = cita.especialidad.nombre,
                                    fontSize = 14.sp,
                                    color = AzulPrimario,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = cita.medico.cmp,
                                    fontSize = 12.sp,
                                    color = TextoGris
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 2. DETALLES DE ATENCIÓN (PACIENTE, FECHA, HORA)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BordeCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Información de la atención",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoOscuro
                        )

                        ItemDetalleFila(
                            icono = Icons.Default.Person,
                            etiqueta = "Paciente",
                            valor = cita.usuario.nombre
                        )

                        ItemDetalleFila(
                            icono = Icons.Default.MedicalServices,
                            etiqueta = "Especialidad",
                            valor = cita.especialidad.nombre
                        )

                        ItemDetalleFila(
                            icono = Icons.Default.CalendarToday,
                            etiqueta = "Fecha de atención",
                            valor = cita.fecha
                        )

                        ItemDetalleFila(
                            icono = Icons.Default.AccessTime,
                            etiqueta = "Hora agendada",
                            valor = cita.hora
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // DIÁLOGO DE CONFIRMACIÓN DE CANCELACIÓN DE CITA
    if (mostrarDialogo && cita != null) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            icon = {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(FondoRojoSuave),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "Cancelar",
                        tint = RojoRojo,
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "¿Cancelar esta cita?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    color = TextoOscuro,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = "Esta acción liberará el horario agendado para el ${cita.fecha} a las ${cita.hora}. ¿Deseas continuar?",
                    fontSize = 14.sp,
                    color = TextoGris,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        Repositorio.cancelarCita(cita.id)
                        mostrarDialogo = false
                        Toast.makeText(context, "Cita cancelada correctamente", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RojoRojo),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Sí, cancelar cita",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { mostrarDialogo = false },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, BordeCard),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "No, mantener cita",
                        fontWeight = FontWeight.SemiBold,
                        color = TextoOscuro
                    )
                }
            }
        )
    }
}

@Composable
private fun ItemDetalleFila(
    icono: ImageVector,
    etiqueta: String,
    valor: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(FondoIcono),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = etiqueta,
                tint = AzulPrimario,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = etiqueta,
                fontSize = 12.sp,
                color = TextoGris
            )
            Text(
                text = valor,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextoOscuro
            )
        }
    }
}