// [G] GUIADO: esqueleto con las cinco tarjetas y TODO. Se completa en los pasos 2 a 8.
// La View conoce solo a su ViewModel y al Notificador; no decide qué se avisa.
package com.yldefonso.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.yldefonso.notificacion.Notificador
import com.yldefonso.viewmodel.AvisosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {
    // Contexto de Android: lo tiene la View (no el ViewModel), por eso la View llama al Notificador
    val contexto = LocalContext.current
    val permiso by vm.permiso.collectAsState()

    // Ejemplo resuelto (ya viene hecho): cada evento del ViewModel se convierte en una notificación.
    LaunchedEffect(Unit) {
        vm.eventos.collect { aviso ->
            val mostrado = Notificador.mostrar(contexto, aviso)
            // TODO Paso 8: si !mostrado, avisar al ViewModel con vm.noSePudoMostrar()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AvisosCampus") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Paso 2: botón "Permitir notificaciones" y etiqueta de estado
            TarjetaFuncion("Permiso de notificaciones") {
                // TODO Paso 2: etiqueta Sin pedir (gris) / Permitido (verde) / Denegado (rojo)
                Text("Estado: ${permiso::class.simpleName}")
            }

            // Paso 3: botón "Mostrar aviso" -> vm.avisarAhora()
            TarjetaFuncion("Avisos") {
                // TODO Paso 3
            }

            // Paso 5: Recordar en 10 s, Cancelar y "Faltan N s"
            TarjetaFuncion("Recordatorio") {
                // TODO Paso 5
            }

            // Paso 6: Switch "Escuchar el modo avión" + DisposableEffect con el receptor
            TarjetaFuncion("Receptor de anuncios") {
                // TODO Paso 6
            }

            // Paso 7: historial (HistorialAvisos recibe vm.avisos con collectAsState())
            TarjetaFuncion("Historial") {
                // TODO Paso 7
            }
        }
    }
}

// Tarjeta reutilizable: título en azul y contenido propio.
@Composable
private fun TarjetaFuncion(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            contenido()
        }
    }
}
