package com.yldefonso.lavacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun GraficoBarras(datos: List<DatoGrafico>, modifier: Modifier = Modifier) {
    val maximo = datos.maxOfOrNull { it.valor } ?: 0f

    Column(modifier.fillMaxWidth()) {
        Canvas(Modifier.fillMaxWidth().height(140.dp)) {
            val n = datos.size.coerceAtLeast(1)
            val ancho = size.width / n
            val barra = ancho * 0.5f
            datos.forEachIndexed { i, d ->
                val alto = if (maximo > 0f) d.valor / maximo * size.height else 0f
                drawRoundRect(
                    color = d.color,
                    topLeft = Offset(ancho * i + (ancho - barra) / 2f, size.height - alto),
                    size = Size(barra, alto),
                    cornerRadius = CornerRadius(8.dp.toPx())
                )
            }
        }
        Row(Modifier.fillMaxWidth()) {
            datos.forEach {
                Text(
                    text = it.etiqueta,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
