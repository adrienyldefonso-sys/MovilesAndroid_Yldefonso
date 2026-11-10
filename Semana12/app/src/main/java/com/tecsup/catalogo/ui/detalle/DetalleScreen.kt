package com.tecsup.catalogo.ui.detalle

import android.content.res.Configuration
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
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
                title = { Text("Detalle del Producto") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            if (estado is UiState.Exito) {
                val item = estado.datos
                val scale by animateFloatAsState(
                    targetValue = if (item.esFavorito) 1.15f else 1.0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "bottomFavScale"
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    tonalElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Precio",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "US$ " + "%.2f".format(item.precio),
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Button(
                            onClick = onFavorito,
                            shape = CircleShape,
                            modifier = Modifier.height(48.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Icon(
                                    imageVector = if (item.esFavorito) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = if (item.esFavorito) "Quitar de favoritos" else "Marcar favorito",
                                    modifier = Modifier.scale(scale)
                                )
                                Text(
                                    text = if (item.esFavorito) "En favoritos" else "Marcar favorito",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { padding ->
        Box(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
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
    ) {
        val heroGradient = Brush.verticalGradient(
            listOf(
                MaterialTheme.colorScheme.primaryContainer,
                MaterialTheme.colorScheme.surfaceVariant
            )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(heroGradient),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = item.imagenUrl,
                contentDescription = item.titulo,
                contentScale = ContentScale.Fit,
                placeholder = rememberVectorPainter(Icons.Outlined.PhoneAndroid),
                error = rememberVectorPainter(Icons.Outlined.PhoneAndroid),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-28).dp),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = item.subtitulo,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = item.titulo,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    item.rating?.let { rating ->
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "%.1f".format(rating),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    item.stock?.let { stock ->
                        val pocasUnidades = stock <= 5
                        val chipColor = if (pocasUnidades) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.secondaryContainer
                        val textColor = if (pocasUnidades) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSecondaryContainer

                        Surface(
                            shape = CircleShape,
                            color = chipColor
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                if (pocasUnidades) {
                                    Icon(
                                        imageVector = Icons.Outlined.Warning,
                                        contentDescription = null,
                                        tint = textColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Pocas unidades ($stock)",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = textColor
                                    )
                                } else {
                                    Text(
                                        text = "Stock: $stock",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = textColor
                                    )
                                }
                            }
                        }
                    }
                }

                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Descripción",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = item.descripcion ?: "Sin descripción disponible.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// Previews
private val itemPruebaDetalle = Item(
    id = 1,
    titulo = "Smartphone Pro Max Ultra 5G",
    subtitulo = "Apple",
    imagenUrl = null,
    precio = 1299.99,
    esFavorito = false,
    descripcion = "El smartphone más avanzado con pantalla OLED de 6.7 pulgadas, chip A18 Pro, cámara triple de 48MP y batería de larga duración.",
    stock = 3,
    rating = 4.8
)

private val itemPruebaStockNormal = itemPruebaDetalle.copy(stock = 25, esFavorito = true)

@Preview(name = "Stock Bajo (Claro)", showBackground = true)
@Preview(name = "Stock Bajo (Oscuro)", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDetalleStockBajo() {
    CatalogoTheme {
        DetalleContent(
            estado = UiState.Exito(itemPruebaDetalle),
            onVolver = {},
            onFavorito = {},
            onReintentar = {}
        )
    }
}

@Preview(name = "Stock Normal (Claro)", showBackground = true)
@Composable
private fun PreviewDetalleStockNormal() {
    CatalogoTheme {
        DetalleContent(
            estado = UiState.Exito(itemPruebaStockNormal),
            onVolver = {},
            onFavorito = {},
            onReintentar = {}
        )
    }
}
