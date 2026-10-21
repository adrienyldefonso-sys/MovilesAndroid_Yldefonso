package com.yldefonso.lavacampus.ui.administrador

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import com.yldefonso.lavacampus.model.Casilleros
import com.yldefonso.lavacampus.model.EstadoPedido
import com.yldefonso.lavacampus.ui.components.AppTopBar
import com.yldefonso.lavacampus.ui.components.EstadoPedidoChip
import com.yldefonso.lavacampus.ui.components.LavaButton
import com.yldefonso.lavacampus.ui.components.StepperEstado
import com.yldefonso.lavacampus.ui.components.fechaLarga
import com.yldefonso.lavacampus.ui.components.formatoCantidad
import com.yldefonso.lavacampus.ui.components.soles
import com.yldefonso.lavacampus.viewmodel.GestionPedidoViewModel
import com.yldefonso.lavacampus.viewmodel.ReglasPedido

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GestionPedidoScreen(vm: GestionPedidoViewModel, onAtras: () -> Unit) {
    val pc by vm.pedido.collectAsState()
    val ocupados by vm.casillerosOcupados.collectAsState()
    val elegido by vm.casilleroElegido.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    var confirmarCancelar by remember { mutableStateOf(false) }

    val pedidoActual = pc

    Scaffold(
        topBar = {
            AppTopBar(
                titulo = pedidoActual?.let { "Pedido #${it.pedido.id}" } ?: "Pedido",
                onAtras = onAtras,
                claro = true
            )
        }
    ) { padding ->
        if (pedidoActual == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Cargando pedido…")
            }
            return@Scaffold
        }

        val p = pedidoActual.pedido
        val estado = EstadoPedido.valueOf(p.estado)
        val siguiente = estado.siguiente()

        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // ---- Estudiante ----
            Tarjeta {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            pedidoActual.usuario.nombre,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "${pedidoActual.usuario.codigo} · ${pedidoActual.usuario.correo}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            "Entrega estimada: ${fechaLarga(p.fechaEntrega)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    EstadoPedidoChip(estado)
                }
            }

            // ---- Seguimiento ----
            Tarjeta {
                Text("Seguimiento", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                StepperEstado(estado = estado, vertical = true)
            }

            // ---- Detalle ----
            Tarjeta {
                Text("Detalle", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                pedidoActual.detalles.forEach { d ->
                    Row {
                        Text(
                            "${d.servicio.nombre} · ${formatoCantidad(d.detalle.cantidad, d.servicio.unidad)}",
                            modifier = Modifier.weight(1f)
                        )
                        Text(soles(d.detalle.subtotal))
                    }
                }
            }

            // ---- Casillero ----
            Tarjeta {
                Text("Casillero", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                when (estado) {
                    EstadoPedido.EN_PROCESO -> {
                        Text(
                            elegido?.let { "Elegido: $it" } ?: "Elige un casillero libre para marcarlo como listo",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Casilleros.todos.forEach { c ->
                                FilterChip(
                                    selected = c == elegido,
                                    onClick = { vm.elegirCasillero(c) },
                                    enabled = c !in ocupados,   // ocupados: en gris y deshabilitados
                                    label = { Text(c) }
                                )
                            }
                        }
                    }
                    EstadoPedido.LISTO -> Text(
                        "Asignado: ${p.casillero ?: "—"}",
                        color = EstadoPedido.LISTO.color,
                        fontWeight = FontWeight.Bold
                    )
                    EstadoPedido.RECIBIDO -> Text(
                        "Se elige al pasar el pedido a Listo",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    else -> Text(p.casillero ?: "Sin casillero", style = MaterialTheme.typography.bodyMedium)
                }
            }

            // ---- Total y cobro ----
            Tarjeta {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Total", style = MaterialTheme.typography.labelMedium)
                        Text(
                            soles(p.total),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text("Cobrado", fontWeight = FontWeight.SemiBold)
                    Switch(
                        checked = p.pagado,
                        onCheckedChange = vm::marcarPagado,
                        enabled = estado != EstadoPedido.CANCELADO,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                HorizontalDivider()
                Text(
                    "Recuerda: para entregar, el pedido debe estar cobrado.",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // ---- Motivo de rechazo ----
            mensaje?.let {
                Text(it, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold)
            }

            // ---- Acciones ----
            if (siguiente != null) {
                LavaButton(texto = "Marcar como ${siguiente.etiqueta}", onClick = vm::avanzar)
            }
            if (ReglasPedido.puedeCancelarAdmin(estado)) {
                OutlinedButton(
                    onClick = { confirmarCancelar = true },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Cancelar pedido") }
            }
        }

        if (confirmarCancelar) {
            AlertDialog(
                onDismissRequest = { confirmarCancelar = false },
                title = { Text("¿Cancelar el pedido #${p.id}?") },
                text = { Text("Esta acción no se puede deshacer.") },
                confirmButton = {
                    TextButton(onClick = { confirmarCancelar = false; vm.cancelar() }) {
                        Text("Sí, cancelar", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = { TextButton(onClick = { confirmarCancelar = false }) { Text("No") } }
            )
        }
    }
}

@Composable
private fun Tarjeta(contenido: @Composable () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) { contenido() }
    }
}