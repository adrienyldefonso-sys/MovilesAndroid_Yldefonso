package com.yldefonso.lavacampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yldefonso.lavacampus.viewmodel.NotificacionesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampanaNotificaciones(
    vm: NotificacionesViewModel,
    onAbrirPedido: (Int) -> Unit
) {
    var abierto by remember { mutableStateOf(false) }
    val habilitadas by vm.habilitadas.collectAsState()
    val lista by vm.notificaciones.collectAsState()
    val conteo by vm.conteoSinLeer.collectAsState()

    IconButton(
        onClick = {
            abierto = true
            vm.marcarComoLeidas() // <--- Limpia el indicador rojo al abrir
        }
    ) {
        BadgedBox(
            badge = {
                if (habilitadas && conteo > 0) {
                    Badge { Text(conteo.toString()) }
                }
            }
        ) {
            Icon(Icons.Filled.Notifications, contentDescription = "Notificaciones")
        }
    }

    if (abierto) {
        AlertDialog(
            onDismissRequest = { abierto = false },
            title = { Text("Notificaciones", fontWeight = FontWeight.Bold) },
            text = {
                if (!habilitadas) {
                    Text("Las notificaciones están desactivadas en Ajustes.")
                } else if (lista.isEmpty()) {
                    Text("No tienes notificaciones por ahora.")
                } else {
                    LazyColumn(Modifier.heightIn(max = 380.dp)) {
                        items(lista, key = { "${it.pedidoId}-${it.titulo}" }) { n ->
                            val color = if (n.atrasado) MaterialTheme.colorScheme.error else n.estado.color
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        abierto = false
                                        onAbrirPedido(n.pedidoId)
                                    }
                                    .padding(vertical = 10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Spacer(Modifier.width(2.dp))
                                Box(
                                    Modifier
                                        .padding(top = 6.dp)
                                        .size(10.dp)
                                        .background(color, CircleShape)
                                )
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(n.titulo, fontWeight = FontWeight.Bold, color = color)
                                    Text(n.detalle, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                            HorizontalDivider()
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { abierto = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}