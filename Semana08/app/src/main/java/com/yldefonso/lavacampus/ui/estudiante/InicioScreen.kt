package com.yldefonso.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yldefonso.lavacampus.data.local.entity.Usuario
import com.yldefonso.lavacampus.model.EstadoPedido
import com.yldefonso.lavacampus.ui.components.CampanaNotificaciones
import com.yldefonso.lavacampus.ui.components.LavaButton
import com.yldefonso.lavacampus.ui.components.PedidoCard
import com.yldefonso.lavacampus.ui.components.StepperEstado
import com.yldefonso.lavacampus.ui.components.fechaPanel
import com.yldefonso.lavacampus.viewmodel.InicioViewModel
import com.yldefonso.lavacampus.viewmodel.NotificacionesViewModel
import java.time.LocalDate

@Composable
fun InicioScreen(
    vm: InicioViewModel,
    usuario: Usuario,
    onNuevoPedido: () -> Unit,
    onMisPedidos: () -> Unit,
    onTarifas: () -> Unit,
    onPedido: (Int) -> Unit,
    notificaciones: NotificacionesViewModel,
    barraInferior: @Composable () -> Unit = {}
) {
    val activo by vm.pedidoActivo.collectAsState()
    val recientes by vm.recientes.collectAsState()

    Scaffold(bottomBar = barraInferior) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ---- Encabezado: saludo + campana ----
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(
                        "Hola, ${usuario.nombre.substringBefore(" ")}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${usuario.codigo} · Tecsup",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
                CampanaNotificaciones(vm = notificaciones, onAbrirPedido = onPedido)
            }

            // ---- Pedido activo (tarjeta turquesa) ----
            val primario = MaterialTheme.colorScheme.primary
            val sobrePrimario = MaterialTheme.colorScheme.onPrimary
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = primario),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val p = activo
                    if (p == null) {
                        Text(
                            "No tienes pedidos activos",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = sobrePrimario
                        )
                        Text(
                            "Arma tu primer pedido y sigue tu ropa desde aquí.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = sobrePrimario.copy(alpha = 0.9f)
                        )
                    } else {
                        val estado = EstadoPedido.valueOf(p.pedido.estado)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Tu pedido activo",
                                style = MaterialTheme.typography.labelLarge,
                                color = sobrePrimario.copy(alpha = 0.85f),
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                "#${p.pedido.id.toString().padStart(3, '0')}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = sobrePrimario
                            )
                        }
                        Text(
                            estado.etiqueta,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = sobrePrimario
                        )
                        if (estado == EstadoPedido.LISTO && p.pedido.casillero != null) {
                            Text(
                                "Recógelo en el casillero ${p.pedido.casillero}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = sobrePrimario
                            )
                        } else {
                            Text(
                                "Entrega estimada: " +
                                        fechaPanel(LocalDate.parse(p.pedido.fechaEntrega)).lowercase(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = sobrePrimario.copy(alpha = 0.9f)
                            )
                        }
                        StepperEstado(
                            estado = estado,
                            colorActivo = sobrePrimario,
                            colorInactivo = sobrePrimario.copy(alpha = 0.3f),
                            colorTexto = sobrePrimario,
                            colorTextoInactivo = sobrePrimario.copy(alpha = 0.6f),
                            colorCheck = primario
                        )
                    }
                }
            }

            LavaButton(texto = "Nuevo pedido", onClick = onNuevoPedido)

            // ---- Accesos rápidos ----
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AccesoRapido(
                    titulo = "Mis pedidos",
                    subtitulo = "Ver estado",
                    icono = Icons.Filled.Menu,
                    onClick = onMisPedidos,
                    modifier = Modifier.weight(1f)
                )
                AccesoRapido(
                    titulo = "Tarifas",
                    subtitulo = "Precios por kg",
                    icono = Icons.Filled.LocalOffer,
                    onClick = onTarifas,
                    modifier = Modifier.weight(1f)
                )
            }

            // ---- Últimos pedidos ----
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Últimos pedidos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    "Ver todos",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = primario,
                    modifier = Modifier.clickable(onClick = onMisPedidos).padding(4.dp)
                )
            }
            if (recientes.isEmpty()) {
                Text("Aún no tienes pedidos.", style = MaterialTheme.typography.bodyMedium)
            } else {
                recientes.forEach { pedido ->
                    PedidoCard(
                        pedido = pedido,
                        onClick = { onPedido(pedido.pedido.id) },
                        resaltarCasillero = false,
                        fechaEnSubtitulo = true
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccesoRapido(
    titulo: String,
    subtitulo: String,
    icono: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(44.dp).background(
                    MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp)
                ),
                contentAlignment = Alignment.Center
            ) {
                Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
            Column(Modifier.padding(start = 10.dp)) {
                Text(titulo, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(
                    subtitulo,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}