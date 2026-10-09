package com.yldefonso.clinicasaludplus.ui.components.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

// Paleta de colores unificada
private val AzulPrimario = Color(0xFF2F6BEA)
private val AzulDeshabilitado = Color(0xFFBFD0F7)
private val TextoOscuro = Color(0xFF1B2540)
private val TextoGris = Color(0xFF6B7690)
private val FondoChip = Color(0xFFF1F4FA)
private val FondoBloque = Color(0xFFF3F6FC)
private val AzulPastelFondo = Color(0xFFEAF1FF)
private val GrisDeshabilitado = Color(0xFFCBD5E1)

@OptIn(ExperimentalMaterial3Api::class)
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

    // Seleccionar por defecto el primer día disponible de la semana (o el primero si ninguno está disponible)
    var diaSeleccionado by remember(lunesSemana, medico) {
        val primerDisponible = diasHabiles.firstOrNull { d ->
            medico != null && Repositorio.esDiaDisponible(medico.id, d)
        }
        mutableStateOf(primerDisponible ?: diasHabiles.first())
    }

    var horaSeleccionadaLocal by remember { mutableStateOf(Repositorio.horaSeleccionada) }

    val localeEs = remember { Locale.Builder().setLanguage("es").setRegion("ES").build() }
    val formatoIso = remember { DateTimeFormatter.ISO_LOCAL_DATE }
    val formatoTexto = remember { DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", localeEs) }

    val esDiaValido = remember(medico?.id, diaSeleccionado) {
        medico != null && Repositorio.esDiaDisponible(medico.id, diaSeleccionado)
    }

    val fechaIsoActual = diaSeleccionado.format(formatoIso)

    LaunchedEffect(diaSeleccionado) {
        val textoLimpio = diaSeleccionado.format(formatoTexto).replaceFirstChar { it.uppercase() }
        Repositorio.fechaSeleccionadaIso = fechaIsoActual
        Repositorio.fechaSeleccionadaTexto = textoLimpio
        Repositorio.fechaSeleccionada = textoLimpio
    }

    // Horarios disponibles desde el repositorio
    val horarios = remember(medico?.id, fechaIsoActual, esDiaValido, Repositorio.citasReservadas.size) {
        if (medico != null && esDiaValido) {
            Repositorio.horariosDisponibles(medico.id, fechaIsoActual)
        } else emptyList()
    }

    val mesAnoTexto = remember(lunesSemana) {
        lunesSemana.format(DateTimeFormatter.ofPattern("MMMM yyyy", localeEs)).replaceFirstChar { it.uppercase() }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Seleccionar fecha y hora",
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Button(
                    onClick = {
                        if (esDiaValido && horaSeleccionadaLocal.isNotBlank()) {
                            navController.navigate(Rutas.ConfirmarCita.ruta)
                        }
                    },
                    enabled = esDiaValido && horaSeleccionadaLocal.isNotBlank(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulPrimario,
                        contentColor = Color.White,
                        disabledContainerColor = AzulDeshabilitado,
                        disabledContentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Text(
                        text = "Continuar",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.3.sp
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 4.dp)
        ) {
            // 1. CARD DEL MÉDICO (CENTRADOS FOTO Y TEXTO)
            medico?.let { med ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .background(FondoBloque)
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = med.fotoUrl,
                        contentDescription = "Foto de ${med.nombre}",
                        placeholder = rememberVectorPainter(Icons.Default.Person),
                        error = rememberVectorPainter(Icons.Default.Person),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(84.dp)
                            .clip(CircleShape)
                            .background(AzulPastelFondo)
                    )
                    Spacer(modifier = Modifier.width(18.dp))
                    Column {
                        Text(
                            text = med.nombre,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = TextoOscuro
                        )
                        val especialidadNombre =
                            Repositorio.especialidades.find { it.id == med.especialidadId }?.nombre ?: ""
                        if (especialidadNombre.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = especialidadNombre,
                                fontSize = 16.sp,
                                color = TextoGris
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 2. BLOQUE DE CALENDARIO (NAVEGACIÓN POR MES Y DÍAS HÁBILES)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { if (semanaOffset > 0) semanaOffset-- },
                    enabled = semanaOffset > 0
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Semana anterior",
                        tint = if (semanaOffset > 0) TextoOscuro else TextoGris.copy(alpha = 0.4f)
                    )
                }
                Text(
                    text = mesAnoTexto,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoOscuro
                )
                IconButton(onClick = { semanaOffset++ }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Siguiente semana",
                        tint = TextoOscuro
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // LEYENDA VISUAL DE COLORES
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(AzulPastelFondo)
                            .border(1.5.dp, AzulPrimario, CircleShape)
                    )
                    Text(
                        text = "Disponible",
                        fontSize = 13.sp,
                        color = TextoGris,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(GrisDeshabilitado)
                    )
                    Text(
                        text = "No disponible",
                        fontSize = 13.sp,
                        color = TextoGris,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // CHIPS DE DÍAS (LUNES A VIERNES)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                diasHabiles.forEach { dia ->
                    val esDisponible = medico != null && Repositorio.esDiaDisponible(medico.id, dia)
                    val esSeleccionado = dia == diaSeleccionado && esDisponible

                    val nombreDiaCorto = dia.format(DateTimeFormatter.ofPattern("EEE", localeEs))
                        .take(3)
                        .replaceFirstChar { it.uppercase() }

                    val fondo = when {
                        esSeleccionado -> AzulPrimario
                        esDisponible -> AzulPastelFondo
                        else -> FondoChip.copy(alpha = 0.5f)
                    }
                    val colorTexto = when {
                        esSeleccionado -> Color.White
                        esDisponible -> TextoOscuro
                        else -> TextoGris.copy(alpha = 0.45f)
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .height(86.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(fondo)
                            .then(
                                if (!esSeleccionado && esDisponible) {
                                    Modifier.border(1.dp, AzulPrimario, RoundedCornerShape(16.dp))
                                } else Modifier
                            )
                            .clickable(enabled = esDisponible) {
                                diaSeleccionado = dia
                                horaSeleccionadaLocal = ""
                                Repositorio.horaSeleccionada = ""
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = nombreDiaCorto,
                            fontSize = 14.sp,
                            fontWeight = if (esSeleccionado) FontWeight.Bold else FontWeight.Medium,
                            color = colorTexto
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = dia.dayOfMonth.toString(),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorTexto
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 3. BLOQUE DE HORARIOS (3 COLUMNAS CENTRADAS EN FORMATO 24H)
            if (!esDiaValido) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "El médico no atiende en esta fecha. Selecciona un día disponible.",
                        fontSize = 15.sp,
                        color = TextoGris,
                        textAlign = TextAlign.Center
                    )
                }
            } else if (horarios.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay horarios disponibles para esta fecha.",
                        fontSize = 15.sp,
                        color = TextoGris,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    horarios.chunked(3).forEach { filaHoras ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            filaHoras.forEach { hora ->
                                val esSeleccionado = hora == horaSeleccionadaLocal
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(62.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(if (esSeleccionado) AzulPrimario else FondoChip)
                                        .clickable {
                                            horaSeleccionadaLocal = hora
                                            Repositorio.horaSeleccionada = hora
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = hora,
                                        fontSize = 16.sp,
                                        fontWeight = if (esSeleccionado) FontWeight.Bold else FontWeight.Medium,
                                        color = if (esSeleccionado) Color.White else TextoOscuro
                                    )
                                }
                            }
                            repeat(3 - filaHoras.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}
