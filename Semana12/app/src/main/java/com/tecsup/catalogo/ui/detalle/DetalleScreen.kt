package com.tecsup.catalogo.ui.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.tecsup.catalogo.domain.model.Item
import com.tecsup.catalogo.ui.UiState
import com.tecsup.catalogo.ui.components.Cargando
import com.tecsup.catalogo.ui.components.ErrorView

@Composable
fun DetalleScreen(factory: ViewModelProvider.Factory, onVolver: () -> Unit) {
    val vm: DetalleViewModel = viewModel(factory = factory)
    val estado by vm.estado.collectAsStateWithLifecycle()

    DetalleContent(
        estado = estado,
        onVolver = onVolver,
        onFavorito = { vm.alternarFavorito() },
        onReintentar = { vm.cargar() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleContent(
    estado: UiState<Item>,
    onVolver: () -> Unit,
    onFavorito: () -> Unit,
    onReintentar: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (estado is UiState.Exito) {
                        IconButton(onClick = onFavorito) {
                            Icon(
                                imageVector = if (estado.datos.esFavorito) Icons.Filled.Favorite
                                else Icons.Outlined.FavoriteBorder,
                                contentDescription = if (estado.datos.esFavorito) "Quitar de favoritos"
                                else "Marcar como favorito"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when (estado) {
                UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorView(estado.mensaje, onReintentar)
                is UiState.Exito -> Contenido(estado.datos)
            }
        }
    }
}

@Composable
private fun Contenido(item: Item) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        AsyncImage(
            model = item.imagenUrl,
            contentDescription = item.titulo,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        Spacer(Modifier.height(16.dp))
        Text(item.titulo, style = MaterialTheme.typography.headlineSmall)
        Text(item.subtitulo, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Text(
            text = "US$ " + "%.2f".format(item.precio),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(12.dp))
        // Estos datos solo vienen en el segundo endpoint (/products/{id}).
        item.rating?.let { Text("Calificación: " + "%.1f".format(it) + " / 5") }
        item.stock?.let { Text("Stock disponible: $it unidades") }
        Spacer(Modifier.height(12.dp))
        Text("Descripción", style = MaterialTheme.typography.titleMedium)
        Text(item.descripcion ?: "Sin descripción", style = MaterialTheme.typography.bodyLarge)
    }
}