package com.yldefonso.clinicasaludplus.ui

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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import com.yldefonso.clinicasaludplus.model.Usuario
import com.yldefonso.clinicasaludplus.navigation.Rutas
import com.yldefonso.clinicasaludplus.repository.Repositorio

// Paleta (Pantalla 2 de la maqueta)
private val AzulPrimario = Color(0xFF2F6BEA)
private val TextoOscuro = Color(0xFF1B2540)
private val TextoGris = Color(0xFF6B7690)
private val PlaceholderGris = Color(0xFFB4BCCB)
private val CampoBorde = Color(0xFFE1E7F3)
private val FondoIcono = Color(0xFFEEF3FC)

@Composable
fun RegistroScreen(navController: NavController) {
    val context = LocalContext.current
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
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
            // Contenido con scroll y distribución vertical proporcionada
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                // Encabezado
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Crear cuenta",
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.4).sp,
                            color = TextoOscuro
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Regístrate para agendar tus citas",
                        fontSize = 14.sp,
                        letterSpacing = 0.2.sp,
                        color = TextoGris
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Campos de entrada
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    CampoFormulario(
                        valor = nombre,
                        alCambiar = { nombre = it; errorMensaje = "" },
                        etiqueta = "Nombres y apellidos",
                        placeholder = "Juan Pérez",
                        icono = Icons.Default.Person,
                        tipoTeclado = KeyboardType.Text
                    )

                    CampoFormulario(
                        valor = telefono,
                        alCambiar = { telefono = it; errorMensaje = "" },
                        etiqueta = "Teléfono",
                        placeholder = "987 654 321",
                        icono = Icons.Default.Phone,
                        tipoTeclado = KeyboardType.Phone
                    )

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
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMensaje,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón "Registrarme" de mayor presencia y Términos destacados
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            when {
                                nombre.isBlank() || correo.isBlank() || contrasena.isBlank() -> {
                                    errorMensaje = "Todos los campos son obligatorios"
                                }
                                !esCorreoValido(correo) -> {
                                    errorMensaje = "Formato de correo electrónico inválido"
                                }
                                else -> {
                                    val nuevoUsuario = Usuario(
                                        id = 0,
                                        nombre = nombre,
                                        telefono = telefono,
                                        correo = correo,
                                        contrasena = contrasena
                                    )
                                    if (Repositorio.registrarUsuario(nuevoUsuario)) {
                                        Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                        navController.navigate(Rutas.Home.ruta) {
                                            popUpTo(Rutas.Registro.ruta) { inclusive = true }
                                        }
                                    } else {
                                        errorMensaje = "El correo ya se encuentra registrado"
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AzulPrimario,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Registrarme",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.3.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Al registrarte aceptas nuestros",
                        fontSize = 13.sp,
                        color = TextoGris,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Términos y Condiciones",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulPrimario,
                        modifier = Modifier
                            .clickable { navController.navigate(Rutas.Terminos.ruta) }
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Pie de página fijo inferior
            HorizontalDivider(thickness = 1.dp, color = CampoBorde)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Ya tienes cuenta? ",
                    fontSize = 14.sp,
                    color = TextoGris
                )
                Text(
                    text = "Iniciar sesión",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulPrimario,
                    modifier = Modifier.clickable {
                        navController.navigate(Rutas.Login.ruta)
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