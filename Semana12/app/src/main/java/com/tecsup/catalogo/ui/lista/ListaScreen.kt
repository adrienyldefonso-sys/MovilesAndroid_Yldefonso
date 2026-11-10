package com.tecsup.catalogo.ui.lista

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text("Smartphones") },
                scrollBehavior = scrollBehavior,
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
        val backgroundBrush = Brush.verticalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                MaterialTheme.colorScheme.background
            )
        )

        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .background(backgroundBrush)
        ) {
            AnimatedVisibility(visible = actualizando && estado is UiState.Exito) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
            }

            AnimatedVisibility(visible = aviso != null) {
                if (aviso != null) {
                    AvisoBanner(
                        texto = aviso,
                        onReintentar = onReintentar,
                        habilitado = !actualizando
                    )
                }
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
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "header_count") {
            Text(
                text = "${datos.size} equipos disponibles",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }

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
    val scale by animateFloatAsState(
        targetValue = if (item.esFavorito) 1.15f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "favoritoScale"
    )

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageGradient = Brush.linearGradient(
                listOf(
                    MaterialTheme.colorScheme.primaryContainer,
                    MaterialTheme.colorScheme.surfaceVariant
                )
            )

            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(imageGradient),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = item.imagenUrl,
                    contentDescription = item.titulo,
                    contentScale = ContentScale.Crop,
                    placeholder = rememberVectorPainter(Icons.Outlined.PhoneAndroid),
                    error = rememberVectorPainter(Icons.Outlined.PhoneAndroid),
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(
                Modifier
                    .weight(1f)
                    .padding(vertical = 2.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = item.subtitulo,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = item.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "US$ " + "%.2f".format(item.precio),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.width(8.dp))

            val favContainer = if (item.esFavorito) {
                MaterialTheme.colorScheme.tertiaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
            val favTint = if (item.esFavorito) {
                MaterialTheme.colorScheme.tertiary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }

            Surface(
                onClick = onFavorito,
                shape = CircleShape,
                color = favContainer,
                modifier = Modifier.size(44.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (item.esFavorito)
                            Icons.Filled.Favorite
                        else
                            Icons.Outlined.FavoriteBorder,
                        tint = favTint,
                        contentDescription = if (item.esFavorito)
                            "Quitar de favoritos"
                        else
                            "Marcar como favorito",
                        modifier = Modifier.scale(scale)
                    )
                }
            }
        }
    }
}

/**
 * Aviso de modo sin conexión.
 */
@Composable
private fun AvisoBanner(
    texto: String,
    onReintentar: () -> Unit,
    habilitado: Boolean
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.secondaryContainer,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.WifiOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Text(
                text = texto,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
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
}

/** Estado Vacío. */
@Composable
private fun VacioView() {
    Box(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.size(80.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.Inbox,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Text(
                text = "Sin elementos",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Text(
                text = "No hay equipos disponibles en este momento",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Datos de prueba utilizados únicamente para los Preview.
private val datosDePrueba = listOf(
    Item(1, "Smartphone Pro Max Ultra", "Apple", null, 999.99, esFavorito = true),
    Item(2, "Galaxy Note Ultra 5G", "Samsung", null, 899.50),
    Item(3, "Pixel 9 Pro XL", "Google", null, 799.00),
    Item(4, "Xiaomi 14 Ultra", "Xiaomi", null, 699.90),
    Item(5, "OnePlus 12", "OnePlus", null, 599.99)
)

@Preview(name = "Claro", showBackground = true)
@Preview(name = "Oscuro", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
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

@Preview(name = "Cargando", showBackground = true)
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

@Preview(name = "Error", showBackground = true)
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

@Preview(name = "Vacio", showBackground = true)
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
