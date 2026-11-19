package com.yldefonso.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yldefonso.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareasViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CentroTareas", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TarjetaProgreso(vm)
            TarjetaPrimos(vm)
            TarjetaCargas(vm)
        }
    }
}

@Composable
private fun TarjetaProgreso(vm: TareasViewModel) {
    val mensaje by vm.mensaje.collectAsState()
    var toques by remember { mutableIntStateOf(0) }

    Tarjeta("Tarea con progreso") {
        // Prueba de vida: si deja de sumar, la pantalla está congelada
        Button(onClick = { toques++ }) {
            Text("Tócame: $toques")
        }
        // Paso 2: ahora llama a tareaLarga() (corrutina, no bloquea)
        Button(onClick = { vm.tareaLarga() }) {
            Text("Tarea larga")
        }
        Text(mensaje)

        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = MaterialTheme.shapes.small
        ) {
            Text(
                text = "Los toques siguen sumando mientras trabaja",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
        // TODO Paso 3: LinearProgressIndicator y porcentaje
        // TODO Paso 4: botones Iniciar / Cancelar
    }
}

@Composable
private fun TarjetaPrimos(vm: TareasViewModel) {
    Tarjeta("Contar números primos") {
        // TODO Paso 5: OutlinedTextField, botones Contar / Cancelar y resultado
    }
}

@Composable
private fun TarjetaCargas(vm: TareasViewModel) {
    Tarjeta("Dos cargas") {
        // TODO Paso 6: botones «Una por una» / «A la vez» y resultado
        // TODO Paso 7: Switch «Simular error» y botón Reintentar
    }
}

@Composable
private fun Tarjeta(titulo: String, contenido: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )
            contenido()
        }
    }
}