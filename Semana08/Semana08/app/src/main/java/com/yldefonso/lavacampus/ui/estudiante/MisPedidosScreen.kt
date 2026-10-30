package com.yldefonso.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yldefonso.lavacampus.data.local.relation.PedidoConDetalle
import com.yldefonso.lavacampus.ui.components.AppTopBar
import com.yldefonso.lavacampus.ui.components.PedidoCard
import com.yldefonso.lavacampus.ui.components.SelectorPildora
import com.yldefonso.lavacampus.viewmodel.MisPedidosViewModel

@Composable
fun MisPedidosScreen(
    vm: MisPedidosViewModel,
    onAtras: () -> Unit,
    onPedido: (Int) -> Unit,
    onNuevoPedido: () -> Unit,
    onRepetir: (Int) -> Unit = {},
    barraInferior: @Composable () -> Unit = {}
) {
    val activos by vm.activos.collectAsState()
    val historial by vm.historial.collectAsState()
    val aviso by vm.avisoListo.collectAsState()
    var pestana by rememberSaveable { mutableIntStateOf(0) }
    var porCancelar by remember { mutableStateOf<PedidoConDetalle?>(null) }

    Scaffold(
        topBar = { AppTopBar(titulo = "Mis pedidos", onAtras = onAtras, claro = true) },
        bottomBar = barraInferior,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNuevoPedido,
                shape = RoundedCornerShape(18.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) { Icon(Icons.Filled.Add, contentDescription = "Nuevo pedido", modifier = Modifier.size(28.dp)) }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            SelectorPildora(
                opciones = listOf("Activos", "Historial"),
                seleccionada = pestana,
                onSeleccion = { pestana = it },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            val lista = if (pestana == 0) activos else historial
            if (lista.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        if (pestana == 0) "No tienes pedidos activos" else "Aún no tienes pedidos en el historial",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(lista, key = { it.pedido.id }) { pedido ->
                        PedidoCard(
                            pedido = pedido,
                            onClick = { onPedido(pedido.pedido.id) },
                            onCancelar = if (pestana == 0 && vm.puedeCancelar(pedido)) ({ porCancelar = pedido }) else null,
                            onRepetir = if (pestana == 1 && vm.puedeRepetir(pedido)) ({ onRepetir(pedido.pedido.id) }) else null,
                            mostrarEstudiante = true   // quita esta línea si no quieres "Ana T. ·"
                        )
                    }
                    if (pestana == 0) {
                        aviso?.let { listo -> item { AvisoCard(listo.pedido.id) } }
                    }
                }
            }
        }
    }

    porCancelar?.let { pedido ->
        AlertDialog(
            onDismissRequest = { porCancelar = null },
            title = { Text("¿Cancelar el pedido #${pedido.pedido.id}?") },
            text = { Text("Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = { vm.cancelar(pedido); porCancelar = null }) {
                    Text("Sí, cancelar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = { TextButton(onClick = { porCancelar = null }) { Text("No") } }
        )
    }
}

@Composable
private fun AvisoCard(pedidoId: Int) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                "Aviso",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "Tu pedido #$pedidoId está listo: recógelo en el casillero indicado.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}