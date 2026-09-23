package com.yldefonso.clinicasaludplus.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yldefonso.clinicasaludplus.ui.theme.PurpleLight
import com.yldefonso.clinicasaludplus.ui.theme.PurpleMid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Pantalla simple de perfil del paciente (datos hardcodeados, sin login real)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope
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
            modifier = Modifier.padding(padding).fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(80.dp).clip(CircleShape).background(PurpleLight),
                contentAlignment = Alignment.Center
            ) { Text("JP", color = PurpleMid, style = MaterialTheme.typography.headlineSmall) }
            Spacer(Modifier.height(12.dp))
            Text("Juan Pérez", style = MaterialTheme.typography.titleLarge)
            Text("Paciente", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(24.dp))
            Text("Correo: juan.perez@example.com")
            Text("Teléfono: +51 987 654 321")
        }
    }
}