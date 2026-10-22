package com.yldefonso.lavacampus.ui.administrador

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yldefonso.lavacampus.data.local.entity.Servicio
import com.yldefonso.lavacampus.ui.components.AppTopBar
import com.yldefonso.lavacampus.ui.components.IconoServicio
import com.yldefonso.lavacampus.ui.components.LavaButton
import com.yldefonso.lavacampus.ui.components.LavaTextField
import com.yldefonso.lavacampus.viewmodel.ServiciosViewModel

/** Crea (servicioId = 0) o edita un servicio. */
@Composable
fun FormularioServicioScreen(vm: ServiciosViewModel, servicioId: Int, onAtras: () -> Unit) {
    val servicios by vm.servicios.collectAsState()
    val original = servicios.firstOrNull { it.id == servicioId }
    val esperando = servicioId != 0 && original == null

    Scaffold(
        topBar = {
            AppTopBar(
                titulo = if (servicioId == 0) "Nuevo servicio" else "Editar servicio",
                onAtras = onAtras,
                claro = true
            )
        }
    ) { padding ->
        if (esperando) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Cargando servicio…")
            }
        } else {
            FormularioContenido(vm, original, onAtras, Modifier.padding(padding))
        }
    }
}

@Composable
private fun FormularioContenido(
    vm: ServiciosViewModel,
    original: Servicio?,
    onAtras: () -> Unit,
    modifier: Modifier
) {
    val mensaje by vm.mensaje.collectAsState()

    var nombre by remember(original) { mutableStateOf(original?.nombre ?: "") }
    var unidad by remember(original) { mutableStateOf(original?.unidad ?: "KG") }
    var precio by remember(original) {
        mutableStateOf(original?.precio?.let { "%.2f".format(java.util.Locale.US, it) } ?: "")
    }
    var horas by remember(original) { mutableStateOf(original?.horasEntrega ?: 24) }
    var descripcion by remember(original) { mutableStateOf(original?.descripcion ?: "") }
    var icono by remember(original) { mutableStateOf(original?.icono ?: "wash") }
    var activo by remember(original) { mutableStateOf(original?.activo ?: true) }

    val primario = MaterialTheme.colorScheme.primary

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LavaTextField(
            valor = nombre,
            onValorChange = { nombre = it; vm.limpiarMensaje() },
            etiqueta = "Nombre del servicio"
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Etiqueta("Se cobra por")
            OpcionesPildora(
                opciones = listOf("KG" to "Kilo", "PRENDA" to "Prenda", "UNIDAD" to "Unidad"),
                seleccion = unidad,
                onSeleccion = { unidad = it }
            )
        }

        LavaTextField(
            valor = precio,
            onValorChange = { precio = it; vm.limpiarMensaje() },
            etiqueta = "Precio (S/)",
            teclado = KeyboardType.Decimal
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Etiqueta("Tiempo de entrega")
            OpcionesPildora(
                opciones = listOf(24 to "24 h", 48 to "48 h", 72 to "72 h"),
                seleccion = horas,
                onSeleccion = { horas = it }
            )
        }

        LavaTextField(
            valor = descripcion,
            onValorChange = { descripcion = it },
            etiqueta = "Descripción"
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Etiqueta("Ícono")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf("wash", "iron", "shirt").forEach { opcion ->
                    val seleccionado = opcion == icono
                    val forma = RoundedCornerShape(16.dp)
                    Box(
                        Modifier
                            .weight(1f)
                            .height(64.dp)
                            .clip(forma)
                            .background(
                                if (seleccionado) primario.copy(alpha = 0.12f)
                                else MaterialTheme.colorScheme.surface
                            )
                            .border(
                                width = if (seleccionado) 2.dp else 1.dp,
                                color = if (seleccionado) primario
                                else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                                shape = forma
                            )
                            .clickable { icono = opcion },
                        contentAlignment = Alignment.Center
                    ) {
                        IconoServicio(icono = opcion, tamano = 32.dp)
                    }
                }
            }
        }

        // Fila "Servicio activo"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Servicio activo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Los estudiantes pueden pedirlo",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
            Switch(
                checked = activo,
                onCheckedChange = { activo = it }
            )
        }

        mensaje?.let {
            Text(it, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold)
        }

        Spacer(Modifier.height(4.dp))

        // Botón Principal
        LavaButton(
            texto = if (original == null) "Crear servicio" else "Guardar servicio",
            onClick = {
                vm.guardar(
                    Servicio(
                        id = original?.id ?: 0,
                        nombre = nombre.trim(),
                        descripcion = descripcion.trim(),
                        unidad = unidad,
                        precio = precio.replace(',', '.').toDoubleOrNull() ?: 0.0,
                        horasEntrega = horas,
                        activo = activo,
                        icono = icono
                    ),
                    onGuardado = onAtras
                )
            }
        )

        // Botón Cancelar
        OutlinedButton(
            onClick = onAtras,
            border = BorderStroke(1.dp, primario),
            shape = RoundedCornerShape(50),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Cancelar",
                fontWeight = FontWeight.Bold,
                color = primario,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun Etiqueta(texto: String) {
    Text(
        texto,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun <T> OpcionesPildora(
    opciones: List<Pair<T, String>>,
    seleccion: T,
    onSeleccion: (T) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        opciones.forEach { (valor, texto) ->
            val seleccionado = valor == seleccion
            val primario = MaterialTheme.colorScheme.primary

            Surface(
                onClick = { onSeleccion(valor) },
                shape = RoundedCornerShape(50),
                color = if (seleccionado) primario else MaterialTheme.colorScheme.surface,
                border = if (seleccionado) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier.padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = texto,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
                        color = if (seleccionado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}