package com.yldefonso.lavacampus.ui.administrador

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import com.yldefonso.lavacampus.ui.components.FilaSwitch
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

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        LavaTextField(
            valor = nombre,
            onValorChange = { nombre = it; vm.limpiarMensaje() },
            etiqueta = "Nombre del servicio"
        )

        Etiqueta("Se cobra por")
        Opciones(
            opciones = listOf("KG" to "Kilo", "PRENDA" to "Prenda", "UNIDAD" to "Unidad"),
            seleccion = unidad,
            onSeleccion = { unidad = it }
        )

        LavaTextField(
            valor = precio,
            onValorChange = { precio = it; vm.limpiarMensaje() },
            etiqueta = "Precio (S/)",
            teclado = KeyboardType.Decimal
        )

        Etiqueta("Tiempo de entrega")
        Opciones(
            opciones = listOf(24 to "24 h", 48 to "48 h", 72 to "72 h"),
            seleccion = horas,
            onSeleccion = { horas = it }
        )

        LavaTextField(valor = descripcion, onValorChange = { descripcion = it }, etiqueta = "Descripción")

        Etiqueta("Ícono")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf("wash", "iron", "shirt").forEach { opcion ->
                val seleccionado = opcion == icono
                val forma = RoundedCornerShape(14.dp)
                Box(
                    Modifier
                        .size(64.dp)
                        .clip(forma)
                        .background(
                            if (seleccionado) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surface
                        )
                        .border(
                            width = if (seleccionado) 2.dp else 1.dp,
                            color = if (seleccionado) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outlineVariant,
                            shape = forma
                        )
                        .clickable { icono = opcion },
                    contentAlignment = Alignment.Center
                ) { IconoServicio(icono = opcion, tamano = 36.dp) }
            }
        }

        FilaSwitch(titulo = "Servicio activo", marcado = activo, onCambio = { activo = it })

        mensaje?.let {
            Text(it, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold)
        }

        LavaButton(
            texto = if (original == null) "Crear servicio" else "Guardar cambios",
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
    }
}

@Composable
private fun Etiqueta(texto: String) {
    Text(texto, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> Opciones(opciones: List<Pair<T, String>>, seleccion: T, onSeleccion: (T) -> Unit) {
    SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
        opciones.forEachIndexed { i, (valor, texto) ->
            SegmentedButton(
                selected = valor == seleccion,
                onClick = { onSeleccion(valor) },
                shape = SegmentedButtonDefaults.itemShape(index = i, count = opciones.size)
            ) { Text(texto) }
        }
    }
}