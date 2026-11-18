package com.yldefonso.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.yldefonso.campusgps.model.UbicacionUi
import com.yldefonso.campusgps.util.formatearDistancia
import com.yldefonso.campusgps.viewmodel.MapaViewModel
import java.util.Locale

/**
 * [G] Pantalla principal. Paso 8: los errores se muestran con MensajeError
 * (mensaje + botón según el tipo de error).
 * La View SOLO dibuja según el estado y le avisa eventos al ViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    // collectAsState: cada vez que un estado cambia, Compose redibuja lo que lo usa.
    val estado by vm.estado.collectAsState()
    val lugares by vm.lugares.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()      // null hasta que haya ubicación

    // Context actual: lo necesitamos SOLO aquí en la View para consultar el permiso.
    val contexto = LocalContext.current

    // Lanzador que muestra el cuadro de permiso de Android (lo dibuja el sistema).
    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        // Basta con que el usuario conceda UNO (preciso o aproximado) para obtener la ubicación.
        if (resultado.values.any { it }) vm.buscar() else vm.permisoDenegado()
    }

    // Se ejecuta al pulsar el botón principal (y también desde "Reintentar").
    fun alPulsar() {
        val fine = ContextCompat.checkSelfPermission(
            contexto, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            contexto, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fine || coarse) {
            vm.buscar()   // ya tiene permiso: busca directo
        } else {
            lanzador.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

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
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            // weight(1f): el mapa ocupa TODO el espacio que sobra; lo de abajo mantiene su tamaño.
            MapaUbicacion(
                estado = estado,
                lugares = lugares,
                seleccionado = seleccionado,
                masCercano = masCercano,
                modifier = Modifier.weight(1f).fillMaxWidth()
            )

            Column(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { alPulsar() },                    // pasa por la verificación de permiso
                    enabled = estado !is UbicacionUi.Buscando   // deshabilitado mientras busca
                ) { Text("Obtener mi ubicación") }

                // Botón con borde. Deshabilitado mientras masCercano sea null.
                OutlinedButton(
                    onClick = { masCercano?.let { vm.seleccionar(it.id) } },
                    enabled = masCercano != null
                ) { Text("Ir al más cercano") }

                // "Más cercano: nombre (distancia)". La distancia ya viene calculada; aquí solo se formatea.
                masCercano?.let { cercano ->
                    val metros = lugares.firstOrNull { it.lugar.id == cercano.id }?.metros
                    Text(
                        buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Más cercano: ") }
                            append(cercano.nombre)
                            if (metros != null) append(" (${formatearDistancia(metros)})")
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(Modifier.height(2.dp))

                // `when` sobre el sealed interface: un bloque por cada estado posible.
                when (val e = estado) {
                    UbicacionUi.Inicial -> Text("Pulsa el botón para empezar", style = MaterialTheme.typography.bodySmall)
                    UbicacionUi.Buscando -> CircularProgressIndicator()
                    is UbicacionUi.Lista -> {
                        // Una sola línea para ahorrar espacio. Locale.US garantiza punto decimal.
                        Text(
                            "Latitud: ${String.format(Locale.US, "%.4f", e.posicion.latitud)}   " +
                                    "Longitud: ${String.format(Locale.US, "%.4f", e.posicion.longitud)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    // Paso 8: recuadro de error + botón según el tipo (Abrir ajustes / Reintentar / ninguno).
                    is UbicacionUi.Error -> MensajeError(error = e, onReintentar = { alPulsar() })
                }
            }

            // Lista con altura FIJA (220.dp): así el mapa conserva el resto del espacio.
            ListaLugares(
                lugares = lugares,
                seleccionado = seleccionado,
                onClick = { id -> vm.seleccionar(id) },
                modifier = Modifier.fillMaxWidth().height(220.dp)
            )
        }
    }
}