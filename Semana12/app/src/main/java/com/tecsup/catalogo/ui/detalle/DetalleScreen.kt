package com.tecsup.catalogo.ui.detalle

import android.content.res.Configuration
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.outlined.LocalShipping
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
                title = { Text("Apple Store") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Precio total",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "S/ " + "%.2f".format(item.precio),
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Button(
                                onClick = onFavorito,
                                shape = CircleShape,
                                modifier = Modifier.height(50.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                ) {
                                    Icon(
                                        imageVector = if (item.esFavorito) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                        contentDescription = if (item.esFavorito) "Quitar de favoritos" else "Marcar favorito",
                                        modifier = Modifier.scale(scale)
                                    )
                                    Text(
                                        text = if (item.esFavorito) "En favoritos" else "Agregar a bolsa",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.LocalShipping,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Envío gratis · Entrega estimada en 1-2 días hábiles",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
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
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (estado) {
                UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorView(estado.mensaje, onReintentar)
                is UiState.Exito -> ContenidoAppleStyle(estado.datos)
            }
        }
    }
}

@Composable
private fun ContenidoAppleStyle(item: Item) {
    var selectedFinish by rememberSaveable { mutableStateOf("Grafito") }
    var selectedCapacity by rememberSaveable { mutableStateOf("256GB") }

    val finishes = listOf("Grafito", "Plata", "Oro", "Azul Sierra")
    val capacities = listOf("128GB", "256GB", "512GB")

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        // Hero Image Container (Apple Store Product Showcase)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .clip(MaterialTheme.shapes.extraLarge)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                            MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                ),
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
                    .padding(32.dp)
            )
        }

        // Title and Subtitle
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
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
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Rating and Stock
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

        // Apple Store Finish Selector ("Choose your finish")
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Elige tu acabado.",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                finishes.chunked(2).forEach { rowFinishes ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowFinishes.forEach { finish ->
                            val isSelected = finish == selectedFinish
                            val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                            val containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface

                            Surface(
                                onClick = { selectedFinish = finish },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp),
                                shape = MaterialTheme.shapes.medium,
                                border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor),
                                color = containerColor
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = finish,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Apple Store Capacity Selector ("Choose your capacity")
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Elige tu capacidad.",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                capacities.chunked(3).forEach { rowCaps ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowCaps.forEach { cap ->
                            val isSelected = cap == selectedCapacity
                            val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                            val containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface

                            Surface(
                                onClick = { selectedCapacity = cap },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(64.dp),
                                shape = MaterialTheme.shapes.medium,
                                border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor),
                                color = containerColor
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = cap,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "S/ " + "%.2f".format(item.precio + if (cap == "512GB") 200.0 else if (cap == "256GB") 100.0 else 0.0),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Description Card
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Descripción del producto",
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
