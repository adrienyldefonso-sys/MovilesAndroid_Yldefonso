package com.yldefonso.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yldefonso.model.CargaUi
import com.yldefonso.model.PrimosUi
import com.yldefonso.ui.theme.VerdeExito
import com.yldefonso.ui.theme.VerdeExitoTexto
import com.yldefonso.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareasViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CentroTareas", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TarjetaProgreso(vm)
            TarjetaPrimos(vm)
            TarjetaCargas(vm)
        }
    }
}

@Composable
private fun TarjetaProgreso(vm: TareasViewModel) {
    val mensaje by vm.mensaje.collectAsState()
    val progreso by vm.progreso.collectAsState()
    val corriendo by vm.corriendo.collectAsState()
    var toques by remember { mutableIntStateOf(0) }

    Tarjeta("Tarea con progreso") {
        // Prueba de vida: si deja de sumar, la pantalla está congelada
        Button(onClick = { toques++ }) {
            Text("Tócame: $toques")
        }
        // Iniciar se deshabilita mientras corre; Cancelar solo mientras corre
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { vm.tareaLarga() },
                enabled = !corriendo
            ) {
                Text("Iniciar")
            }
            OutlinedButton(
                onClick = { vm.cancelar() },
                enabled = corriendo
            ) {
                Text("Cancelar")
            }
        }
        // (si marca error, usa progress = progreso en vez de la lambda)
        LinearProgressIndicator(
            progress = { progreso },
            modifier = Modifier.fillMaxWidth()
        )
        Text("${(progreso * 100).toInt()} %")
        Text(mensaje)
    }
}

@Composable
private fun TarjetaPrimos(vm: TareasViewModel) {
    val primos by vm.primos.collectAsState()
    var texto by remember { mutableStateOf("500000") }
    var calculado by remember { mutableIntStateOf(0) } // N usado en el último cálculo

    val numero = texto.toIntOrNull()
    val valido = numero != null && numero >= 2
    val calculando = primos is PrimosUi.Calculando

    Tarjeta("Contar números primos") {
        // Solo acepta dígitos (máx. 9 para no desbordar Int)
        OutlinedTextField(
            value = texto,
            onValueChange = { nuevo -> texto = nuevo.filter { it.isDigit() }.take(9) },
            label = { Text("Hasta el número") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    // Ignora textos no numéricos o menores que 2
                    if (valido) {
                        calculado = numero!!
                        vm.contar(numero)
                    }
                },
                enabled = valido && !calculando
            ) {
                Text("Contar")
            }
            OutlinedButton(
                onClick = { vm.cancelarPrimos() },
                enabled = calculando
            ) {
                Text("Cancelar")
            }
        }
        when (val estado = primos) {
            is PrimosUi.Inicial -> Unit
            is PrimosUi.Calculando -> Text("Calculando...")
            is PrimosUi.Cancelado -> Text("Cancelado")
            is PrimosUi.Listo -> CuadroVerde {
                Text(
                    text = "Primos hasta $calculado: ${estado.cantidad}",
                    color = VerdeExitoTexto,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Tiempo: ${estado.milisegundos} ms",
                    color = VerdeExitoTexto,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun TarjetaCargas(vm: TareasViewModel) {
    val carga by vm.carga.collectAsState()
    val simularError by vm.simularError.collectAsState()
    val cargando = carga is CargaUi.Cargando

    Tarjeta("Dos cargas") {
        // Paso 7: interruptor para forzar el error en los pedidos
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Switch(
                checked = simularError,
                onCheckedChange = { vm.cambiarSimularError(it) },
                enabled = !cargando
            )
            Text("Simular error")
        }
        // Los botones se deshabilitan mientras carga (evita una segunda carga)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = { vm.cargarUnaPorUna() },
                enabled = !cargando
            ) {
                Text("Una por una")
            }
            Button(
                onClick = { vm.cargarALaVez() },
                enabled = !cargando
            ) {
                Text("A la vez")
            }
        }
        when (val estado = carga) {
            is CargaUi.Inicial -> Unit
            is CargaUi.Cargando -> CircularProgressIndicator()
            is CargaUi.Exito -> CuadroVerde {
                val estilo = MaterialTheme.typography.bodySmall
                Text(
                    "Modo: ${estado.modo}",
                    color = VerdeExitoTexto,
                    fontWeight = FontWeight.Bold
                )
                Text("Perfil: ${estado.perfil}", color = VerdeExitoTexto, style = estilo)
                Text("Pedidos: ${estado.pedidos}", color = VerdeExitoTexto, style = estilo)
                Text("Tiempo: ${estado.milisegundos} ms", color = VerdeExitoTexto, style = estilo)
            }
            // Paso 7: recuadro de error con botón Reintentar (vuelve a cargar A la vez)
            is CargaUi.Error -> {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = estado.mensaje,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp)
                    )
                }
                OutlinedButton(onClick = { vm.cargarALaVez() }) {
                    Text("Reintentar")
                }
            }
        }
        Text(
            text = "«Una por una» tarda ≈ 5000 ms",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

// Recuadro verde reutilizable para los resultados
@Composable
private fun CuadroVerde(contenido: @Composable () -> Unit) {
    Surface(
        color = VerdeExito,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            contenido()
        }
    }
}

@Composable
private fun Tarjeta(titulo: String, contenido: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )
            contenido()
        }
    }
}