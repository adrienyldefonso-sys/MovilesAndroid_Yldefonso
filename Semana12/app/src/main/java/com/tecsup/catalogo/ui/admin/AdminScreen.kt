package com.tecsup.catalogo.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.catalogo.ui.UiState
import com.tecsup.catalogo.ui.components.Cargando
import com.tecsup.catalogo.ui.components.ErrorView

@Composable
fun AdminScreen(factory: ViewModelProvider.Factory, onVolver: () -> Unit) {
    val vm: AdminViewModel = viewModel(factory = factory)
    val estado by vm.estado.collectAsStateWithLifecycle()
    val ocupado by vm.ocupado.collectAsStateWithLifecycle()
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()

    AdminContent(
        estado = estado,
        ocupado = ocupado,
        mensaje = mensaje,
        onSincronizar = { vm.sincronizar() },
        onLimpiar = { vm.limpiarCache() },
        onReintentar = { vm.cargarTotales() },
        onMensajeMostrado = { vm.mensajeMostrado() },
        onVolver = onVolver
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminContent(
    estado: UiState<AdminResumen>,
    ocupado: Boolean,
    mensaje: String?,
    onSincronizar: () -> Unit,
    onLimpiar: () -> Unit,
    onReintentar: () -> Unit,
    onMensajeMostrado: () -> Unit,
    onVolver: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var confirmarLimpiar by rememberSaveable { mutableStateOf(false) }

    // Muestra el resultado de sincronizar o limpiar y lo marca como mostrado.
    LaunchedEffect(mensaje) {
        if (mensaje != null) {
            snackbarHostState.showSnackbar(mensaje)
            onMensajeMostrado()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Admin") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when (estado) {
                UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorView(estado.mensaje, onReintentar)
                is UiState.Exito -> Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TarjetaTotal("Productos guardados", estado.datos.totalGuardado)
                    TarjetaTotal("Favoritos", estado.datos.totalFavoritos)

                    Button(
                        onClick = onSincronizar,
                        enabled = !ocupado,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (ocupado) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                        else Text("Sincronizar")
                    }
                    OutlinedButton(
                        onClick = { confirmarLimpiar = true },
                        enabled = !ocupado,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Limpiar caché")
                    }
                }
            }
        }
    }

    if (confirmarLimpiar) {
        AlertDialog(
            onDismissRequest = { confirmarLimpiar = false },
            title = { Text("Limpiar caché") },
            text = {
                Text("Se borrarán los productos guardados y también los favoritos. Podrás volver a descargar los productos al sincronizar.")
            },
            confirmButton = {
                TextButton(onClick = {
                    confirmarLimpiar = false
                    onLimpiar()
                }) { Text("Limpiar") }
            },
            dismissButton = {
                TextButton(onClick = { confirmarLimpiar = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun TarjetaTotal(titulo: String, valor: Int) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium)
            Text(
                text = valor.toString(),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}