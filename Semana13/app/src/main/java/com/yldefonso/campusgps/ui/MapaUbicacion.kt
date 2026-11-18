package com.yldefonso.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.yldefonso.campusgps.model.Lugar
import com.yldefonso.campusgps.model.LugarConDistancia
import com.yldefonso.campusgps.model.UbicacionUi

/**
 * [G] Mapa de Google.
 * Paso 3: la cámara viaja a tu posición y se dibuja el marcador "Estás aquí".
 * Paso 4: se dibuja un marcador por cada lugar del campus.
 * Paso 6: al seleccionar un lugar, la cámara viaja hasta él.
 * Paso 7: el lugar más cercano se dibuja en verde; los demás siguen en azul.
 * Solo DIBUJA lo que recibe: no consulta Repository ni calcula distancias.
 */
@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,        // lista que llega ya lista desde el ViewModel
    seleccionado: Int?,                      // id del lugar tocado en la lista (null = ninguno)
    masCercano: Lugar?,                      // Paso 7: lugar más cercano (null si aún no hay ubicación)
    modifier: Modifier = Modifier
) {
    // Estado de la cámara: empieza en Lima (punto base) con zoom 12 (vista de ciudad).
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), 12f)
    }

    // Cuando la ubicación llega (Lista), anima la cámara hasta tu posición con zoom 16.
    LaunchedEffect(estado) {
        if (estado is UbicacionUi.Lista) {
            camara.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(estado.posicion.latitud, estado.posicion.longitud), 16f
                )
            )
        }
    }

    // Cada vez que cambia el lugar seleccionado, la cámara viaja hasta él con zoom 17.
    LaunchedEffect(seleccionado) {
        val lugar = lugares.firstOrNull { it.lugar.id == seleccionado }?.lugar
        if (lugar != null) {
            camara.animate(
                CameraUpdateFactory.newLatLngZoom(LatLng(lugar.latitud, lugar.longitud), 17f)
            )
        }
    }

    GoogleMap(modifier = modifier, cameraPositionState = camara) {
        // Marcador propio: rojo (color por defecto), solo cuando ya tenemos la ubicación.
        if (estado is UbicacionUi.Lista) {
            val posicion = LatLng(estado.posicion.latitud, estado.posicion.longitud)
            Marker(
                state = rememberMarkerState(key = "$posicion", position = posicion),
                title = "Estás aquí"
            )
        }

        // Un marcador por cada lugar del campus.
        for (item in lugares) {
            val lugar = item.lugar
            // key(id): identifica cada marcador de forma estable aunque la lista se reordene.
            key(lugar.id) {
                val estadoMarcador = rememberMarkerState(
                    key = "lugar-${lugar.id}",
                    position = LatLng(lugar.latitud, lugar.longitud)
                )

                // Si este lugar es el seleccionado, abre su globo con nombre y categoría.
                LaunchedEffect(seleccionado) {
                    if (seleccionado == lugar.id) estadoMarcador.showInfoWindow()
                }

                // Paso 7: verde (HUE_GREEN) para el más cercano; azul (HUE_AZURE) para los demás.
                val color = if (lugar.id == masCercano?.id)
                    BitmapDescriptorFactory.HUE_GREEN
                else
                    BitmapDescriptorFactory.HUE_AZURE

                Marker(
                    state = estadoMarcador,
                    title = lugar.nombre,           // nombre en negrita al tocar el marcador
                    snippet = lugar.categoria,      // categoría en gris debajo del nombre
                    icon = BitmapDescriptorFactory.defaultMarker(color)
                )
            }
        }
    }
}