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

// Paleta de colores unificada
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
    var aceptoTerminos by remember { mutableStateOf(false) }
    var errorMensaje by remember { mutableStateOf("") }

    // FUNCIONES DE VALIDACIÓN
    fun esNombreValido(texto: String): Boolean {
        // Solo permite letras (incluyendo acentos y 'ñ') y espacios
        return texto.isNotBlank() && texto.all { it.isLetter() || it.isWhitespace() }
    }

    fun esTelefonoValido(celular: String): Boolean {
        // Debe tener exactamente 9 dígitos numéricos
        return celular.length == 9 && celular.all { it.isDigit() }
    }

    fun esCorreoValido(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    }

    Scaffold(containerColor = Color.White) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) {
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
                    // ENCABEZADO
                    Text(
                        text = "Crear Cuenta",
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.4).sp,
                            color = TextoOscuro
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Regístrate para agendar tus citas médicas",
                        fontSize = 14.sp,
                        letterSpacing = 0.2.sp,
                        color = TextoGris
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // FORMULARIO DE REGISTRO
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        CampoFormularioRegistro(
                            valor = nombre,
                            alCambiar = { nombre = it; errorMensaje = "" },
                            etiqueta = "Nombres y Apellidos",
                            placeholder = "Juan Pérez",
                            icono = Icons.Default.Person,
                            tipoTeclado = KeyboardType.Text
                        )

                        CampoFormularioRegistro(
                            valor = telefono,
                            alCambiar = {
                                if (it.length <= 9) {
                                    telefono = it
                                    errorMensaje = ""
                                }
                            },
                            etiqueta = "Número de celular",
                            placeholder = "987654321",
                            icono = Icons.Default.Phone,
                            tipoTeclado = KeyboardType.Number
                        )

                        CampoFormularioRegistro(
                            valor = correo,
                            alCambiar = { correo = it; errorMensaje = "" },
                            etiqueta = "Correo electrónico",
                            placeholder = "juan@correo.com",
                            icono = Icons.Default.Email,
                            tipoTeclado = KeyboardType.Email
                        )

                        CampoFormularioRegistro(
                            valor = contrasena,
                            alCambiar = { contrasena = it; errorMensaje = "" },
                            etiqueta = "Contraseña",
                            placeholder = "••••••••",
                            icono = Icons.Default.Lock,
                            tipoTeclado = KeyboardType.Password,
                            esPassword = true
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // CHECKBOX Y ENLACE A TÉRMINOS Y CONDICIONES
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = aceptoTerminos,
                            onCheckedChange = {
                                aceptoTerminos = it
                                errorMensaje = ""
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = AzulPrimario,
                                uncheckedColor = CampoBorde
                            )
                        )
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Acepto los ",
                                fontSize = 13.sp,
                                color = TextoGris
                            )
                            Text(
                                text = "Términos y Condiciones",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulPrimario,
                                modifier = Modifier.clickable {
                                    navController.navigate(Rutas.Terminos.ruta)
                                }
                            )
                        }
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

                    Spacer(modifier = Modifier.height(16.dp))

                    // BOTÓN "REGISTRARSE" CON VALIDACIONES ESTRICTAS
                    Button(
                        onClick = {
                            when {
                                nombre.isBlank() || telefono.isBlank() || correo.isBlank() || contrasena.isBlank() -> {
                                    errorMensaje = "Todos los campos son obligatorios"
                                }
                                !esNombreValido(nombre) -> {
                                    errorMensaje = "El nombre solo debe contener letras, no números"
                                }
                                !esTelefonoValido(telefono) -> {
                                    errorMensaje = "El celular debe contener exactamente 9 dígitos numéricos"
                                }
                                !esCorreoValido(correo) -> {
                                    errorMensaje = "Formato de correo electrónico inválido"
                                }
                                contrasena.length < 6 -> {
                                    errorMensaje = "La contraseña debe tener al menos 6 caracteres"
                                }
                                !aceptoTerminos -> {
                                    errorMensaje = "Debes aceptar los Términos y Condiciones"
                                }
                                else -> {
                                    val nuevoUsuario = Usuario(
                                        id = 0,
                                        nombre = nombre.trim(),
                                        telefono = telefono.trim(),
                                        correo = correo.trim(),
                                        contrasena = contrasena
                                    )
                                    val exito = Repositorio.registrarUsuario(nuevoUsuario)
                                    if (exito) {
                                        Toast.makeText(context, "Cuenta creada exitosamente. Inicia sesión", Toast.LENGTH_LONG).show()
                                        navController.navigate(Rutas.Login.ruta) {
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
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AzulPrimario,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Registrarse",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.3.sp
                        )
                    }
                }
            }

            // FOOTER A INICIAR SESIÓN
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
                    text = "Inicia sesión aquí",
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

@Composable
private fun CampoFormularioRegistro(
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
            Text(
                text = etiqueta,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextoGris,
                modifier = Modifier.padding(start = 2.dp, bottom = 2.dp)
            )

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