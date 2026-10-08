package com.yldefonso.clinicasaludplus.ui.components.perfil

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yldefonso.clinicasaludplus.navigation.Rutas
import com.yldefonso.clinicasaludplus.repository.Repositorio

// Paleta de colores unificada
private val AzulPrimario = Color(0xFF2F6BEA)
private val TextoOscuro = Color(0xFF1B2540)
private val TextoGris = Color(0xFF6B7690)
private val FondoPantalla = Color(0xFFF8FAFC)
private val BordeCard = Color(0xFFE2E8F0)
private val FondoIcono = Color(0xFFEAF1FF)
private val VerdeFondo = Color(0xFFF0FDF4)
private val VerdeTexto = Color(0xFF166534)
private val RojoRojo = Color(0xFFDC2626)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(navController: NavController) {
    val usuario = Repositorio.usuarioActual
    val iniciales = obtenerIniciales(usuario?.nombre)

    Scaffold(
        containerColor = FondoPantalla,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Perfil de Usuario",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = TextoOscuro
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = FondoPantalla
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // 1. AVATAR CIRCULAR CON INICIALES DEL NOMBRE Y APELLIDO
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(FondoIcono),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = iniciales,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AzulPrimario
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. NOMBRE Y CORREO PRINCIPAL
            Text(
                text = usuario?.nombre ?: "Usuario",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextoOscuro
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = usuario?.correo ?: "sin_correo@saludplus.com",
                fontSize = 14.sp,
                color = TextoGris
            )

            Spacer(modifier = Modifier.height(12.dp))

            // INSIGNIA "PACIENTE VERIFICADO"
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(VerdeFondo)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verificado",
                        tint = VerdeTexto,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Paciente verificado",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdeTexto
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 3. TARJETA DE DATOS PERSONALES
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BordeCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Información Personal",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoOscuro
                    )

                    HorizontalDivider(thickness = 1.dp, color = BordeCard)

                    ItemPerfilFila(
                        icono = Icons.Default.Person,
                        etiqueta = "Nombres y apellidos",
                        valor = usuario?.nombre ?: "No registrado"
                    )

                    ItemPerfilFila(
                        icono = Icons.Default.Email,
                        etiqueta = "Correo electrónico",
                        valor = usuario?.correo ?: "No registrado"
                    )

                    ItemPerfilFila(
                        icono = Icons.Default.Phone,
                        etiqueta = "Teléfono de contacto",
                        valor = usuario?.telefono ?: "No registrado"
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 4. BOTÓN CERRAR SESIÓN (ROJO SÓLIDO Y LETRAS DESTACADAS)
            Button(
                onClick = {
                    Repositorio.cerrarSesion()
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(0)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RojoRojo,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Cerrar Sesión",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.3.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

/**
 * Función que extrae las dos primeras iniciales (Nombre y Apellido).
 * Ejemplo: "Becker Yldefonso" -> "BY"
 */
private fun obtenerIniciales(nombre: String?): String {
    if (nombre.isNullOrBlank()) return "U"
    val partes = nombre.trim().split("\\s+".toRegex())
    return when {
        partes.size >= 2 -> "${partes[0].first().uppercaseChar()}${partes[1].first().uppercaseChar()}"
        partes.isNotEmpty() && partes[0].isNotEmpty() -> "${partes[0].first().uppercaseChar()}"
        else -> "U"
    }
}

@Composable
private fun ItemPerfilFila(
    icono: ImageVector,
    etiqueta: String,
    valor: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(FondoIcono),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = etiqueta,
                tint = AzulPrimario,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = etiqueta,
                fontSize = 12.sp,
                color = TextoGris
            )
            Text(
                text = valor,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextoOscuro
            )
        }
    }
}