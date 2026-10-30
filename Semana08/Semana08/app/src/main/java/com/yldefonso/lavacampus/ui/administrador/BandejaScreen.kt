package com.yldefonso.lavacampus.ui.administrador

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yldefonso.lavacampus.data.local.relation.PedidoConDetalle
import com.yldefonso.lavacampus.model.EstadoPedido
import com.yldefonso.lavacampus.ui.components.EstadoPedidoChip
import com.yldefonso.lavacampus.ui.components.IconoServicio
import com.yldefonso.lavacampus.ui.components.fechaCorta
import com.yldefonso.lavacampus.ui.components.resumenCantidades
import com.yldefonso.lavacampus.ui.components.soles
import com.yldefonso.lavacampus.viewmodel.BandejaViewModel
import com.yldefonso.lavacampus.viewmodel.FiltroBandeja

@Composable
fun BandejaScreen(
    vm: BandejaViewModel,
    onPedido: (Int) -> Unit,
    barraInferior: @Composable () -> Unit = {}
) {
    val pedidos by vm.pedidos.collectAsState()
    val filtro by vm.filtro.collectAsState()
    val busqueda by vm.busqueda.collectAsState()
    val atrasados by vm.atrasados.collectAsState()

    Scaffold(bottomBar = barraInferior) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            Text(
                "Pedidos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
            )

            OutlinedTextField(
                value = busqueda,
                onValueChange = vm::buscar,
                placeholder = { Text("Buscar por estudiante o número") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                items(FiltroBandeja.entries) { f ->
                    FilterChip(
                        selected = f == filtro,
                        onClick = { vm.elegirFiltro(f) },
                        label = { Text(f.etiqueta) }
                    )
                }
            }

            if (pedidos.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay pedidos para mostrar", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(pedidos, key = { it.pedido.id }) { p ->
                        PedidoBandejaItem(
                            pedido = p,
                            atrasado = p.pedido.id in atrasados,
                            onAbrir = { onPedido(p.pedido.id) },
                            onIniciar = { vm.iniciar(p) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PedidoBandejaItem(
    pedido: PedidoConDetalle,
    atrasado: Boolean,
    onAbrir: () -> Unit,
    onIniciar: () -> Unit
) {
    val p = pedido.pedido
    val estado = EstadoPedido.valueOf(p.estado)
    val icono = pedido.detalles.firstOrNull()?.servicio?.icono ?: "wash"

    // Texto del botón según el estado; los pasos con reglas se resuelven en la gestión
    val accion: String? = when (estado) {
        EstadoPedido.RECIBIDO -> "Iniciar"
        EstadoPedido.EN_PROCESO -> "Marcar listo"
        EstadoPedido.LISTO -> "Entregar"
        else -> null
    }

    Card(
        onClick = onAbrir,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = if (atrasado) BorderStroke(1.5.dp, MaterialTheme.colorScheme.error) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
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
                    "${pedido.usuario.nombre} · ${resumenCantidades(pedido.detalles)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                )
                if (atrasado) {
                    Text(
                        "Entrega vencida: ${fechaCorta(p.fechaEntrega)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Medium
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    EstadoPedidoChip(estado)
                    if (atrasado) {
                        Surface(
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.padding(start = 6.dp)
                        ) {
                            Text(
                                text = "Atrasado",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                    if (p.pagado && estado.esActivo) {
                        Text(
                            "  Cobrado",
                            style = MaterialTheme.typography.labelSmall,
                            color = EstadoPedido.LISTO.color,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Box(Modifier.weight(1f))
                    if (accion != null) {
                        Button(
                            onClick = if (estado == EstadoPedido.RECIBIDO) onIniciar else onAbrir,
                            shape = RoundedCornerShape(50),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (estado == EstadoPedido.LISTO) EstadoPedido.LISTO.color
                                else MaterialTheme.colorScheme.primary
                            )
                        ) { Text(accion, style = MaterialTheme.typography.labelLarge) }
                    }
                }
            }
        }
    }
}