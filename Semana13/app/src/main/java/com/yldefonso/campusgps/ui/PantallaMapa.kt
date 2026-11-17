package com.yldefonso.campusgps.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yldefonso.campusgps.model.UbicacionUi
import com.yldefonso.campusgps.viewmodel.MapaViewModel
import java.util.Locale

/**
 * [G] Pantalla del ejemplo resuelto: botón + latitud/longitud.
 * La View SOLO dibuja según el estado y le avisa eventos al ViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    // collectAsState: cada vez que el estado cambia, Compose redibuja esta pantalla.
    val estado by vm.estado.collectAsState()

    // TODO Paso 2: val contexto = LocalContext.current + rememberLauncherForActivityResult(
    //              RequestMultiplePermissions()) para pedir el permiso antes de buscar.
    // TODO Paso 3: MapaUbicacion(...) con weight(1f) sobre el botón.
    // TODO Paso 6: ListaLugares(...) debajo, con altura fija.
    // TODO Paso 8: MensajeError(...) cuando el estado sea Error.

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CampusGPS") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Button(
                // Por ahora llama directo a buscar(); en el Paso 2 pedirá el permiso primero.
                onClick = { vm.buscar() },
                enabled = estado !is UbicacionUi.Buscando   // deshabilitado mientras busca
            ) { Text("Obtener mi ubicación") }

            Spacer(Modifier.height(16.dp))

            // `when` sobre el sealed interface: un bloque por cada estado posible.
            when (val e = estado) {
                UbicacionUi.Inicial -> Text("Pulsa el botón para empezar")
                UbicacionUi.Buscando -> CircularProgressIndicator()
                is UbicacionUi.Lista -> {
                    // Locale.US garantiza punto decimal (-12.0464) en cualquier idioma del celular.
                    Text("Latitud: ${String.format(Locale.US, "%.4f", e.posicion.latitud)}")
                    Text("Longitud: ${String.format(Locale.US, "%.4f", e.posicion.longitud)}")
                }
                is UbicacionUi.Error -> Text(e.mensaje, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
