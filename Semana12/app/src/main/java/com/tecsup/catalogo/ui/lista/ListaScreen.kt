
package com.tecsup.catalogo.ui.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.tecsup.catalogo.domain.model.Item
import com.tecsup.catalogo.ui.UiState
import com.tecsup.catalogo.ui.components.Cargando
import com.tecsup.catalogo.ui.components.ErrorView
import com.tecsup.catalogo.ui.theme.CatalogoTheme

/**
 * H6: la pantalla observa el estado del ViewModel y comunica los eventos.
 * H7: se agrega la navegación hacia la pantalla de favoritos.
 * H8: se incorpora el indicador de actualización y el aviso de modo sin conexión.
 * H12: se agrega la opción para cerrar sesión.
 * H13: se agrega el acceso al panel de administrador.
 */

@Composable
fun ListaScreen(
    factory: ViewModelProvider.Factory,
    onItemClick: (Int) -> Unit = {},
    onVerFavoritos: () -> Unit = {},
    onCerrarSesion: () -> Unit = {},
    esAdmin: Boolean = false,
    onAdmin: () -> Unit = {}
) {
    val vm: ListaViewModel = viewModel(factory = factory)
    val estado by vm.estado.collectAsStateWithLifecycle()
    val aviso by vm.aviso.collectAsStateWithLifecycle()
    val actualizando by vm.actualizando.collectAsStateWithLifecycle()

    ListaContent(
        estado = estado,
        aviso = aviso,
        onItemClick = onItemClick,
        onFavorito = { id -> vm.alternarFavorito(id) },
        onReintentar = { vm.refrescar() },
        onVerFavoritos = onVerFavoritos,
        actualizando = actualizando,
        onCerrarSesion = onCerrarSesion,
        esAdmin = esAdmin,
        onAdmin = onAdmin
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaContent(
    estado: UiState<List<Item>>,
    aviso: String?,
    onItemClick: (Int) -> Unit,
    onFavorito: (Int) -> Unit,
    onReintentar: () -> Unit,
    onVerFavoritos: () -> Unit = {},
    actualizando: Boolean = false,
    onCerrarSesion: () -> Unit = {},
    esAdmin: Boolean = false,
    onAdmin: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Smartphones") },
                actions = {
                    IconButton(
                        onClick = onReintentar,
                        enabled = !actualizando
                    ) {
                        Icon(
                            Icons.Filled.Refresh,
                            contentDescription = "Actualizar"
                        )
                    }

                    IconButton(onClick = onVerFavoritos) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Ver favoritos"
                        )
                    }

                    if (esAdmin) {
                        IconButton(onClick = onAdmin) {
                            Icon(
                                Icons.Filled.AdminPanelSettings,
                                contentDescription = "Panel Admin"
                            )
                        }
                    }

                    IconButton(onClick = onCerrarSesion) {
                        Icon(
                            Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Cerrar sesión"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (actualizando && estado is UiState.Exito) {
                LinearProgressIndicator(
                    Modifier.fillMaxWidth()
                )
            }

            if (aviso != null) {
                AvisoBanner(
                    texto = aviso,
                    onReintentar = onReintentar,
                    habilitado = !actualizando
                )
            }

            Box(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (estado) {
                    UiState.Cargando -> Cargando()

                    is UiState.Error -> ErrorView(
                        estado.mensaje,
                        onReintentar
                    )

                    is UiState.Exito ->
                        if (estado.datos.isEmpty()) {
                            VacioView()
                        } else {
                            ListaItems(
                                estado.datos,
                                onItemClick,
                                onFavorito
                            )
                        }
                }
            }
        }
    }
}

@Composable
private fun ListaItems(
    datos: List<Item>,
    onItemClick: (Int) -> Unit,
    onFavorito: (Int) -> Unit
) {
    LazyColumn(
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(datos, key = { it.id }) { item ->
            ItemCard(
                item,
                onClick = { onItemClick(item.id) },
                onFavorito = { onFavorito(item.id) }
            )
        }
    }
}

@Composable
internal fun ItemCard(
    item: Item,
    onClick: () -> Unit,
    onFavorito: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.imagenUrl,
                contentDescription = item.titulo,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = item.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = item.subtitulo,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "US$ " + "%.2f".format(item.precio),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onFavorito) {
                Icon(
                    imageVector = if (item.esFavorito)
                        Icons.Filled.Favorite
                    else
                        Icons.Outlined.FavoriteBorder,
                    contentDescription = if (item.esFavorito)
                        "Quitar de favoritos"
                    else
                        "Marcar como favorito"
                )
            }
        }
    }
}

/**
 * Aviso de modo sin conexión (R08).
 * La lista se ve con lo guardado y se puede reintentar.
 */
@Composable
private fun AvisoBanner(
    texto: String,
    onReintentar: () -> Unit,
    habilitado: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(start = 12.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.weight(1f)
        )

        TextButton(
            onClick = onReintentar,
            enabled = habilitado
        ) {
            Text("Reintentar")
        }
    }
}

/** Estado Vacío (R03). */
@Composable
private fun VacioView() {
    Box(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No hay elementos para mostrar",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

// Datos de prueba utilizados únicamente para los Preview.
private val datosDePrueba = listOf(
    Item(1, "Smartphone Demo 1", "Marca A", null, 299.99),
    Item(2, "Smartphone Demo 2", "Marca B", null, 449.50, esFavorito = true),
    Item(3, "Smartphone Demo 3", "Marca C", null, 199.00),
    Item(4, "Smartphone Demo 4", "Marca A", null, 899.90),
    Item(5, "Smartphone Demo 5", "Marca D", null, 129.99)
)

@Preview(showBackground = true)
@Composable
private fun PreviewExito() {
    CatalogoTheme {
        ListaContent(
            UiState.Exito(datosDePrueba),
            null,
            {},
            {},
            {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCargando() {
    CatalogoTheme {
        ListaContent(
            UiState.Cargando,
            null,
            {},
            {},
            {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewError() {
    CatalogoTheme {
        ListaContent(
            UiState.Error("No hay conexión a internet"),
            null,
            {},
            {},
            {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewVacio() {
    CatalogoTheme {
        ListaContent(
            UiState.Exito(emptyList()),
            null,
            {},
            {},
            {}
        )
    }
}
