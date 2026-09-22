package com.yldefonso.navlab.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yldefonso.navlab.navigation.Screen
import com.yldefonso.navlab.ui.theme.BECKER_AVATAR_URL
import com.yldefonso.navlab.ui.theme.BackgroundLilac
import com.yldefonso.navlab.ui.theme.BrandGradient
import com.yldefonso.navlab.ui.theme.ErrorRed
import com.yldefonso.navlab.ui.theme.PurpleMid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración de Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Encabezado con BrandGradient (degradado morado de 3 paradas) y esquinas inferiores redondeadas (32.dp)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .background(BrandGradient)
                    .padding(vertical = 32.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar circular grande cargado desde BECKER_AVATAR_URL con borde blanco (4.dp)
                AsyncImage(
                    model = BECKER_AVATAR_URL, // Misma constante usada en Student.kt para Becker Yldefonso
                    contentDescription = "Foto de perfil de Becker Yldefonso",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(96.dp)
                        .border(4.dp, Color.White, CircleShape)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Nombre completo del usuario logueado en blanco y negrita
                Text(
                    text = "Becker Yldefonso",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 24.sp
                    )
                )
            }

            // Cuerpo de la pantalla con un degradado sutil de BackgroundLilac a blanco
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(BackgroundLilac, Color.White)
                        )
                    )
                    .padding(20.dp)
            ) {
                // Titulo de seccion: INFORMACION PERSONAL en color PurpleMid
                Text(
                    text = "INFORMACIÓN PERSONAL",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = PurpleMid,
                        letterSpacing = 1.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Contenedor totalmente blanco sin lineas divisorias internas ni bordes (elevation = 0.dp)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ProfileInfoRow(
                            icon = Icons.Default.Person,
                            label = "Nombre Completo",
                            value = "Becker Yldefonso"
                        )
                        Spacer(modifier = Modifier.height(14.dp)) // Sin HorizontalDivider, solo espaciado limpio
                        ProfileInfoRow(
                            icon = Icons.Default.Email,
                            label = "Correo",
                            value = "adrien.yldefonso@tecsup.edu.pe"
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        ProfileInfoRow(
                            icon = Icons.Default.Phone,
                            label = "Teléfono",
                            value = "927 902 563"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Titulo de seccion: ACADEMICO en color PurpleMid
                Text(
                    text = "ACADÉMICO",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = PurpleMid,
                        letterSpacing = 1.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Contenedor limpio sin divisorias para los datos academicos
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ProfileInfoRow(
                            icon = Icons.Default.School,
                            label = "Carrera",
                            value = "Diseño y Desarrollo de Software"
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        ProfileInfoRow(
                            icon = Icons.Default.Timeline,
                            label = "Ciclo Actual",
                            value = "IV Ciclo"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Boton "Cerrar Sesion" redisenado como una Card-Boton con borde rojo y fondo suave
                OutlinedButton(
                    onClick = {
                        // Navega a Login limpiando todo el backstack (popUpTo(0))
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    border = BorderStroke(1.5.dp, ErrorRed),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = ErrorRed.copy(alpha = 0.08f), // Fondo rojo muy suave
                        contentColor = ErrorRed
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            tint = ErrorRed
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Cerrar Sesión",
                            color = ErrorRed,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

// Componente auxiliar para las filas de informacion de perfil
@Composable
private fun ProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = PurpleMid,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            )
        }
    }
}
