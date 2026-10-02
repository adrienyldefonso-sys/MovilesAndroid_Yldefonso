package com.yldefonso.lab04_carrito_yldefonso.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DrawerContenido(
    destinos: List<String>,
    pantallaActual: String,
    onDestinoSeleccionado: (String) -> Unit
) {
    ModalDrawerSheet {
        // Encabezado del usuario
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF6C5CA5)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "BY",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Becker Yldefonso",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "becker@tecsup.edu.pe",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))

        // Lista de opciones con RadioButton de selección
        destinos.forEach { destino ->
            val esSeleccionado = pantallaActual == destino

            NavigationDrawerItem(
                label = { Text(destino) },
                icon = {
                    RadioButton(
                        selected = esSeleccionado,
                        onClick = null, // El click se maneja en la tarjeta completa
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFF6C5CA5)
                        )
                    )
                },
                selected = esSeleccionado,
                onClick = { onDestinoSeleccionado(destino) },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = Color(0xFFEDE9F5),
                    selectedTextColor = Color(0xFF6C5CA5)
                ),
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}