// [G] Commit 3: tarjeta de permiso con botón y etiqueta de estado. Las demás tarjetas siguen con TODO.
package com.yldefonso.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yldefonso.model.PermisoUi
import com.yldefonso.notificacion.Notificador
import com.yldefonso.viewmodel.AvisosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {
    val contexto = LocalContext.current
    val permiso by vm.permiso.collectAsState()

    // Lanzador del cuadro de permiso de Android (API 33+).
    // Cuando el usuario responde, Android llama a este bloque con true (aceptó) o false (negó).
    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        vm.permisoRespondido(concedido)
    }

    // Al abrir la pantalla: si el permiso ya estaba concedido, se refleja en el ViewModel.
    LaunchedEffect(Unit) {
        if (Notificador.tienePermiso(contexto)) {
            vm.permisoRespondido(true)
        }
    }

    // Ejemplo resuelto (ya viene hecho): cada evento se convierte en una notificación.
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
            // Paso 2 (commit 3): permiso, botón y etiqueta de estado
            TarjetaFuncion("Permiso de notificaciones") {
                // Etiqueta de color: gris = Sin pedir, verde = Permitido, rojo = Denegado
                val (textoEstado, colorEstado) = when (permiso) {
                    PermisoUi.SinPedir -> "Sin pedir" to Color.Gray
                    PermisoUi.Concedido -> "Permitido" to Color(0xFF2E7D32)
                    PermisoUi.Denegado -> "Denegado" to Color(0xFFC62828)
                }
                Text(
                    text = textoEstado,
                    color = colorEstado,
                    fontWeight = FontWeight.Bold
                )
                Button(onClick = {
                    // API 33+: lanza el cuadro de Android. Antes: se considera concedido directamente.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        lanzador.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        vm.permisoRespondido(true)
                    }
                }) {
                    Text("Permitir notificaciones")
                }
            }

            // Paso 3: botón "Mostrar aviso" -> vm.avisarAhora()
            TarjetaFuncion("Avisos") {
                // TODO Paso 3
            }

            // Paso 5: Recordar en 10 s, Cancelar y "Faltan N s"
            TarjetaFuncion("Recordatorio") {
                // TODO Paso 5
            }

            // Paso 6: Switch "Escuchar el modo avión"
            TarjetaFuncion("Receptor de anuncios") {
                // TODO Paso 6
            }

            // Paso 7: historial
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