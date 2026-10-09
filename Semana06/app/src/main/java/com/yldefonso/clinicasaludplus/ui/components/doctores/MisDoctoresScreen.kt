package com.yldefonso.clinicasaludplus.ui.components.doctores

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
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

private val AzulPrimario = Color(0xFF2F6BEA)
private val AzulPastel = Color(0xFFEAF1FF)
private val TextoOscuro = Color(0xFF1B2540)
private val TextoGris = Color(0xFF6B7690)
private val FondoPantalla = Color(0xFFF8FAFC)
private val BordeCard = Color(0xFFE2E8F0)
private val ColorEstrella = Color(0xFFFFB800)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisDoctoresScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val todosMedicos = Repositorio.buscarTodosLosMedicos(searchQuery)

    // Agrupar los médicos filtrados por Especialidad
    val medicosPorEspecialidad = remember(todosMedicos) {
        todosMedicos.groupBy { med ->
            Repositorio.especialidades.find { it.id == med.especialidadId }?.nombre ?: "Otras Especialidades"
        }
    }

    Scaffold(
        containerColor = FondoPantalla,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Directorio Médico",
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            // SUBTÍTULO Y BUSCADOR
            Text(
                text = "Conoce a nuestro equipo de especialistas de primer nivel",
                fontSize = 14.sp,
                color = TextoGris,
                modifier = Modifier.padding(bottom = 14.dp)
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                placeholder = {
                    Text(
                        text = "Buscar por médico, especialidad o CMP...",
                        color = TextoGris,
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = TextoGris
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(26.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = BordeCard,
                    focusedBorderColor = AzulPrimario,
                    cursorColor = AzulPrimario
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (medicosPorEspecialidad.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se encontraron médicos.",
                        fontSize = 14.sp,
                        color = TextoGris
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    medicosPorEspecialidad.forEach { (nombreEspecialidad, medicosLista) ->
                        item {
                            Text(
                                text = nombreEspecialidad,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextoOscuro,
                                modifier = Modifier.padding(vertical = 4.dp, horizontal = 2.dp)
                            )
                        }

                        items(medicosLista) { medico ->
                            CardDoctorItem(
                                medico = medico,
                                nombreEspecialidad = nombreEspecialidad,
                                onClickAgendar = {
                                    val esp = Repositorio.especialidades.find { it.id == medico.especialidadId }
                                    Repositorio.especialidadSeleccionada = esp
                                    Repositorio.medicoSeleccionado = medico
                                    if (Repositorio.sedeSeleccionada == null) {
                                        navController.navigate(Rutas.Sedes.ruta)
                                    } else {
                                        navController.navigate(Rutas.FechaHora.ruta)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CardDoctorItem(
    medico: Medico,
    nombreEspecialidad: String,
    onClickAgendar: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickAgendar() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BordeCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // AVATAR CIRCULAR
                AsyncImage(
                    model = medico.fotoUrl,
                    contentDescription = "Foto de ${medico.nombre}",
                    placeholder = rememberVectorPainter(Icons.Default.Person),
                    error = rememberVectorPainter(Icons.Default.Person),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(AzulPastel)
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = medico.nombre,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = TextoOscuro
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = nombreEspecialidad,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = AzulPrimario
                    )
                    Text(
                        text = medico.cmp,
                        fontSize = 12.sp,
                        color = TextoGris
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(thickness = 1.dp, color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // CALIFICACIÓN DE EJEMPLO
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Calificación",
                        tint = ColorEstrella,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "4.9",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TextoOscuro
                    )
                    Text(
                        text = " (Excelente)",
                        fontSize = 12.sp,
                        color = TextoGris
                    )
                }

                // CHIP/BOTÓN DE ACCIÓN
                Button(
                    onClick = onClickAgendar,
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulPastel,
                        contentColor = AzulPrimario
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Ver agenda",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
