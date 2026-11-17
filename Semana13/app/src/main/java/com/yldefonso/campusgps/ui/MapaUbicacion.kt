package com.yldefonso.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.yldefonso.campusgps.model.LugarConDistancia
import com.yldefonso.campusgps.model.UbicacionUi

/**
 * [G] Mapa de Google. Paso 3: la cámara viaja a tu posición y se dibuja el marcador "Estás aquí".
 * Solo DIBUJA lo que recibe: no consulta Repository ni calcula distancias.
 */
@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,        // se usará en el Paso 4 (marcadores de lugares)
    seleccionado: Int?,                      // se usará en el Paso 6 (mover cámara al lugar tocado)
    modifier: Modifier = Modifier
) {
    // Estado de la cámara: empieza en Lima (punto base) con zoom 12 (vista de ciudad).
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), 12f)
    }

    // Se ejecuta cada vez que CAMBIA el estado. Si la ubicación llegó (Lista),
    // anima la cámara hasta tu posición con zoom 16 (nivel de calle).
    // animate() es suspend: por eso va dentro de un LaunchedEffect (corrutina de Compose).
    LaunchedEffect(estado) {
        if (estado is UbicacionUi.Lista) {
            camara.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(estado.posicion.latitud, estado.posicion.longitud), 16f
                )
            )
        }
    }

    GoogleMap(modifier = modifier, cameraPositionState = camara) {
        // El marcador propio solo existe cuando ya tenemos la ubicación (estado Lista).
        if (estado is UbicacionUi.Lista) {
            val posicion = LatLng(estado.posicion.latitud, estado.posicion.longitud)
            Marker(
                // key = posición: si buscas de nuevo en otro punto, el marcador se recrea y se mueve.
                state = rememberMarkerState(key = "$posicion", position = posicion),
                title = "Estás aquí"          // texto que se ve al tocar el marcador (rojo por defecto)
            )
        }
        // TODO 3 (Paso 4): un Marker por cada lugar
    }
}