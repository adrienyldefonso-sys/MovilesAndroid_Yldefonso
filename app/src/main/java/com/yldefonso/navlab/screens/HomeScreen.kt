package com.yldefonso.navlab.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yldefonso.navlab.navigation.Screen
import com.yldefonso.navlab.ui.theme.BrandGradient
import com.yldefonso.navlab.ui.theme.PurpleDark
import com.yldefonso.navlab.ui.theme.PurpleLight

@Composable
fun HomeScreen(navController: NavController) {
    // Contenedor principal con fondo BrandGradient (3 tonos de degradado morado) que cubre TODA la pantalla
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandGradient)
    ) {
        // Scaffold transparente para garantizar que el boton "Cerrar Sesion Segura" este SIEMPRE fijo al fondo (bottomBar)
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                // Boton de salida siempre visible en la parte inferior de la pantalla
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(
                        onClick = {
                            // Limpia todo el backstack y redirige a LoginScreen
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Cerrar sesión",
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Cerrar Sesión Segura",
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            // Column con Arrangement.Center para CENTRAR VERTICALMENTE todo el bloque en el espacio disponible
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Texto de bienvenida centrado horizontalmente
                Text(
                    text = "Bienvenido,",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 22.sp
                    ),
                    textAlign = TextAlign.Center
                )

                // Nombre del usuario logueado en texto grande, blanco y centrado
                Text(
                    text = "Becker Yldefonso",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 34.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "¿Qué deseas gestionar hoy?",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color(0xFFE9DFFC) // Lila claro para contraste sobre morado
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Opcion 1: Tarjeta para navegar al Directorio de Alumnos
                HomeMenuCard(
                    icon = Icons.Default.Group,
                    title = "Directorio de Alumnos",
                    subtitle = "Ver y gestionar estudiantes",
                    onClick = { navController.navigate(Screen.List.route) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Opcion 2: Tarjeta para navegar al Perfil Academico
                HomeMenuCard(
                    icon = Icons.Default.Person,
                    title = "Mi Perfil Académico",
                    subtitle = "Datos personales y progreso",
                    onClick = { navController.navigate(Screen.Profile.route) }
                )
            }
        }
    }
}

// Composable auxiliar reutilizable para las tarjetas del menu principal de Home
@Composable
private fun HomeMenuCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono circular a la izquierda
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(PurpleLight.copy(alpha = 0.25f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = PurpleDark,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Titulo y subtitulo descriptivo
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Gray
                    )
                )
            }
        }
    }
}
