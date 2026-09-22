package com.yldefonso.navlab.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yldefonso.navlab.model.Student
import com.yldefonso.navlab.ui.theme.BrandGradient
import com.yldefonso.navlab.ui.theme.DetailCardGray
import com.yldefonso.navlab.ui.theme.PurpleDark
import com.yldefonso.navlab.ui.theme.PurpleMid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, student: Student) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expediente Académico") },
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
            // Contenedor del encabezado con el avatar superpuesto en el borde inferior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp) // Altura suficiente para dar espacio al avatar superpuesto
            ) {
                // Bloque morado con fondo BrandGradient y esquinas inferiores redondeadas (32.dp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                        .background(BrandGradient)
                )

                // Avatar circular grande (96.dp) con borde blanco (4.dp) superpuesto en el borde inferior
                AsyncImage(
                    model = student.avatarUrl,
                    contentDescription = "Foto de perfil de ${student.name}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(96.dp)
                        .align(Alignment.BottomCenter)
                        .border(4.dp, Color.White, CircleShape)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre del estudiante en negrita
            Text(
                text = student.name,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Carrera profesional en color morado PurpleMid
            Text(
                text = student.career,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = PurpleMid,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // UNA SOLA Card con fondo DetailCardGray (gris medio) conteniendo todos los datos del expediente
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DetailCardGray),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Fila ID Estudiante
                        DetailInfoRow(
                            icon = Icons.Default.Badge,
                            label = "ID Estudiante",
                            value = student.studentCode
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                        // Fila Correo Electronico
                        DetailInfoRow(
                            icon = Icons.Default.Email,
                            label = "Correo Electrónico",
                            value = student.email
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                        // Fila Facultad
                        DetailInfoRow(
                            icon = Icons.Default.School,
                            label = "Facultad",
                            value = student.faculty
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                        // Seccion Biografia dentro de la MISMA Card
                        Text(
                            text = "Biografía",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PurpleDark
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = student.bio,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

// Componente auxiliar para mostrar filas tipo etiqueta + valor con icono tintado en color morado PurpleMid
@Composable
private fun DetailInfoRow(
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
            tint = PurpleMid, // Icono con tint morado
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant // Etiqueta en color onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface // Valor en color onSurface
                )
            )
        }
    }
}
