package com.yldefonso.clinicasaludplus.ui.components.agendamiento

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yldefonso.clinicasaludplus.model.Medico
import com.yldefonso.clinicasaludplus.navigation.Rutas
import com.yldefonso.clinicasaludplus.repository.Repositorio

// Paleta de colores
private val TextoOscuro = Color(0xFF1B2540)
private val TextoGris = Color(0xFF6B7690)
private val FondoPantalla = Color.White
private val BordeCard = Color(0xFFE2E8F0)
private val AzulPrimario = Color(0xFF2F6BEA)

// Badge verde menta claro y texto oscuro
private val FondoVerde = Color(0xFFF0FDF4)
private val TextoVerde = Color(0xFF166534)
private val ColorEstrella = Color(0xFFFFB800)

private val calificaciones = listOf("4.9", "4.8", "4.7", "4.6")
private val totalResenas = listOf(124, 98, 85, 76)
private val disponibilidades = listOf(
    "Disponible hoy",
    "Disponible mañana",
    "Disponible hoy",
    "Disponible esta semana"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicosScreen(
    navController: NavController,
    especialidadId: Int
) {
    var query by remember { mutableStateOf("") }
    var buscando by remember { mutableStateOf(false) }
    val especialidad = Repositorio.especialidades.find { it.id == especialidadId }
        ?: Repositorio.especialidadSeleccionada
    val medicosFiltrados = Repositorio.buscarMedicos(especialidadId, query)

    Scaffold(
        containerColor = FondoPantalla,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Médicos de ${especialidad?.nombre ?: "Especialidad"}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 21.sp,
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
                actions = {
                    IconButton(onClick = {
                        buscando = !buscando
                        if (!buscando) query = ""
                    }) {
                        Icon(
                            imageVector = if (buscando) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = "Buscar médico",
                            tint = TextoOscuro
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = FondoPantalla
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            if (buscando) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    placeholder = {
                        Text(
                            text = "Buscar médico por nombre...",
                            color = TextoGris,
                            fontSize = 14.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = TextoGris
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(27.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedBorderColor = BordeCard,
                        focusedBorderColor = AzulPrimario,
                        cursorColor = AzulPrimario
                    )
                )
                Spacer(modifier = Modifier.height(14.dp))
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (medicosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se encontraron médicos disponibles.",
                        fontSize = 14.sp,
                        color = TextoGris
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    itemsIndexed(medicosFiltrados) { index, med ->
                        CardMedico(
                            medico = med,
                            nombreEspecialidad = Repositorio.especialidades
                                .find { it.id == med.especialidadId }?.nombre
                                ?: especialidad?.nombre
                                ?: "",
                            calificacion = calificaciones[index % calificaciones.size],
                            resenas = totalResenas[index % totalResenas.size],
                            disponibilidad = disponibilidades[index % disponibilidades.size],
                            onClick = {
                                Repositorio.medicoSeleccionado = med
                                navController.navigate(Rutas.FechaHora.ruta)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CardMedico(
    medico: Medico,
    nombreEspecialidad: String,
    calificacion: String,
    resenas: Int,
    disponibilidad: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BordeCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // FILA PRINCIPAL: FOTO Y TEXTOS CENTRADOS VERTICALMENTE
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // FOTO DEL MÉDICO
                AsyncImage(
                    model = medico.fotoUrl,
                    contentDescription = "Foto de ${medico.nombre}",
                    placeholder = rememberVectorPainter(Icons.Default.Person),
                    error = rememberVectorPainter(Icons.Default.Person),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEAF1FF))
                )

                Spacer(modifier = Modifier.width(16.dp))

                // COLUMNA CON NOMBRE, ESPECIALIDAD Y CALIFICACIÓN
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = medico.nombre,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextoOscuro
                    )
                    if (nombreEspecialidad.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = nombreEspecialidad,
                            fontSize = 14.sp,
                            color = TextoGris
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Calificación",
                            tint = ColorEstrella,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = calificacion,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = TextoOscuro
                        )
                        Text(
                            text = " ($resenas)",
                            fontSize = 12.sp,
                            color = TextoGris
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // BADGE DE DISPONIBILIDAD (ALINEADO A LA DERECHA)
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(FondoVerde)
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = disponibilidad,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextoVerde
                    )
                }
            }
        }
    }
}