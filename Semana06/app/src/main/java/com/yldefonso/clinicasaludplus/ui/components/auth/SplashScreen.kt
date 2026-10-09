package com.yldefonso.clinicasaludplus.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.scale
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

// Paleta oficial de la pantalla de bienvenida
private val AzulPrimario = Color(0xFF2F6BEA)
private val AzulMarino = Color(0xFF1B3C8C)
private val TextoGris = Color(0xFF6B7690)
private val FondoSuperior = Color(0xFFEBF3FF)
private val FondoMedio = Color(0xFFF6FAFF)

@Composable
fun SplashScreen(navController: NavController) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // 1. LOGO DE LA CLÍNICA
            if (logoPng != null) {
                Image(
                    painter = logoPng,
                    contentDescription = "Logo SaludPlus",
                    modifier = Modifier.size(92.dp)
                )
            } else {
                LogoSaludPlus(modifier = Modifier.size(92.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 2. TÍTULOS Y SUBTÍTULO
            Text(
                text = "Clínica",
                style = TextStyle(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulMarino,
                    letterSpacing = (-0.2).sp
                )
            )
            Text(
                text = "SaludPlus",
                style = TextStyle(
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-1.2).sp,
                    brush = Brush.horizontalGradient(
                        listOf(Color(0xFF1B3C8C), Color(0xFF2F6BEA), Color(0xFF4286F5))
                    )
                )
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Tu salud, nuestra prioridad",
                fontSize = 16.sp,
                letterSpacing = 0.3.sp,
                color = TextoGris
            )

            // 3. ILUSTRACIÓN DEL MÉDICO
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                if (doctorPng != null) {
                    Image(
                        painter = doctorPng,
                        contentDescription = "Médico SaludPlus",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 4. BOTÓN "COMENZAR" (AMPLIADO Y CON TEXTO MÁS GRANDE)
            Button(
                onClick = {
                    navController.navigate(Rutas.Registro.ruta) {
                        popUpTo(Rutas.Splash.ruta) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulPrimario,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Comenzar",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.4.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 5. ENLACE "YA TENGO UNA CUENTA" (MÁS GRANDE Y REUBICADO MÁS ARRIBA)
            Text(
                text = "Ya tengo una cuenta",
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.3.sp,
                color = AzulPrimario,
                modifier = Modifier
                    .clickable {
                        navController.navigate(Rutas.Login.ruta) {
                            popUpTo(Rutas.Splash.ruta) { inclusive = true }
                        }
                    }
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/** Carga de imagen desde res/drawable **/
@Composable
private fun painterDrawableOpcional(nombre: String): Painter? {
    val context = LocalContext.current
    val id = remember(nombre) {
        context.resources.getIdentifier(nombre, "drawable", context.packageName)
    }
    return if (id != 0) painterResource(id) else null
}

/** Logo vectorial de respaldo para la Cruz con Corazón **/
@Composable
private fun LogoSaludPlus(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.aspectRatio(1f)) {
        scale(scale = size.width / 100f, pivot = Offset.Zero) {
            val degradado = Brush.verticalGradient(
                colors = listOf(Color(0xFF4291F7), Color(0xFF2F6BEA))
            )
            drawRoundRect(
                brush = degradado,
                topLeft = Offset(0f, 32f),
                size = Size(100f, 36f),
                cornerRadius = CornerRadius(16f, 16f)
            )
            drawRoundRect(
                brush = degradado,
                topLeft = Offset(32f, 0f),
                size = Size(36f, 100f),
                cornerRadius = CornerRadius(16f, 16f)
            )
            val corazon = Path().apply {
                moveTo(50f, 70f)
                cubicTo(34f, 58f, 29f, 48f, 35f, 40f)
                cubicTo(41f, 34f, 47f, 36f, 50f, 43f)
                cubicTo(53f, 36f, 59f, 34f, 65f, 40f)
                cubicTo(71f, 48f, 66f, 58f, 50f, 70f)
                close()
            }
            drawPath(corazon, Color.White)
        }
    }
}