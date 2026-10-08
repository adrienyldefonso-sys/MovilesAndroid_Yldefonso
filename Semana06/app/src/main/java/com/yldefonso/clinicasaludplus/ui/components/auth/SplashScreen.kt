package com.yldefonso.clinicasaludplus.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yldefonso.clinicasaludplus.navigation.Rutas

// ---------------------------------------------------------------- Paleta (pantalla 1 de la maqueta)
private val AzulPrimario = Color(0xFF2F6BEA)
private val AzulMarino = Color(0xFF1B3C8C)
private val TextoGris = Color(0xFF6B7690)
private val FondoSuperior = Color(0xFFE6F0FF)
private val FondoMedio = Color(0xFFF4F8FF)

@Composable
fun SplashScreen(navController: NavController) {
    // Si guardas los PNG originales de la maqueta como res/drawable/logo_saludplus.png
    // y res/drawable/doctor_splash.png, se usan automáticamente. Si no existen, se dibujan en vector.
    val logoPng = painterDrawableOpcional("logo_saludplus")
    val doctorPng = painterDrawableOpcional("doctor_splash")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(FondoSuperior, FondoMedio, Color.White)
                )
            )
    ) {
        Scaffold(containerColor = Color.Transparent) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(36.dp))

                // === LOGO ===
                if (logoPng != null) {
                    Image(
                        painter = logoPng,
                        contentDescription = "Logo SaludPlus",
                        modifier = Modifier.size(72.dp)
                    )
                } else {
                    LogoSaludPlus(modifier = Modifier.size(72.dp))
                }

                Spacer(modifier = Modifier.height(14.dp))

                // === NOMBRE DE LA CLÍNICA ===
                Text(
                    text = "Clínica",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.3).sp,
                        color = AzulMarino
                    )
                )
                Text(
                    text = "SaludPlus",
                    style = TextStyle(
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-1).sp,
                        brush = Brush.horizontalGradient(
                            listOf(Color(0xFF1B3C8C), Color(0xFF2F6BEA), Color(0xFF3C86E8))
                        )
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Tu salud, nuestra prioridad",
                    fontSize = 14.sp,
                    letterSpacing = 0.4.sp,
                    color = TextoGris
                )

                // === ILUSTRACIÓN DEL MÉDICO (ocupa el espacio libre y queda pegada abajo) ===
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    val modificadorIlustracion = Modifier
                        .fillMaxHeight()
                        .aspectRatio(300f / 260f, matchHeightConstraintsFirst = true)

                    if (doctorPng != null) {
                        Image(
                            painter = doctorPng,
                            contentDescription = "Médico SaludPlus",
                            contentScale = ContentScale.Fit,
                            modifier = modificadorIlustracion
                        )
                    } else {
                        IlustracionMedico(modifier = modificadorIlustracion)
                    }
                }

                // === BOTÓN COMENZAR ===
                Button(
                    onClick = {
                        navController.navigate(Rutas.Registro.ruta) {
                            popUpTo(Rutas.Splash.ruta) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulPrimario,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Comenzar",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.4.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // === ENLACE "YA TENGO UNA CUENTA" ===
                Text(
                    text = "Ya tengo una cuenta",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.3.sp,
                    color = AzulPrimario,
                    modifier = Modifier
                        .clickable {
                            navController.navigate(Rutas.Login.ruta) {
                                popUpTo(Rutas.Splash.ruta) { inclusive = true }
                            }
                        }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

/** Devuelve el drawable con ese nombre si existe en res/drawable; si no, null (sin romper la compilación). */
@Composable
private fun painterDrawableOpcional(nombre: String): Painter? {
    val context = LocalContext.current
    val id = remember(nombre) {
        context.resources.getIdentifier(nombre, "drawable", context.packageName)
    }
    return if (id != 0) painterResource(id) else null
}

// ---------------------------------------------------------------- Logo: cruz con corazón
@Composable
private fun LogoSaludPlus(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.aspectRatio(1f)) {
        // Se dibuja en un lienzo de 100 x 100 y se escala al tamaño real
        scale(scale = size.width / 100f, pivot = Offset.Zero) {
            val degradado = Brush.linearGradient(
                colors = listOf(Color(0xFF4DB6F2), Color(0xFF2F6BEA)),
                start = Offset(0f, 0f),
                end = Offset(100f, 100f)
            )
            // Brazos de la cruz
            drawRoundRect(
                brush = degradado,
                topLeft = Offset(0f, 32f),
                size = Size(100f, 36f),
                cornerRadius = CornerRadius(14f, 14f)
            )
            drawRoundRect(
                brush = degradado,
                topLeft = Offset(32f, 0f),
                size = Size(36f, 100f),
                cornerRadius = CornerRadius(14f, 14f)
            )
            // Corazón blanco
            val corazon = Path().apply {
                moveTo(50f, 72f)
                cubicTo(33f, 60f, 28f, 49f, 34f, 41f)
                cubicTo(40f, 35f, 47f, 37f, 50f, 44f)
                cubicTo(53f, 37f, 60f, 35f, 66f, 41f)
                cubicTo(72f, 49f, 67f, 60f, 50f, 72f)
                close()
            }
            drawPath(corazon, Color.White)
        }
    }
}

// ---------------------------------------------------------------- Ilustración: médico con plantas
private fun DrawScope.relleno(color: Color, trazo: Path.() -> Unit) {
    drawPath(Path().apply(trazo), color)
}

private fun DrawScope.contorno(color: Color, ancho: Float, trazo: Path.() -> Unit) {
    drawPath(
        path = Path().apply(trazo),
        color = color,
        style = Stroke(width = ancho, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
}

@Composable
private fun IlustracionMedico(modifier: Modifier = Modifier) {
    val piel = Color(0xFFF3C9A5)
    val bata = Color.White
    val bordeBata = Color(0xFFD5E0F0)
    val cabello = Color(0xFF1E2430)

    Canvas(modifier = modifier.clipToBounds()) {
        // Lienzo de diseño: 300 x 260. Todo se dibuja en coordenadas de 300 x 330 y se sube 70.
        scale(scale = size.width / 300f, pivot = Offset.Zero) {
            translate(left = 0f, top = -70f) {

                // Halo suave detrás del médico
                drawCircle(
                    color = Color(0xFFD6E8FF).copy(alpha = 0.7f),
                    radius = 135f,
                    center = Offset(150f, 205f)
                )

                // Plantas (izquierda)
                relleno(Color(0xFF2C8F8C)) {
                    moveTo(45f, 325f); quadraticBezierTo(21f, 270f, 20f, 215f)
                    quadraticBezierTo(57f, 270f, 45f, 325f); close()
                }
                relleno(Color(0xFF4FB8AE)) {
                    moveTo(50f, 325f); quadraticBezierTo(82f, 263f, 70f, 205f)
                    quadraticBezierTo(40f, 262f, 50f, 325f); close()
                }
                relleno(Color(0xFF1F7479)) {
                    moveTo(40f, 328f); quadraticBezierTo(8f, 296f, 8f, 255f)
                    quadraticBezierTo(42f, 296f, 40f, 328f); close()
                }
                relleno(Color(0xFF6BCBBE)) {
                    moveTo(55f, 328f); quadraticBezierTo(92f, 290f, 100f, 250f)
                    quadraticBezierTo(66f, 280f, 55f, 328f); close()
                }
                // Plantas (derecha)
                relleno(Color(0xFF2C8F8C)) {
                    moveTo(255f, 325f); quadraticBezierTo(279f, 270f, 280f, 215f)
                    quadraticBezierTo(243f, 270f, 255f, 325f); close()
                }
                relleno(Color(0xFF4FB8AE)) {
                    moveTo(250f, 325f); quadraticBezierTo(218f, 263f, 230f, 205f)
                    quadraticBezierTo(260f, 262f, 250f, 325f); close()
                }
                relleno(Color(0xFF1F7479)) {
                    moveTo(260f, 328f); quadraticBezierTo(292f, 296f, 292f, 255f)
                    quadraticBezierTo(258f, 296f, 260f, 328f); close()
                }
                relleno(Color(0xFF6BCBBE)) {
                    moveTo(245f, 328f); quadraticBezierTo(208f, 290f, 200f, 250f)
                    quadraticBezierTo(234f, 280f, 245f, 328f); close()
                }

                // Bata blanca
                val cuerpo = Path().apply {
                    moveTo(78f, 330f); lineTo(82f, 262f)
                    quadraticBezierTo(84f, 238f, 110f, 230f)
                    lineTo(190f, 230f)
                    quadraticBezierTo(216f, 238f, 218f, 262f)
                    lineTo(222f, 330f); close()
                }
                drawPath(cuerpo, bata)
                drawPath(cuerpo, bordeBata, style = Stroke(width = 2f))

                // Camisa, corbata y solapas
                relleno(Color(0xFF5B9BD5)) {
                    moveTo(128f, 226f); lineTo(172f, 226f); lineTo(150f, 292f); close()
                }
                relleno(Color(0xFF1F4E79)) {
                    moveTo(146f, 232f); lineTo(154f, 232f); lineTo(157f, 262f)
                    lineTo(150f, 276f); lineTo(143f, 262f); close()
                }
                contorno(bordeBata, 2f) { moveTo(122f, 228f); lineTo(146f, 300f) }
                contorno(bordeBata, 2f) { moveTo(178f, 228f); lineTo(154f, 300f) }

                // Cuello
                drawRoundRect(
                    color = piel,
                    topLeft = Offset(138f, 198f),
                    size = Size(24f, 36f),
                    cornerRadius = CornerRadius(8f, 8f)
                )

                // Estetoscopio
                contorno(Color(0xFF3C4A5C), 4f) {
                    moveTo(126f, 230f); cubicTo(112f, 262f, 118f, 292f, 140f, 296f)
                }
                contorno(Color(0xFF3C4A5C), 4f) {
                    moveTo(174f, 230f); cubicTo(184f, 252f, 178f, 270f, 165f, 278f)
                }
                drawCircle(Color(0xFFB8C4D4), radius = 8f, center = Offset(140f, 298f))
                drawCircle(
                    Color(0xFF3C4A5C), radius = 8f, center = Offset(140f, 298f),
                    style = Stroke(width = 2f)
                )

                // Brazo levantado (señalando con el dedo)
                contorno(bordeBata, 30f) {
                    moveTo(206f, 252f); lineTo(232f, 284f); lineTo(238f, 236f)
                }
                contorno(bata, 26f) {
                    moveTo(206f, 252f); lineTo(232f, 284f); lineTo(238f, 236f)
                }
                drawCircle(piel, radius = 11f, center = Offset(239f, 222f))
                drawRoundRect(
                    color = piel,
                    topLeft = Offset(235f, 190f),
                    size = Size(8f, 34f),
                    cornerRadius = CornerRadius(4f, 4f)
                )
                drawCircle(piel, radius = 5f, center = Offset(229f, 226f))
                drawCircle(piel, radius = 6f, center = Offset(247f, 226f))

                // Portapapeles
                rotate(degrees = -10f, pivot = Offset(185f, 300f)) {
                    drawRoundRect(
                        color = Color(0xFF2F6BEA),
                        topLeft = Offset(150f, 268f),
                        size = Size(70f, 62f),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(156f, 278f),
                        size = Size(58f, 46f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                    drawRoundRect(
                        color = Color(0xFF1F4FBF),
                        topLeft = Offset(172f, 262f),
                        size = Size(26f, 10f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                    contorno(bordeBata, 3f) { moveTo(162f, 290f); lineTo(206f, 290f) }
                    contorno(bordeBata, 3f) { moveTo(162f, 300f); lineTo(206f, 300f) }
                    contorno(bordeBata, 3f) { moveTo(162f, 310f); lineTo(192f, 310f) }
                }

                // Brazo que sostiene el portapapeles
                contorno(bordeBata, 30f) {
                    moveTo(92f, 258f); cubicTo(80f, 290f, 100f, 316f, 150f, 312f)
                }
                contorno(bata, 26f) {
                    moveTo(92f, 258f); cubicTo(80f, 290f, 100f, 316f, 150f, 312f)
                }
                drawCircle(piel, radius = 10f, center = Offset(156f, 310f))

                // Cabeza: orejas, cara, cabello
                drawCircle(piel, radius = 8f, center = Offset(111f, 168f))
                drawCircle(piel, radius = 8f, center = Offset(189f, 168f))
                drawOval(piel, topLeft = Offset(112f, 120f), size = Size(76f, 86f))
                relleno(cabello) {
                    moveTo(110f, 160f)
                    quadraticBezierTo(104f, 108f, 150f, 106f)
                    quadraticBezierTo(198f, 108f, 190f, 160f)
                    quadraticBezierTo(186f, 138f, 168f, 132f)
                    quadraticBezierTo(140f, 142f, 120f, 134f)
                    quadraticBezierTo(112f, 142f, 110f, 160f)
                    close()
                }

                // Rostro: ojos, cejas, sonrisa y mejillas
                drawOval(Color(0xFF2B2B2B), topLeft = Offset(133f, 160f), size = Size(7f, 10f))
                drawOval(Color(0xFF2B2B2B), topLeft = Offset(160f, 160f), size = Size(7f, 10f))
                drawLine(cabello, Offset(130f, 153f), Offset(142f, 151f), strokeWidth = 2.5f, cap = StrokeCap.Round)
                drawLine(cabello, Offset(158f, 151f), Offset(170f, 153f), strokeWidth = 2.5f, cap = StrokeCap.Round)
                contorno(Color(0xFFB5543D), 3f) {
                    moveTo(138f, 178f); quadraticBezierTo(150f, 192f, 162f, 178f)
                }
                drawCircle(Color(0xFFF5A38B).copy(alpha = 0.35f), radius = 6f, center = Offset(128f, 178f))
                drawCircle(Color(0xFFF5A38B).copy(alpha = 0.35f), radius = 6f, center = Offset(172f, 178f))
            }
        }
    }
}