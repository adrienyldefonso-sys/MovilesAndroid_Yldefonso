package com.yldefonso.lavacampus.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Dibuja con Canvas la lavadora ("wash"), la plancha ("iron") o la camisa ("shirt"). */
@Composable
fun IconoServicio(
    icono: String,
    modifier: Modifier = Modifier,
    tamano: Dp = 40.dp,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Canvas(modifier.size(tamano)) {
        val w = size.width
        val h = size.height
        val trazo = Stroke(width = w * 0.07f, cap = StrokeCap.Round, join = StrokeJoin.Round)

        when (icono) {
            "wash" -> {
                drawRoundRect(
                    color = color, topLeft = Offset(w * 0.12f, h * 0.08f),
                    size = Size(w * 0.76f, h * 0.84f),
                    cornerRadius = CornerRadius(w * 0.1f), style = trazo
                )
                drawCircle(color = color, radius = w * 0.24f, center = Offset(w * 0.5f, h * 0.58f), style = trazo)
                drawCircle(color = color, radius = w * 0.04f, center = Offset(w * 0.26f, h * 0.2f))
                drawCircle(color = color, radius = w * 0.04f, center = Offset(w * 0.4f, h * 0.2f))
            }
            "iron" -> {
                val cuerpo = Path().apply {
                    moveTo(w * 0.08f, h * 0.8f)
                    lineTo(w * 0.92f, h * 0.8f)
                    lineTo(w * 0.84f, h * 0.5f)
                    lineTo(w * 0.62f, h * 0.32f)
                    lineTo(w * 0.08f, h * 0.32f)
                    close()
                }
                drawPath(cuerpo, color = color, style = trazo)
                val asa = Path().apply {
                    moveTo(w * 0.24f, h * 0.32f)
                    lineTo(w * 0.3f, h * 0.16f)
                    lineTo(w * 0.58f, h * 0.16f)
                    lineTo(w * 0.62f, h * 0.32f)
                }
                drawPath(asa, color = color, style = trazo)
            }
            else -> { // "shirt"
                val camisa = Path().apply {
                    moveTo(w * 0.3f, h * 0.12f)
                    lineTo(w * 0.42f, h * 0.12f)
                    lineTo(w * 0.5f, h * 0.22f)
                    lineTo(w * 0.58f, h * 0.12f)
                    lineTo(w * 0.7f, h * 0.12f)
                    lineTo(w * 0.95f, h * 0.32f)
                    lineTo(w * 0.82f, h * 0.48f)
                    lineTo(w * 0.72f, h * 0.4f)
                    lineTo(w * 0.72f, h * 0.88f)
                    lineTo(w * 0.28f, h * 0.88f)
                    lineTo(w * 0.28f, h * 0.4f)
                    lineTo(w * 0.18f, h * 0.48f)
                    lineTo(w * 0.05f, h * 0.32f)
                    close()
                }
                drawPath(camisa, color = color.copy(alpha = 0.15f))
                drawPath(camisa, color = color, style = trazo)
            }
        }
    }
}
