package com.yldefonso.clinicasaludplus.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yldefonso.clinicasaludplus.model.Cita
import com.yldefonso.clinicasaludplus.navigation.Screen
import com.yldefonso.clinicasaludplus.ui.theme.PurpleLight
import com.yldefonso.clinicasaludplus.ui.theme.PurpleMid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Pantalla de perfil del paciente con estadísticas de citas y opción de cerrar sesión
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    citas: List<Cita>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi perfil") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar con iniciales del paciente
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(PurpleLight),
                contentAlignment = Alignment.Center
            ) {
                Text("JP", color = PurpleMid, style = MaterialTheme.typography.headlineSmall)
            }
            Spacer(Modifier.height(12.dp))
            Text("Juan Pérez", style = MaterialTheme.typography.titleLarge)
            Text("Paciente", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(16.dp))
            Text("Correo: juan.perez@example.com")
            Text("Teléfono: +51 987 654 321")

            Spacer(Modifier.height(28.dp))

            // Sección de estadísticas calculadas dinámicamente según la lista de citas
            Text(
                text = "Mis estadísticas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Tarjeta: Total de citas registradas
                StatCard(
                    titulo = "Citas totales",
                    valor = citas.size.toString(),
                    modifier = Modifier.weight(1f)
                )
                // Tarjeta: Citas en estado Confirmada
                StatCard(
                    titulo = "Confirmadas",
                    valor = citas.count { it.estado == "Confirmada" }.toString(),
                    modifier = Modifier.weight(1f)
                )
                // Tarjeta: Citas en estado Completada
                StatCard(
                    titulo = "Completadas",
                    valor = citas.count { it.estado == "Completada" }.toString(),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.weight(1f))

            // Botón para cerrar sesión con borde y texto de color rojo error
            OutlinedButton(
                onClick = {
                    // Limpia todo el historial de navegación y regresa al Login
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Cerrar sesión", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// Componente auxiliar para renderizar cada tarjeta de estadística
@Composable
private fun StatCard(
    titulo: String,
    valor: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = valor,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = PurpleMid
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
