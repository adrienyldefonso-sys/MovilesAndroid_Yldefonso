package com.yldefonso.clinicasaludplus.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
    citas: List<Cita>
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
                items(citas) { cita -> CitaCard(cita) }
            }
        }
    }
}

// Card tipo "cuadrada" con una franja morada a la izquierda, nombre en
// negrita, fecha/hora debajo, y el estado como pill de color al final.
@Composable
private fun CitaCard(cita: Cita) {
    val confirmada = cita.estado == "Confirmada"

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
            }
        }
    }
}