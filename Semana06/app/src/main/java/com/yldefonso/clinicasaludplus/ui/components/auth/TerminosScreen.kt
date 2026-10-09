package com.yldefonso.clinicasaludplus.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Paleta de colores unificada
private val AzulPrimario = Color(0xFF2F6BEA)
private val TextoOscuro = Color(0xFF1B2540)
private val TextoGris = Color(0xFF6B7690)
private val FondoPantalla = Color(0xFFF8FAFC)
private val BordeCard = Color(0xFFE2E8F0)
private val FondoIcono = Color(0xFFEAF1FF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminosScreen(navController: NavController) {
    Scaffold(
        containerColor = FondoPantalla,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Términos y Condiciones",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = TextoOscuro
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = TextoOscuro
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = FondoPantalla
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulPrimario,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Entendido / Volver",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.3.sp
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // 1. HEADER CON ÍCONO LEGAL
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BordeCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(FondoIcono),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Gavel,
                            contentDescription = "Términos",
                            tint = AzulPrimario,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Clínica Salud Plus",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AzulPrimario
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Términos del Servicio",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoOscuro
                        )
                        Text(
                            text = "Última actualización: Octubre 2026",
                            fontSize = 12.sp,
                            color = TextoGris
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. SECCIONES NUMERADAS DE LOS TÉRMINOS
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BordeCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    SeccionTermino(
                        numero = "1",
                        titulo = "Aceptación de los Términos",
                        descripcion = "Al registrarse y utilizar la aplicación Clínica Salud Plus, el usuario acepta cumplir con todos los términos y políticas de privacidad establecidas para garantizar una atención médica segura y transparente."
                    )

                    HorizontalDivider(thickness = 1.dp, color = BordeCard)

                    SeccionTermino(
                        numero = "2",
                        titulo = "Uso de Datos Personales",
                        descripcion = "Sus datos de registro (nombre, teléfono y correo electrónico) se utilizarán exclusivamente para la autenticación, gestión de citas y notificaciones médicas dentro del aplicativo."
                    )

                    HorizontalDivider(thickness = 1.dp, color = BordeCard)

                    SeccionTermino(
                        numero = "3",
                        titulo = "Responsabilidad del Usuario",
                        descripcion = "El usuario es responsable de mantener la confidencialidad de sus credenciales de acceso y de ingresar información médica verídica durante el agendamiento."
                    )

                    HorizontalDivider(thickness = 1.dp, color = BordeCard)

                    SeccionTermino(
                        numero = "4",
                        titulo = "Modificaciones del Servicio",
                        descripcion = "La clínica se reserva el derecho de actualizar la aplicación para mejorar la experiencia del usuario, agregando nuevas funcionalidades y optimizando la atención."
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun SeccionTermino(
    numero: String,
    titulo: String,
    descripcion: String
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(FondoIcono),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = numero,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulPrimario
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = titulo,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextoOscuro
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = descripcion,
            fontSize = 13.sp,
            color = TextoGris,
            lineHeight = 19.sp
        )
    }
}