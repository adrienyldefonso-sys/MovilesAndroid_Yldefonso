package com.yldefonso.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yldefonso.lavacampus.data.local.entity.Usuario
import com.yldefonso.lavacampus.model.EstadoPedido
import com.yldefonso.lavacampus.ui.components.AppTopBar
import com.yldefonso.lavacampus.ui.components.EstadoPedidoChip
import com.yldefonso.lavacampus.ui.components.LavaButton
import com.yldefonso.lavacampus.ui.components.PedidoCard
import com.yldefonso.lavacampus.ui.components.StepperEstado
import com.yldefonso.lavacampus.ui.components.fechaLarga
import com.yldefonso.lavacampus.viewmodel.InicioViewModel

@Composable
fun InicioScreen(
    vm: InicioViewModel,
    usuario: Usuario,
    onNuevoPedido: () -> Unit,
    onMisPedidos: () -> Unit,
    onTarifas: () -> Unit,
    onPedido: (Int) -> Unit,
    onCerrarSesion: () -> Unit,   // temporal: se mueve a Perfil en el Paso 13
    barraInferior: @Composable () -> Unit = {}
) {
    val activo by vm.pedidoActivo.collectAsState()
    val recientes by vm.recientes.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(titulo = "LavaCampus", acciones = {
                IconButton(onClick = onCerrarSesion) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
                }
            })
        },
        bottomBar = barraInferior
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Saludo
            Column {
                Text(
                    "Hola, ${usuario.nombre.substringBefore(" ")}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text("Código ${usuario.codigo}", style = MaterialTheme.typography.bodyMedium)
            }

            // Pedido activo con seguimiento
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    val p = activo
                    if (p == null) {
                        Text(
                            "No tienes pedidos activos",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            "Arma tu primer pedido y sigue tu ropa desde aquí.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    } else {
                        val estado = EstadoPedido.valueOf(p.pedido.estado)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Pedido #${p.pedido.id}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.weight(1f)
                            )
                            EstadoPedidoChip(estado)
                        }
                        StepperEstado(estado = estado)
                        if (estado == EstadoPedido.LISTO && p.pedido.casillero != null) {
                            Text(
                                "Recógelo en el casillero ${p.pedido.casillero}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = estado.color
                            )
                        } else {
                            Text(
                                "Entrega estimada: ${fechaLarga(p.pedido.fechaEntrega)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            LavaButton(texto = "Nuevo pedido", onClick = onNuevoPedido)

            // Accesos rápidos
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FilledTonalButton(onClick = onMisPedidos, modifier = Modifier.weight(1f)) { Text("Mis pedidos") }
                FilledTonalButton(onClick = onTarifas, modifier = Modifier.weight(1f)) { Text("Tarifas") }
            }

            // Últimos pedidos
            Text(
                "Últimos pedidos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            if (recientes.isEmpty()) {
                Text("Aún no tienes pedidos.", style = MaterialTheme.typography.bodyMedium)
            } else {
                recientes.forEach { pedido ->
                    PedidoCard(pedido = pedido, onClick = { onPedido(pedido.pedido.id) })
                }
            }
        }
    }
}