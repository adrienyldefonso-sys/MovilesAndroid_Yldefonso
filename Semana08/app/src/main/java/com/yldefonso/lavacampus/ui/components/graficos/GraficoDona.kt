package com.yldefonso.lavacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun GraficoDona(datos: List<DatoGrafico>, modifier: Modifier = Modifier) {
    val total = datos.sumOf { it.valor.toDouble() }.toFloat()
    val vacio = MaterialTheme.colorScheme.outlineVariant

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.size(160.dp), contentAlignment = Alignment.Center) {
            Canvas(Modifier.fillMaxSize()) {
                val grosor = 28.dp.toPx()
                val diametro = size.minDimension - grosor
                val origen = Offset(grosor / 2f, grosor / 2f)
                val tam = Size(diametro, diametro)
                if (total <= 0f) {
                    drawArc(color = vacio, startAngle = 0f, sweepAngle = 360f, useCenter = false,
                        topLeft = origen, size = tam, style = Stroke(grosor))
                } else {
                    var inicio = -90f
                    datos.forEach {
                        val barrido = it.valor / total * 360f
                        drawArc(color = it.color, startAngle = inicio, sweepAngle = barrido, useCenter = false,
                            topLeft = origen, size = tam, style = Stroke(grosor))
                        inicio += barrido
                    }
                }
            }
            Text(
                text = total.toInt().toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(8.dp))
        datos.forEach {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(10.dp).background(it.color, CircleShape))
                Spacer(Modifier.width(8.dp))
                Text("${it.etiqueta}: ${it.valor.toInt()}", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}
