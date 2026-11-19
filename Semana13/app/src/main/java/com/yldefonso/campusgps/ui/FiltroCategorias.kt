package com.yldefonso.campusgps.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.yldefonso.campusgps.ui.components.IconosCampus
import com.yldefonso.campusgps.ui.theme.Dimens
import com.yldefonso.campusgps.ui.theme.EstudioColor
import com.yldefonso.campusgps.ui.theme.EstudioFondo
import com.yldefonso.campusgps.ui.theme.ComidaColor
import com.yldefonso.campusgps.ui.theme.ComidaFondo
import com.yldefonso.campusgps.ui.theme.EventosColor
import com.yldefonso.campusgps.ui.theme.EventosFondo
import com.yldefonso.campusgps.ui.theme.ServiciosColor
import com.yldefonso.campusgps.ui.theme.ServiciosFondo
import com.yldefonso.campusgps.ui.theme.OutlineColor
import com.yldefonso.campusgps.ui.theme.SurfaceCard
import com.yldefonso.campusgps.ui.theme.TextPrimary

/**
 * [G] Filtros reutilizables de CampusGPS.
 *
 * Cada categoría se presenta como un chip con:
 * - Fondo blanco.
 * - Contorno visible.
 * - Icono vectorial.
 * - Color de selección por categoría.
 *
 * No administra estado interno.
 */
@Composable
fun FiltroCategorias(
    categorias: List<String>,
    categoriasActivas: Set<String>,
    onToggleCategoria: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = Dimens.espacioS,
            vertical = Dimens.espacioXs
        ),
        horizontalArrangement = Arrangement.spacedBy(
            Dimens.espacioS
        )
    ) {

        items(
            items = categorias,
            key = { it }
        ) { categoria ->

            val estaActiva =
                categoriasActivas.contains(categoria)

            val estilo = obtenerEstiloFiltro(categoria)

            FilterChip(
                selected = estaActiva,

                // Mantiene la acción original del filtro.
                onClick = {
                    onToggleCategoria(categoria)
                },

                modifier = Modifier.heightIn(
                    min = Dimens.alturaMinimaTactil
                ),

                shape = RoundedCornerShape(14.dp),

                label = {
                    Text(
                        text = categoria,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (estaActiva) {
                            FontWeight.SemiBold
                        } else {
                            FontWeight.Medium
                        }
                    )
                },

                leadingIcon = {
                    Icon(
                        imageVector = estilo.icono,
                        contentDescription = null,
                        tint = estilo.color,
                        modifier = Modifier.size(20.dp)
                    )
                },

                // Fondo blanco en reposo y pastel al seleccionar.
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = SurfaceCard,
                    labelColor = TextPrimary,
                    selectedContainerColor = estilo.fondo,
                    selectedLabelColor = TextPrimary
                ),

                // Contorno visible incluso sin seleccionar.
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = estaActiva,
                    borderColor = OutlineColor,
                    selectedBorderColor = estilo.color,
                    borderWidth = 1.2.dp,
                    selectedBorderWidth = 1.8.dp
                ),

                // [G] Sombra sutil para distinguir los filtros del fondo.
                // Compatible con la versión actual de Material 3.
                elevation = FilterChipDefaults.filterChipElevation(
                    elevation = 2.dp,
                    pressedElevation = 1.dp,
                    focusedElevation = 2.dp,
                    hoveredElevation = 3.dp
                )
            )
        }
    }
}

/**
 * [G] Datos exclusivamente visuales de cada categoría.
 */
private data class EstiloFiltro(
    val icono: ImageVector,
    val color: Color,
    val fondo: Color
)

/**
 * [G] Relaciona cada categoría con su icono y paleta.
 * Reutiliza los colores centralizados en Color.kt.
 */
private fun obtenerEstiloFiltro(
    categoria: String
): EstiloFiltro {

    return when (categoria.trim().lowercase()) {

        "estudio", "biblioteca" -> EstiloFiltro(
            icono = IconosCampus.Biblioteca,
            color = EstudioColor,
            fondo = EstudioFondo
        )

        "comida", "cafetería", "cafeteria" -> EstiloFiltro(
            icono = IconosCampus.Cafe,
            color = ComidaColor,
            fondo = ComidaFondo
        )

        "eventos", "auditorio" -> EstiloFiltro(
            icono = IconosCampus.Calendario,
            color = EventosColor,
            fondo = EventosFondo
        )

        "servicios" -> EstiloFiltro(
            icono = IconosCampus.Servicios,
            color = ServiciosColor,
            fondo = ServiciosFondo
        )

        else -> EstiloFiltro(
            icono = IconosCampus.Navegacion,
            color = EstudioColor,
            fondo = EstudioFondo
        )
    }
}