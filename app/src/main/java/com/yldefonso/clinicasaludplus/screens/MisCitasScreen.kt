package com.yldefonso.clinicasaludplus.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yldefonso.clinicasaludplus.components.RatingDialog
import com.yldefonso.clinicasaludplus.model.Cita
import com.yldefonso.clinicasaludplus.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisCitasScreen(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    // Recibe SnapshotStateList para que las modificaciones en la lista disparen recomposición
    citas: SnapshotStateList<Cita>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis citas") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                    }
                }
            )
        }
    ) { padding ->
        if (citas.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Aún no tienes citas agendadas")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Iteramos la lista pasando 'citas' a cada Card para permitir actualización puntual
                items(citas, key = { it.id }) { cita ->
                    CitaCard(cita = cita, citas = citas)
                }
            }
        }
    }
}

// Card tipo "cuadrada" con una franja morada a la izquierda, nombre en
// negrita, fecha/hora debajo, el estado como pill de color y la opción de calificación para completadas.
@Composable
private fun CitaCard(
    cita: Cita,
    citas: SnapshotStateList<Cita>
) {
    val confirmada = cita.estado == "Confirmada"

    // Estado local para controlar la visibilidad del diálogo de calificación
    var mostrarDialogo by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // Franja/línea morada a la izquierda
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(PurpleMid)
            )
            Column(
                modifier = Modifier.padding(start = 16.dp, top = 14.dp, bottom = 14.dp, end = 16.dp)
            ) {
                Text(
                    cita.medicoNombre,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "${cita.fecha}, ${cita.hora}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF757575)
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (confirmada) ConfirmadaBg else CompletadaBg)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        cita.estado,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (confirmada) ConfirmadaText else CompletadaText
                    )
                }

                // Sección de calificación únicamente activa para citas en estado "Completada"
                if (!confirmada) {
                    // Espaciado cómodo entre el pill de estado y el botón/texto de calificación
                    Spacer(Modifier.height(6.dp))
                    if (cita.calificacion == null) {
                        // Botón "Calificar atención" en PurpleMid para abrir el diálogo
                        TextButton(
                            onClick = { mostrarDialogo = true },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                "Calificar atención",
                                style = MaterialTheme.typography.labelMedium,
                                color = PurpleMid,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        // Muestra el ícono de estrella y el texto "Calificado (X/5)" alineados en PurpleMid
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Calificación",
                                tint = PurpleMid,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                "Calificado (${cita.calificacion}/5)",
                                style = MaterialTheme.typography.labelMedium,
                                color = PurpleMid,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }

    // Muestra el diálogo de calificación al pulsar en "Calificar atención"
    if (mostrarDialogo) {
        RatingDialog(
            onDismiss = { mostrarDialogo = false },
            onConfirm = { estrellas ->
                // Al modificar un elemento de la SnapshotStateList por una copia,
                // Compose recompone únicamente la tarjeta correspondiente
                val index = citas.indexOfFirst { it.id == cita.id }
                if (index != -1) {
                    citas[index] = cita.copy(calificacion = estrellas)
                }
                mostrarDialogo = false
            }
        )
    }
}