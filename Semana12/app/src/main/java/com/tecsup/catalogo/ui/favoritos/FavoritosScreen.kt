package com.tecsup.catalogo.ui.favoritos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.catalogo.domain.model.Item
import com.tecsup.catalogo.ui.UiState
import com.tecsup.catalogo.ui.components.Cargando
import com.tecsup.catalogo.ui.lista.ItemCard

@Composable
fun FavoritosScreen(
    factory: ViewModelProvider.Factory,
    onItemClick: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val vm: FavoritosViewModel = viewModel(factory = factory)
    val estado by vm.estado.collectAsStateWithLifecycle()

    FavoritosContent(
        estado = estado,
        onItemClick = onItemClick,
        onFavorito = { id -> vm.alternarFavorito(id) },
        onVolver = onVolver
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosContent(
    estado: UiState<List<Item>>,
    onItemClick: (Int) -> Unit,
    onFavorito: (Int) -> Unit,
    onVolver: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favoritos") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when (estado) {
                UiState.Cargando -> Cargando()
                is UiState.Error -> Mensaje(estado.mensaje)
                is UiState.Exito ->
                    if (estado.datos.isEmpty()) Mensaje("Aún no tienes favoritos")
                    else LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(estado.datos, key = { it.id }) { item ->
                            ItemCard(
                                item = item,
                                onClick = { onItemClick(item.id) },
                                onFavorito = { onFavorito(item.id) }
                            )
                        }
                    }
            }
        }
    }
}

@Composable
private fun Mensaje(texto: String) {
    Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        Text(texto, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
    }
}