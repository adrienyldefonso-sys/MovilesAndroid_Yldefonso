package com.yldefonso.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.yldefonso.tecsupfit.components.AppBottomBar

//Pantalla simple y destino requerido por el bottomBar (4 pestañas)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutinasScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Rutinas") }) },
        bottomBar = { AppBottomBar(navController) }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Text("Próximamente: rutinas personalizadas")
        }
    }
}