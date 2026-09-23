package com.yldefonso.tecsupfit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.tecsupfit.ui.theme.CompletadaBg
import com.example.tecsupfit.ui.theme.CompletadaText
import com.example.tecsupfit.ui.theme.ConfirmadaBg
import com.example.tecsupfit.ui.theme.ConfirmadaText
import com.example.tecsupfit.ui.theme.TealPrimary
import com.yldefonso.tecsupfit.components.AppBottomBar
import com.yldefonso.tecsupfit.components.CancelDialog
import com.yldefonso.tecsupfit.model.Reserva

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservasScreen(
    navController: NavController,
    // 1. Se cambia List<Reserva> a SnapshotStateList<Reserva> para permitir la eliminación reactiva
    reservas: SnapshotStateList<Reserva>
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis reservas") }) },
        bottomBar = { AppBottomBar(navController) }
    ) { padding ->
        if (reservas.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Aún no tienes clases reservadas")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reservas, key = { it.id }) { reserva ->
                    // Se pasa la referencia de la lista reactiva de reservas a la card
                    ReservaCard(reserva = reserva, reservas = reservas)
                }
            }
        }
    }
}

// Card con franja verde a la izquierda
@Composable
private fun ReservaCard(
    reserva: Reserva,
    // Se recibe reservas como SnapshotStateList para manipular sus elementos directamente
    reservas: SnapshotStateList<Reserva>
) {
    val confirmada = reserva.estado == "Confirmada"

    // Variable de estado para controlar la visibilidad del diálogo de confirmación de cancelación
    var showCancelDialog by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier.width(5.dp).fillMaxHeight().background(TealPrimary)
            )
            Column(modifier = Modifier.padding(start = 16.dp, top = 14.dp, bottom = 14.dp, end = 16.dp)) {
                Text(reserva.claseNombre, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(
                    "${reserva.horario} · ${reserva.sala}",
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
                        reserva.estado,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (confirmada) ConfirmadaText else CompletadaText
                    )
                }

                // 2. Solo si la reserva está "Confirmada", se muestra el botón para cancelar
                if (confirmada) {
                    Spacer(Modifier.height(4.dp))
                    TextButton(
                        onClick = { showCancelDialog = true },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Cancelar reserva",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }

    // Muestra el AlertDialog cuando showCancelDialog es true
    if (showCancelDialog) {
        CancelDialog(
            claseNombre = reserva.claseNombre,
            onDismiss = { showCancelDialog = false },
            onConfirm = {
                // Elimina la reserva especificada de la SnapshotStateList reactiva
                reservas.removeAll { it.id == reserva.id }
                showCancelDialog = false
            }
        )
    }
}
