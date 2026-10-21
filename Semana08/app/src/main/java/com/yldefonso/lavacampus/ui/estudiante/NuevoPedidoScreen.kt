package com.yldefonso.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yldefonso.lavacampus.ui.components.AppTopBar
import com.yldefonso.lavacampus.ui.components.IconoServicio
import com.yldefonso.lavacampus.ui.components.LavaButton
import com.yldefonso.lavacampus.ui.components.LavaTextField
import com.yldefonso.lavacampus.ui.components.SelectorCantidad
import com.yldefonso.lavacampus.ui.components.fechaLarga
import com.yldefonso.lavacampus.ui.components.formatoCantidad
import com.yldefonso.lavacampus.ui.components.soles
import com.yldefonso.lavacampus.ui.components.textoPrecio
import com.yldefonso.lavacampus.viewmodel.NuevoPedidoViewModel
import com.yldefonso.lavacampus.viewmodel.regla

@Composable
fun NuevoPedidoScreen(
    vm: NuevoPedidoViewModel,
    onAtras: () -> Unit,
    onPedidoCreado: () -> Unit
) {
    val recibiendo by vm.recibiendo.collectAsState()
    val servicios by vm.servicios.collectAsState()
    val cantidades by vm.cantidades.collectAsState()
    val nota by vm.nota.collectAsState()
    val resumen by vm.resumen.collectAsState()
    val total by vm.total.collectAsState()
    val fecha by vm.fechaEntrega.collectAsState()
    val puedeConfirmar by vm.puedeConfirmar.collectAsState()
    val guardando by vm.guardando.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val creado by vm.pedidoCreado.collectAsState()

    LaunchedEffect(creado) { if (creado) onPedidoCreado() }

    Scaffold(
        topBar = { AppTopBar(titulo = "Nuevo pedido", onAtras = onAtras) },
        bottomBar = {
            Surface(tonalElevation = 3.dp) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    mensaje?.let {
                        Text(it, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold)
                    }
                    LavaButton(
                        texto = "Confirmar pedido",
                        onClick = vm::confirmar,
                        enabled = puedeConfirmar && !guardando
                    )
                }
            }
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (!recibiendo) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "La lavandería no está recibiendo pedidos",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Text("Elige tus servicios", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

            servicios.forEach { s ->
                val regla = s.regla()
                val valor = cantidades[s.id] ?: 0.0
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (valor > 0.0) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        IconoServicio(icono = s.icono)
                        Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                            Text(s.nombre, fontWeight = FontWeight.SemiBold)
                            Text(s.textoPrecio(), style = MaterialTheme.typography.bodySmall)
                            Text("Entrega en ${s.horasEntrega} h", style = MaterialTheme.typography.labelSmall)
                        }
                        SelectorCantidad(
                            valor = valor,
                            paso = regla.paso,
                            minimo = regla.minimo,
                            maximo = regla.maximo,
                            onValorChange = { nuevo -> vm.cambiarCantidad(s.id, nuevo - valor) }
                        )
                    }
                }
            }

            LavaTextField(valor = nota, onValorChange = vm::cambiarNota, etiqueta = "Nota (opcional)")

            // Resumen
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Resumen", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    if (resumen.isEmpty()) {
                        Text("Elige al menos un servicio para ver el total.", style = MaterialTheme.typography.bodyMedium)
                    } else {
                        resumen.forEach { l ->
                            Row {
                                Text(
                                    "${l.servicio.nombre} · ${formatoCantidad(l.cantidad, l.servicio.unidad)}",
                                    modifier = Modifier.weight(1f)
                                )
                                Text(soles(l.subtotal))
                            }
                        }
                        HorizontalDivider()
                        Row {
                            Text("Total", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            Text(soles(total), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                        fecha?.let {
                            Text(
                                "Entrega estimada: ${fechaLarga(it.toString())}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}