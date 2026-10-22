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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yldefonso.lavacampus.model.EstadoPedido

private val etapas = listOf(
    EstadoPedido.RECIBIDO, EstadoPedido.EN_PROCESO, EstadoPedido.LISTO, EstadoPedido.ENTREGADO
)

/**
 * Línea de seguimiento dibujada con Canvas. Horizontal (Inicio) o vertical (Detalle).
 * Los colores se pueden cambiar para usarla sobre un fondo de color; [colorCheck] != null
 * dibuja un check dentro de las etapas ya alcanzadas.
 */
@Composable
fun StepperEstado(
    estado: EstadoPedido,
    modifier: Modifier = Modifier,
    vertical: Boolean = false,
    colorActivo: Color = MaterialTheme.colorScheme.primary,
    colorInactivo: Color = MaterialTheme.colorScheme.outlineVariant,
    colorTexto: Color = Color.Unspecified,
    colorTextoInactivo: Color = Color.Unspecified,
    colorCheck: Color? = null
) {
    val actual = if (estado == EstadoPedido.CANCELADO) -1 else etapas.indexOf(estado)
    val txtActivo = if (colorTexto != Color.Unspecified) colorTexto else MaterialTheme.colorScheme.onSurface
    val txtInactivo = if (colorTextoInactivo != Color.Unspecified) colorTextoInactivo else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)

    if (vertical) {
        val alto = 48.dp
        Row(modifier) {
            Canvas(Modifier.width(28.dp).height(alto * etapas.size)) {
                val h = alto.toPx()
                val cx = size.width / 2f
                val radio = 9.dp.toPx()

                for (i in 0 until etapas.size - 1) {
                    drawLine(
                        color = if (i < actual) colorActivo else colorInactivo,
                        start = Offset(cx, h * i + h / 2f),
                        end = Offset(cx, h * (i + 1) + h / 2f),
                        strokeWidth = 3.dp.toPx()
                    )
                }
                etapas.indices.forEach { i ->
                    val alcanzada = i <= actual
                    val center = Offset(cx, h * i + h / 2f)

                    drawCircle(
                        color = if (alcanzada) colorActivo else colorInactivo,
                        radius = radio,
                        center = center
                    )

                    if (alcanzada && colorCheck != null) {
                        val check = Path().apply {
                            moveTo(center.x - radio * 0.45f, center.y)
                            lineTo(center.x - radio * 0.1f, center.y + radio * 0.38f)
                            lineTo(center.x + radio * 0.5f, center.y - radio * 0.35f)
                        }
                        drawPath(
                            path = check,
                            color = colorCheck,
                            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                        )
                    }
                }
            }
            Column {
                etapas.forEachIndexed { i, e ->
                    Box(Modifier.height(alto), contentAlignment = Alignment.CenterStart) {
                        Text(
                            text = e.etiqueta,
                            fontWeight = if (i == actual) FontWeight.Bold else FontWeight.Normal,
                            color = if (i <= actual) txtActivo else txtInactivo,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                }
            }
        }
    } else {
        Column(modifier.fillMaxWidth()) {
            Canvas(Modifier.fillMaxWidth().height(28.dp)) {
                val w = size.width
                val cy = size.height / 2f
                val radio = 10.dp.toPx()
                fun cx(i: Int) = w * (2 * i + 1) / 8f

                for (i in 0 until etapas.size - 1) {
                    drawLine(
                        color = if (i < actual) colorActivo else colorInactivo,
                        start = Offset(cx(i), cy),
                        end = Offset(cx(i + 1), cy),
                        strokeWidth = 3.dp.toPx()
                    )
                }
                etapas.indices.forEach { i ->
                    val alcanzada = i <= actual
                    val center = Offset(cx(i), cy)

                    drawCircle(
                        color = if (alcanzada) colorActivo else colorInactivo,
                        radius = radio,
                        center = center
                    )
                    if (alcanzada && colorCheck != null) {
                        val check = Path().apply {
                            moveTo(center.x - radio * 0.45f, center.y)
                            lineTo(center.x - radio * 0.1f, center.y + radio * 0.38f)
                            lineTo(center.x + radio * 0.5f, center.y - radio * 0.35f)
                        }
                        drawPath(
                            path = check,
                            color = colorCheck,
                            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                        )
                    }
                }
            }
            Row(Modifier.fillMaxWidth()) {
                etapas.forEachIndexed { i, e ->
                    Text(
                        text = e.etiqueta,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (i == actual) FontWeight.Bold else FontWeight.Normal,
                        color = if (i <= actual) txtActivo else txtInactivo,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}