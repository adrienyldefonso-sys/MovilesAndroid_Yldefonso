package com.yldefonso.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.yldefonso.campusgps.model.LugarConDistancia
import com.yldefonso.campusgps.model.UbicacionUi

/**
 * [G] Mapa de Google. TODAVÍA NO se usa en PantallaMapa (se conecta en el Paso 3),
 * porque sin la clave de Maps (Paso 1) abrir el mapa cierra la app.
 * Solo DIBUJA lo que recibe: no consulta Repository ni calcula distancias.
 */
@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    modifier: Modifier = Modifier
) {
    // Estado de la cámara: empieza en Lima (punto base) con zoom 12 (ciudad).
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), 12f)
    }

    // TODO 1 (Paso 3): LaunchedEffect(estado): si es Lista, animar la cámara (zoom 16f)

    GoogleMap(modifier = modifier, cameraPositionState = camara) {
        // TODO 2 (Paso 3): si el estado es Lista, un Marker "Estás aquí"
        // TODO 3 (Paso 4): un Marker por cada lugar
    }
}
