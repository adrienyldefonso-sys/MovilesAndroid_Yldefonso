package com.yldefonso.clinicasaludplus.ui.components.notificaciones

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yldefonso.clinicasaludplus.repository.NotificacionItem
import com.yldefonso.clinicasaludplus.repository.Repositorio

private val AzulPrimario = Color(0xFF2F6BEA)
private val AzulFondoSoft = Color(0xFFEAF1FF)
private val TextoOscuro = Color(0xFF1B2540)
private val TextoGris = Color(0xFF6F7B91)
private val FondoCard = Color(0xFFF8FAFC)
private val BadgePendienteFondo = Color(0xFFFFF3E0)
private val BadgePendienteTexto = Color(0xFFE65100)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificacionesScreen(navController: NavController) {
    val listaNotificaciones = Repositorio.notificaciones

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Notificaciones",
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
                    containerColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            if (listaNotificaciones.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tienes notificaciones por el momento",
                        fontSize = 15.sp,
                        color = TextoGris,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(listaNotificaciones) { item ->
                        TarjetaNotificacion(item)
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaNotificacion(item: NotificacionItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FondoCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Contenedor de ícono según tipo de notificación
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(AzulFondoSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (item.estado == "Pendiente") Icons.Default.CalendarMonth else Icons.Default.Notifications,
                    contentDescription = null,
                    tint = AzulPrimario,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.titulo,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextoOscuro,
                        modifier = Modifier.weight(1f)
                    )

                    // Badge de Estado "Pendiente"
                    if (item.estado == "Pendiente") {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(BadgePendienteFondo)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "Pendiente",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BadgePendienteTexto
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.mensaje,
                    fontSize = 13.sp,
                    color = TextoOscuro.copy(alpha = 0.8f),
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = item.fecha,
                    fontSize = 11.sp,
                    color = TextoGris
                )
            }
        }
    }
}