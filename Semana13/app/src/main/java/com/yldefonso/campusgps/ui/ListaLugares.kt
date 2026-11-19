package com.yldefonso.campusgps.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yldefonso.campusgps.model.LugarConDistancia
import com.yldefonso.campusgps.util.formatearDistancia

/**
 * [A] Lista de lugares del campus (Paso 6).
 * No conoce el ViewModel: recibe la lista, el id seleccionado y un onClick(id).
 * Solo DIBUJA; la distancia ya viene calculada en cada LugarConDistancia.
 */
@Composable
fun ListaLugares(
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    idsDentroDelRadio: Set<Int>,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // LazyColumn: solo compone las tarjetas visibles (eficiente y con scroll automático).
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // key = id: Compose identifica cada tarjeta aunque la lista cambie de orden (Paso 5).
        items(lugares, key = { it.lugar.id }) { item ->
            val esSeleccionado = item.lugar.id == seleccionado

            Card(
                onClick = { onClick(item.lugar.id) },     // avisa QUÉ lugar se tocó, nada más
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    // La tarjeta seleccionada se distingue con otro fondo y un borde.
                    containerColor = if (esSeleccionado)
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                ),
                border = if (esSeleccionado) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Punto de color (azul como los marcadores; el Paso 7 lo pondrá verde en el más cercano).
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(Color(0xFF1E88E5), CircleShape)
                    )
                    Spacer(Modifier.width(10.dp))

                    // weight(1f): el nombre/categoría ocupa el espacio y empuja la distancia a la derecha.
                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.lugar.nombre, fontWeight = FontWeight.Bold)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                item.lugar.categoria,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            // Si el lugar está dentro del radio seleccionado, muestra una etiqueta clara.
                            if (idsDentroDelRadio.contains(item.lugar.id)) {
                                Text(
                                    "• Dentro del radio",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Si metros es null (aún no se busca la ubicación) muestra "Sin calcular".
                    Text(
                        text = item.metros?.let { formatearDistancia(it) } ?: "Sin calcular",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}