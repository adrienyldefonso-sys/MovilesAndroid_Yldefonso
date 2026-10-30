package com.yldefonso.lavacampus.ui.components.graficos

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun GraficoDona(
    datos: List<DatoGrafico>,
    modifier: Modifier = Modifier,
    etiquetaCentro: String = "pedidos",
) {
    val total = datos.sumOf { it.valor.toDouble() }.toFloat()
    val animTotal by animateFloatAsState(
        targetValue = total,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "totalDonaAnim"
    )
    val vacio = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box(Modifier.size(130.dp), contentAlignment = Alignment.Center) {
            Canvas(Modifier.fillMaxSize()) {
                val grosor = 20.dp.toPx()
                val diametro = size.minDimension - grosor
                val origen = Offset(grosor / 2f, grosor / 2f)
                val tam = Size(diametro, diametro)

                // Background track
                drawArc(
                    color = vacio,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = origen,
                    size = tam,
                    style = Stroke(grosor, cap = StrokeCap.Round)
                )

                if (total > 0f) {
                    var inicio = -90f
                    datos.forEach {
                        val barrido = (it.valor / total) * 360f
                        if (barrido > 0f) {
                            drawArc(
                                color = it.color,
                                startAngle = inicio,
                                sweepAngle = (barrido - 3f).coerceAtLeast(1f),
                                useCenter = false,
                                topLeft = origen,
                                size = tam,
                                style = Stroke(grosor, cap = StrokeCap.Round)
                            )
                        }
                        inicio += barrido
                    }
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = animTotal.toInt().toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = etiquetaCentro,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            datos.forEach { d ->
                val pct = if (total > 0f) ((d.valor / total) * 100).toInt() else 0
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        Modifier
                            .size(10.dp)
                            .background(d.color, CircleShape)
                    )
                    Text(
                        text = d.etiqueta,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    if ((total > 0f) && (pct > 0)) {
                        Surface(
                            shape = CircleShape,
                            color = d.color.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "$pct%",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = d.color
                            )
                        }
                    }
                    Text(
                        text = d.valor.toInt().toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
