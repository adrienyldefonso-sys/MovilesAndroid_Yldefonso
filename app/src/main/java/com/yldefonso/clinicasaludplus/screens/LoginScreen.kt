package com.yldefonso.clinicasaludplus.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yldefonso.clinicasaludplus.navigation.Screen
import com.yldefonso.clinicasaludplus.ui.theme.PurpleMid

/**
 * Pantalla de inicio de sesión simple como punto de entrada de la app.
 * No realiza autenticación con backend, solo valida que los campos no estén vacíos.
 */
@Composable
fun LoginScreen(navController: NavController) {
    // Estado local para los campos del formulario
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    // Contenedor principal con fondo lila claro y contenido centrado
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F2FB))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Tarjeta blanca redondeada para el formulario de login
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título y subtítulo de la app
                Text(
                    text = "Clínica Salud+",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = PurpleMid
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Accede a tu cuenta de paciente",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Campo para ingreso de correo electrónico con ícono
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Correo",
                            tint = PurpleMid
                        )
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo para ingreso de contraseña con ícono y ocultación visual
                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    label = { Text("Contraseña") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Contraseña",
                            tint = PurpleMid
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Botón de inicio de sesión, habilitado solo si ambos campos contienen texto
                Button(
                    onClick = {
                        // Navega a la pantalla principal removiendo Login del historial de navegación
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    enabled = correo.isNotBlank() && contrasena.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PurpleMid
                    )
                ) {
                    Text(
                        text = "INICIAR SESIÓN",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}
