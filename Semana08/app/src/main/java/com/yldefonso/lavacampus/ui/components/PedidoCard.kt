package com.yldefonso.lavacampus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yldefonso.lavacampus.data.local.relation.PedidoConDetalle
import com.yldefonso.lavacampus.model.EstadoPedido

/** Tarjeta de pedido. Si recibe [onCancelar] muestra el enlace "Cancelar" (Paso 11). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidoCard(
    pedido: PedidoConDetalle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onCancelar: (() -> Unit)? = null
) {
    val p = pedido.pedido
    val estado = EstadoPedido.valueOf(p.estado)
    val listoConCasillero = estado == EstadoPedido.LISTO && p.casillero != null

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = if (listoConCasillero) BorderStroke(2.dp, estado.color) else null,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Pedido #${p.id}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                EstadoPedidoChip(estado)
            }
            Text(
                pedido.detalles.joinToString(" · ") {
                    "${it.servicio.nombre} ${formatoCantidad(it.detalle.cantidad, it.servicio.unidad)}"
                },
                style = MaterialTheme.typography.bodySmall
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Registrado: ${fechaCorta(p.fechaRegistro)}",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(soles(p.total), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            }
            if (listoConCasillero) {
                Text(
                    "Casillero ${p.casillero}",
                    color = estado.color,
                    fontWeight = FontWeight.SemiBold
                )
            }
            if (onCancelar != null) {
                TextButton(onClick = onCancelar) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}