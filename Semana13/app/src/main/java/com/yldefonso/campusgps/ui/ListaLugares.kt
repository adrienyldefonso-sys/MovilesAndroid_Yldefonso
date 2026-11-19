
package com.yldefonso.campusgps.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import com.yldefonso.campusgps.model.Lugar
import com.yldefonso.campusgps.model.LugarConDistancia
import com.yldefonso.campusgps.ui.components.TarjetaLugar
import com.yldefonso.campusgps.ui.theme.Dimens

/**
 * [G] Lista reutilizable de lugares del campus.
 *
 * Recibe los lugares, el identificador seleccionado,
 * los identificadores dentro del radio y los callbacks.
 *
 * No conoce el ViewModel ni calcula distancias.
 * Cada elemento se dibuja mediante TarjetaLugar.
 */
@Composable
fun ListaLugares(
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    idsDentroDelRadio: Set<Int>,
    onClick: (Int) -> Unit,
    onComoLlegar: (Lugar) -> Unit = {},
    modifier: Modifier = Modifier
) {

    // LazyColumn renderiza únicamente los elementos visibles.
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = Dimens.espacioM,
            vertical = Dimens.espacioXs
        ),
        verticalArrangement = Arrangement.spacedBy(
            Dimens.espacioS
        )
    ) {

        // La clave mantiene la identidad de cada lugar
        // cuando cambian los filtros o el orden de la lista.
        items(
            items = lugares,
            key = { item -> item.lugar.id }
        ) { item ->

            // Determina si el lugar está seleccionado.
            val esSeleccionado =
                item.lugar.id == seleccionado

            // Determina si pertenece al radio seleccionado.
            val estaDentroRadio =
                idsDentroDelRadio.contains(item.lugar.id)

            // Componente visual reutilizable y sin estado.
            TarjetaLugar(
                item = item,
                esSeleccionado = esSeleccionado,
                estaDentroRadio = estaDentroRadio,

                // Comunica la selección a la pantalla.
                onClick = {
                    onClick(item.lugar.id)
                },

                // Comunica la solicitud de navegación.
                onComoLlegar = { lugar ->
                    onComoLlegar(lugar)
                }
            )
        }
    }
}
