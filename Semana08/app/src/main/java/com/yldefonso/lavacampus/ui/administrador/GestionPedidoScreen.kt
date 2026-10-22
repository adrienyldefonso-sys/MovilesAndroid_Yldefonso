package com.yldefonso.lavacampus.ui.administrador

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yldefonso.lavacampus.model.Casilleros
import com.yldefonso.lavacampus.model.EstadoPedido
import com.yldefonso.lavacampus.ui.components.AppTopBar
import com.yldefonso.lavacampus.ui.components.EstadoPedidoChip
import com.yldefonso.lavacampus.ui.components.LavaButton
import com.yldefonso.lavacampus.ui.components.StepperEstado
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
                titulo = pedidoActual?.let { "Gestionar pedido #${it.pedido.id.toString().padStart(3, '0')}" } ?: "Gestionar pedido",
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

        val iniciales = pedidoActual.usuario.nombre
            .split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .mapNotNull { it.firstOrNull()?.uppercase() }
            .joinToString("")

        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // ---- Tarjeta del Estudiante con Avatar ----
            Tarjeta {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = iniciales.ifEmpty { "U" },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Column(Modifier.weight(1f)) {
                        Text(
                            pedidoActual.usuario.nombre,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            pedidoActual.usuario.codigo,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    EstadoPedidoChip(estado)
                }
            }

            // ---- Seguimiento (línea de tiempo con checks) ----
            Tarjeta {
                Text("Seguimiento", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                StepperEstado(
                    estado = estado,
                    vertical = true,
                    colorCheck = Color.White
                )
            }

            // ---- Detalle de prendas y servicios ----
            Tarjeta {
                Text("Detalle", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                pedidoActual.detalles.forEach { d ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            "${d.servicio.nombre} · ${formatoCantidad(d.detalle.cantidad, d.servicio.unidad)}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            soles(d.detalle.subtotal),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // ---- Casillero para el recojo ----
            Tarjeta {
                Text("Casillero para el recojo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                when (estado) {
                    EstadoPedido.EN_PROCESO -> {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Casilleros.todos.forEach { c ->
                                FilterChip(
                                    selected = c == elegido,
                                    onClick = { vm.elegirCasillero(c) },
                                    enabled = c !in ocupados,
                                    label = { Text(c) }
                                )
                            }
                        }
                        Text(
                            "Los casilleros en gris ya están ocupados",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                    EstadoPedido.LISTO -> Text(
                        "Asignado: ${p.casillero ?: "—"}",
                        color = EstadoPedido.LISTO.color,
                        fontWeight = FontWeight.Bold
                    )
                    EstadoPedido.RECIBIDO -> Text(
                        "Se elige al pasar el pedido a Listo",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    else -> Text(p.casillero ?: "Sin casillero", style = MaterialTheme.typography.bodyMedium)
                }
            }

            // ---- Total y Cobro ----
            Tarjeta {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            "Total ${soles(p.total)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Se cobra al entregar",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    Text(
                        "Cobrado",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.width(8.dp))
                    Switch(
                        checked = p.pagado,
                        onCheckedChange = vm::marcarPagado,
                        enabled = estado != EstadoPedido.CANCELADO
                    )
                }
            }

            // ---- Mensaje de error / validación ----
            mensaje?.let {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // ---- Botones de Acción ----
            if (siguiente != null) {
                LavaButton(
                    texto = "Marcar como ${siguiente.etiqueta}",
                    onClick = vm::avanzar
                )
            }

            if (ReglasPedido.puedeCancelarAdmin(estado)) {
                OutlinedButton(
                    onClick = { confirmarCancelar = true },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar pedido", fontWeight = FontWeight.Bold)
                }
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