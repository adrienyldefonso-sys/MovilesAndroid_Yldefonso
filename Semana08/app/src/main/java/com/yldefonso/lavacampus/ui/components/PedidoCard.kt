package com.yldefonso.lavacampus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yldefonso.lavacampus.data.local.relation.PedidoConDetalle
import com.yldefonso.lavacampus.model.EstadoPedido
import java.time.LocalDate

/**
 * Tarjeta de pedido. LISTO con casillero: borde y botón verdes.
 * Con [onCancelar] muestra el enlace "Cancelar". [mostrarEstudiante] antepone "Ana T. ·".
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidoCard(
    pedido: PedidoConDetalle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onCancelar: (() -> Unit)? = null,
    onRepetir: (() -> Unit)? = null,
    mostrarEstudiante: Boolean = false,
    resaltarCasillero: Boolean = true,
    fechaEnSubtitulo: Boolean = false
) {
    val p = pedido.pedido
    val estado = EstadoPedido.valueOf(p.estado)
    val listo = estado == EstadoPedido.LISTO && p.casillero != null
    val icono = pedido.detalles.firstOrNull()?.servicio?.icono ?: "wash"
    val subtitulo = (if (mostrarEstudiante) "${nombreCorto(pedido.usuario.nombre)} · " else "") +
            resumenCantidades(pedido.detalles) +
            (if (fechaEnSubtitulo) " · ${fechaPanel(LocalDate.parse(p.fechaRegistro))}" else "")

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = if (listo && resaltarCasillero) BorderStroke(2.dp, estado.color) else null,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                Modifier.size(48.dp).background(estado.color.copy(alpha = 0.12f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) { IconoServicio(icono = icono, tamano = 28.dp, color = estado.color) }

            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Pedido #${p.id}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        soles(p.total),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    subtitulo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    EstadoPedidoChip(estado)
                    Spacer(Modifier.weight(1f))
                    when {
                        listo && resaltarCasillero -> Surface(color = estado.color, shape = RoundedCornerShape(50)) {
                            Text(
                                "Casillero ${p.casillero}",
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                        listo -> Text(
                            "Casillero ${p.casillero}",
                            color = estado.color,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        onCancelar != null -> Text(
                            "Cancelar",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable(onClick = onCancelar).padding(4.dp)
                        )
                        onRepetir != null -> Text(
                            "Pedir de nuevo",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable(onClick = onRepetir).padding(4.dp)
                        )
                        estado.esActivo -> Text(
                            "Entrega ${diaCorto(p.fechaEntrega)}",
                            color = estado.color,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        else -> Text(fechaCorta(p.fechaRegistro), style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}