package com.yldefonso.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yldefonso.lavacampus.model.EstadoPedido
import com.yldefonso.lavacampus.ui.components.AppTopBar
import com.yldefonso.lavacampus.ui.components.EstadoPedidoChip
import com.yldefonso.lavacampus.ui.components.IconoServicio
import com.yldefonso.lavacampus.ui.components.StepperEstado
import com.yldefonso.lavacampus.ui.components.fechaCorta
import com.yldefonso.lavacampus.ui.components.fechaLarga
import com.yldefonso.lavacampus.ui.components.formatoCantidad
import com.yldefonso.lavacampus.ui.components.soles
import com.yldefonso.lavacampus.viewmodel.DetallePedidoViewModel

@Composable
fun DetallePedidoScreen(vm: DetallePedidoViewModel, onAtras: () -> Unit) {
    val pedidoConDetalle by vm.pedido.collectAsState()
    val puedeCancelar by vm.puedeCancelar.collectAsState()
    var mostrarDialogo by remember { mutableStateOf(false) }

    val pc = pedidoConDetalle

    Scaffold(
        topBar = { AppTopBar(titulo = pc?.let { "Pedido #${it.pedido.id}" } ?: "Pedido", onAtras = onAtras) }
    ) { padding ->
        if (pc == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Cargando pedido…")
            }
            return@Scaffold
        }

        val p = pc.pedido
        val estado = EstadoPedido.valueOf(p.estado)

        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cabecera: fecha y chip de estado
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Registrado el ${fechaCorta(p.fechaRegistro)}", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "Entrega estimada: ${fechaLarga(p.fechaEntrega)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                EstadoPedidoChip(estado)
            }

            // Seguimiento vertical
            Tarjeta {
                Text("Seguimiento", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                StepperEstado(estado = estado, vertical = true)
                if (estado == EstadoPedido.CANCELADO) {
                    Text(
                        "Este pedido fue cancelado.",
                        color = EstadoPedido.CANCELADO.color,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Casillero
            Tarjeta {
                Text("Casillero", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                if (estado == EstadoPedido.LISTO && p.casillero != null) {
                    Text(
                        "Recógelo en el casillero ${p.casillero}",
                        color = EstadoPedido.LISTO.color,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text("Se asignará al estar listo", style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Líneas del pedido
            Tarjeta {
                Text("Detalle", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                pc.detalles.forEach { d ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconoServicio(icono = d.servicio.icono, tamano = 32.dp)
                        Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                            Text(d.servicio.nombre, fontWeight = FontWeight.SemiBold)
                            // Precio congelado al momento del pedido
                            Text(
                                "${formatoCantidad(d.detalle.cantidad, d.servicio.unidad)} × ${soles(d.detalle.precioUnitario)}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Text(soles(d.detalle.subtotal), fontWeight = FontWeight.SemiBold)
                    }
                }
                HorizontalDivider()
                Row {
                    Text("Total", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text(soles(p.total), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                if (p.nota.isNotBlank()) {
                    Text("Nota: ${p.nota}", style = MaterialTheme.typography.bodySmall)
                }
            }

            // Pago
            Tarjeta {
                Text("Pago", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                if (p.pagado) {
                    Text("Pagado", color = EstadoPedido.LISTO.color, fontWeight = FontWeight.Bold)
                } else {
                    Text("Pago pendiente, se paga al recoger", style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Cancelación
            OutlinedButton(
                onClick = { mostrarDialogo = true },
                enabled = puedeCancelar,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Cancelar pedido") }

            if (!puedeCancelar) {
                Text(
                    "Solo se cancela antes de empezar el lavado",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        if (mostrarDialogo) {
            AlertDialog(
                onDismissRequest = { mostrarDialogo = false },
                title = { Text("¿Cancelar el pedido #${p.id}?") },
                text = { Text("Esta acción no se puede deshacer.") },
                confirmButton = {
                    TextButton(onClick = { mostrarDialogo = false; vm.cancelar() }) {
                        Text("Sí, cancelar", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogo = false }) { Text("No") }
                }
            )
        }
    }
}

@Composable
private fun Tarjeta(contenido: @Composable () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            contenido()
        }
    }
}