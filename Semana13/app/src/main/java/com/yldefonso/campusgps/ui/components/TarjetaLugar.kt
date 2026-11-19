package com.yldefonso.campusgps.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yldefonso.campusgps.R
import com.yldefonso.campusgps.model.Lugar
import com.yldefonso.campusgps.model.LugarConDistancia
import com.yldefonso.campusgps.ui.theme.Dimens
import com.yldefonso.campusgps.util.formatearDistancia
import com.yldefonso.campusgps.util.formatearTiempoAPie
import com.yldefonso.campusgps.util.obtenerIconoCategoria

/**
 * [G] Tarjeta reutilizable y sin estado de un lugar.
 *
 * Recibe el lugar, su distancia, estados de selección y radio,
 * y comunica la selección o solicitud de ruta mediante lambdas.
 *
 * No consulta ViewModel, repositorios ni calcula distancias.
 */
@Composable
fun TarjetaLugar(
    item: LugarConDistancia,
    esSeleccionado: Boolean,
    estaDentroRadio: Boolean,
    onClick: () -> Unit,
    onComoLlegar: (Lugar) -> Unit,
    modifier: Modifier = Modifier
) {
    // Colores visuales derivados de la categoría del lugar.
    val estilo = obtenerEstiloCategoria(item.lugar.categoria)

    val colorPrimario = MaterialTheme.colorScheme.primary
    val colorTexto = MaterialTheme.colorScheme.onSurface
    val colorSecundario = MaterialTheme.colorScheme.onSurfaceVariant

    val descIndicador = stringResource(R.string.desc_indicador_lugar)

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = if (esSeleccionado) 1.5.dp else 0.5.dp,
            color = if (esSeleccionado) {
                colorPrimario
            } else {
                MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (esSeleccionado) 2.dp else 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.espacioM),
            verticalArrangement = Arrangement.spacedBy(Dimens.espacioS)
        ) {

            // Primera fila: icono, nombre, categoría y distancia.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    Dimens.espacioM
                )
            ) {

                // Contenedor pastel del icono de categoría.
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            color = estilo.fondo,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .semantics {
                            contentDescription = descIndicador
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = obtenerIconoLugar(
                            nombre = item.lugar.nombre,
                            categoria = item.lugar.categoria
                        ),
                        contentDescription = null,
                        tint = estilo.color,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Nombre y categoría del lugar.
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = item.lugar.nombre,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = colorTexto,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = item.lugar.categoria,
                        style = MaterialTheme.typography.labelSmall,
                        color = colorSecundario,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Distancia y tiempo a pie ya calculados.
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = colorPrimario,
                            modifier = Modifier.size(14.dp)
                        )

                        Text(
                            text = item.metros?.let {
                                formatearDistancia(it)
                            } ?: stringResource(R.string.sin_calcular),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = colorPrimario
                        )
                    }

                    if (item.metros != null) {
                        Text(
                            text = formatearTiempoAPie(item.metros),
                            style = MaterialTheme.typography.labelSmall,
                            color = colorSecundario
                        )
                    }
                }
            }

            // Segunda fila: indicadores y acción de navegación.
            // Solo aparece cuando hay información adicional.
            if (
                estaDentroRadio ||
                (esSeleccionado && item.metros != null)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    if (estaDentroRadio) {
                        InsigniaInfo(
                            texto = stringResource(
                                R.string.etiqueta_dentro_radio
                            )
                        )
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    // Mantiene la condición original para mostrar ruta.
                    if (esSeleccionado && item.metros != null) {
                        TextButton(
                            onClick = {
                                onComoLlegar(item.lugar)
                            },
                            modifier = Modifier
                                .defaultMinSize(minHeight = 48.dp),
                            contentPadding = PaddingValues(
                                horizontal = 8.dp,
                                vertical = 4.dp
                            )
                        ) {
                            Text(
                                text = stringResource(
                                    R.string.btn_como_llegar
                                ),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * [G] Estilo visual de una categoría.
 * Solo contiene información de presentación.
 */
private data class EstiloCategoria(
    val icono: ImageVector,
    val color: Color,
    val fondo: Color
)

/**
 * [G] Asigna colores visuales por categoría.
 *
 * Reutiliza obtenerIconoCategoria para mantener la
 * correspondencia de iconos existente en el proyecto.
 */
@Composable
private fun obtenerEstiloCategoria(
    categoria: String
): EstiloCategoria {

    val icono = obtenerIconoCategoria(categoria)

    return when (categoria.trim().lowercase()) {

        "estudio" -> EstiloCategoria(
            icono = icono,
            color = Color(0xFF6547F5),
            fondo = Color(0xFFEAE4FF)
        )

        "comida" -> EstiloCategoria(
            icono = icono,
            color = Color(0xFF138A52),
            fondo = Color(0xFFDDF7E9)
        )

        "eventos" -> EstiloCategoria(
            icono = icono,
            color = Color(0xFF2563C9),
            fondo = Color(0xFFE3EEFF)
        )

        "servicios" -> EstiloCategoria(
            icono = icono,
            color = Color(0xFFBD7518),
            fondo = Color(0xFFFFF0DB)
        )

        else -> EstiloCategoria(
            icono = icono,
            color = MaterialTheme.colorScheme.primary,
            fondo = MaterialTheme.colorScheme.primaryContainer
        )
    }
}

/**
 * [G] Vista previa independiente con datos de ejemplo.
 */
@Preview(showBackground = true)
@Composable
fun TarjetaLugarPreview() {

    MaterialTheme {
        val lugarEjemplo = Lugar(
            1,
            "Biblioteca Central",
            "Estudio",
            -1.0,
            -1.0
        )

        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            TarjetaLugar(
                item = LugarConDistancia(
                    lugarEjemplo,
                    150f
                ),
                esSeleccionado = true,
                estaDentroRadio = true,
                onClick = {},
                onComoLlegar = {}
            )
        }
    }
}