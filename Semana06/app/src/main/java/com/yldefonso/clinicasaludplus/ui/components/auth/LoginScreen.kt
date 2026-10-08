package com.yldefonso.clinicasaludplus.ui.components.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yldefonso.clinicasaludplus.navigation.Rutas
import com.yldefonso.clinicasaludplus.repository.Repositorio

// Paleta de colores idéntica a RegistroScreen
private val AzulPrimario = Color(0xFF2F6BEA)
private val TextoOscuro = Color(0xFF1B2540)
private val TextoGris = Color(0xFF6B7690)
private val PlaceholderGris = Color(0xFFB4BCCB)
private val CampoBorde = Color(0xFFE1E7F3)
private val FondoIcono = Color(0xFFEEF3FC)

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var errorMensaje by remember { mutableStateOf("") }

    fun esCorreoValido(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    Scaffold(containerColor = Color.White) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) {
            // CONTENEDOR PRINCIPAL CENTRADO VERTICALMENTE EN LA PANTALLA
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // 1. ENCABEZADO
                    Text(
                        text = "Iniciar sesión",
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.4).sp,
                            color = TextoOscuro
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Ingresa tus datos para continuar",
                        fontSize = 14.sp,
                        letterSpacing = 0.2.sp,
                        color = TextoGris
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // 2. CAMPOS DE ENTRADA (AGRUPADOS)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CampoFormulario(
                            valor = correo,
                            alCambiar = { correo = it; errorMensaje = "" },
                            etiqueta = "Correo electrónico",
                            placeholder = "juan@correo.com",
                            icono = Icons.Default.Email,
                            tipoTeclado = KeyboardType.Email
                        )

                        CampoFormulario(
                            valor = contrasena,
                            alCambiar = { contrasena = it; errorMensaje = "" },
                            etiqueta = "Contraseña",
                            placeholder = "••••••••",
                            icono = Icons.Default.Lock,
                            tipoTeclado = KeyboardType.Password,
                            esPassword = true
                        )
                    }

                    if (errorMensaje.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = errorMensaje,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // 3. BOTÓN PRINCIPAL
                    Button(
                        onClick = {
                            when {
                                correo.isBlank() || contrasena.isBlank() -> {
                                    errorMensaje = "Todos los campos son obligatorios"
                                }
                                !esCorreoValido(correo) -> {
                                    errorMensaje = "Formato de correo electrónico inválido"
                                }
                                !Repositorio.iniciarSesion(correo, contrasena) -> {
                                    errorMensaje = "Correo o contraseña incorrectos"
                                }
                                else -> {
                                    Toast.makeText(context, "¡Bienvenido!", Toast.LENGTH_SHORT).show()
                                    navController.navigate(Rutas.Home.ruta) {
                                        popUpTo(Rutas.Login.ruta) { inclusive = true }
                                    }
                                }
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
                            text = "Ingresar",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.3.sp
                        )
                    }
                }
            }

            // PIE DE PÁGINA FIJO AL FONDO
            HorizontalDivider(thickness = 1.dp, color = CampoBorde)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿No tienes cuenta? ",
                    fontSize = 14.sp,
                    color = TextoGris
                )
                Text(
                    text = "Regístrate aquí",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulPrimario,
                    modifier = Modifier.clickable {
                        navController.navigate(Rutas.Registro.ruta)
                    }
                )
            }
        }
    }
}

/**
 * Campo estilo maqueta: a la izquierda el ícono en un cuadrado suave,
 * a la derecha la etiqueta pegada justo arriba de la caja de entrada de texto.
 */
@Composable
private fun CampoFormulario(
    valor: String,
    alCambiar: (String) -> Unit,
    etiqueta: String,
    placeholder: String,
    icono: ImageVector,
    tipoTeclado: KeyboardType,
    esPassword: Boolean = false
) {
    val formaTarjeta = RoundedCornerShape(12.dp)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom
    ) {
        // Cuadrado del ícono
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(formaTarjeta)
                .background(FondoIcono),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = etiqueta,
                tint = AzulPrimario,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            // Etiqueta pegada al cuadro de texto
            Text(
                text = etiqueta,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextoGris,
                modifier = Modifier.padding(start = 2.dp, bottom = 2.dp)
            )

            // Caja de entrada
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(formaTarjeta)
                    .background(Color.White)
                    .border(1.dp, CampoBorde, formaTarjeta)
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (valor.isEmpty()) {
                    Text(
                        text = placeholder,
                        fontSize = 14.sp,
                        color = PlaceholderGris
                    )
                }
                BasicTextField(
                    value = valor,
                    onValueChange = alCambiar,
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextoOscuro
                    ),
                    cursorBrush = SolidColor(AzulPrimario),
                    keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado),
                    visualTransformation = if (esPassword) PasswordVisualTransformation() else VisualTransformation.None,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}