package com.yldefonso.lavacampus.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yldefonso.lavacampus.model.EstadoPedido

private val etapas = listOf(
    EstadoPedido.RECIBIDO, EstadoPedido.EN_PROCESO, EstadoPedido.LISTO, EstadoPedido.ENTREGADO
)

/** Línea de seguimiento dibujada con Canvas. Horizontal (Inicio) o vertical (Detalle). */
@Composable
fun StepperEstado(
    estado: EstadoPedido,
    modifier: Modifier = Modifier,
    vertical: Boolean = false
) {
    val activo = MaterialTheme.colorScheme.primary
    val inactivo = MaterialTheme.colorScheme.outlineVariant
    val actual = if (estado == EstadoPedido.CANCELADO) -1 else etapas.indexOf(estado)

    if (vertical) {
        val alto = 56.dp
        Row(modifier) {
            Canvas(Modifier.width(28.dp).height(alto * etapas.size)) {
                val h = alto.toPx()
                val cx = size.width / 2f
                for (i in 0 until etapas.size - 1) {
                    drawLine(
                        color = if (i < actual) activo else inactivo,
                        start = Offset(cx, h * i + h / 2f),
                        end = Offset(cx, h * (i + 1) + h / 2f),
                        strokeWidth = 3.dp.toPx()
                    )
                }
                etapas.indices.forEach { i ->
                    drawCircle(
                        color = if (i <= actual) activo else inactivo,
                        radius = 8.dp.toPx(),
                        center = Offset(cx, h * i + h / 2f)
                    )
                }
            }
            Column {
                etapas.forEachIndexed { i, e ->
                    Box(Modifier.height(alto), contentAlignment = Alignment.CenterStart) {
                        Text(
                            text = e.etiqueta,
                            fontWeight = if (i == actual) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                }
            }
        }
    } else {
        Column(modifier.fillMaxWidth()) {
            Canvas(Modifier.fillMaxWidth().height(24.dp)) {
                val w = size.width
                val cy = size.height / 2f
                fun cx(i: Int) = w * (2 * i + 1) / 8f
                for (i in 0 until etapas.size - 1) {
                    drawLine(
                        color = if (i < actual) activo else inactivo,
                        start = Offset(cx(i), cy),
                        end = Offset(cx(i + 1), cy),
                        strokeWidth = 3.dp.toPx()
                    )
                }
                etapas.indices.forEach { i ->
                    drawCircle(
                        color = if (i <= actual) activo else inactivo,
                        radius = 8.dp.toPx(),
                        center = Offset(cx(i), cy)
                    )
                }
            }
            Row(Modifier.fillMaxWidth()) {
                etapas.forEach {
                    Text(
                        text = it.etiqueta,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
