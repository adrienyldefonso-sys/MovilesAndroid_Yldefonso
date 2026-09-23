package com.yldefonso.tecsupfit.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tecsupfit.ui.theme.TealLight
import com.example.tecsupfit.ui.theme.TealPrimary
import com.yldefonso.tecsupfit.components.AppBottomBar
import com.yldefonso.tecsupfit.model.Reserva
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(navController: NavController, reservas: List<Reserva>) {
    //Estadísticas calculadas en tiempo real a partir de las reservas
    val clasesTomadas = reservas.count { it.estado == "Completada" }
    val racha = reservas.count { it.estado == "Confirmada" }  // ejemplo simple

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mi perfil") }) },
        bottomBar = { AppBottomBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(80.dp).clip(CircleShape).background(TealLight),
                contentAlignment = Alignment.Center
            ) { Text("DR", color = TealPrimary, style = MaterialTheme.typography.headlineSmall) }
            Spacer(Modifier.height(12.dp))
            Text("Diego Ramos", style = MaterialTheme.typography.titleLarge)
            Text("Plan Premium", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(clasesTomadas.toString(), "Clases")
                StatCard(racha.toString(), "Rachas")
            }
        }
    }
}

@Composable
private fun StatCard(numero: String, etiqueta: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF2F2F2))
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(numero, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(etiqueta, style = MaterialTheme.typography.bodySmall)
    }
}