package com.yldefonso.campusgps.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * [G] Define los iconos vectoriales de CampusGPS.
 *
 * No requiere dependencias adicionales.
 * Los iconos se dibujan como vectores escalables.
 */
private fun crearIcono(
    nombre: String,
    dibujar: PathBuilder.() -> Unit
): ImageVector {
    return ImageVector.Builder(
        name = nombre,
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.8f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            pathFillType = PathFillType.NonZero,
            pathBuilder = dibujar
        )
    }.build()
}

/**
 * [G] Colección de iconos personalizados.
 */
object IconosCampus {

    val Biblioteca: ImageVector by lazy {
        crearIcono("Biblioteca") {
            moveTo(12f, 6f)
            curveTo(9f, 4f, 6f, 4f, 3f, 5f)
            lineTo(3f, 19f)
            curveTo(6f, 18f, 9f, 18f, 12f, 20f)
            curveTo(15f, 18f, 18f, 18f, 21f, 19f)
            lineTo(21f, 5f)
            curveTo(18f, 4f, 15f, 4f, 12f, 6f)
            close()
            moveTo(12f, 6f)
            lineTo(12f, 20f)
        }
    }

    val Computadora: ImageVector by lazy {
        crearIcono("Computadora") {
            moveTo(4f, 4f)
            lineTo(20f, 4f)
            lineTo(20f, 16f)
            lineTo(4f, 16f)
            close()
            moveTo(12f, 16f)
            lineTo(12f, 20f)
            moveTo(8f, 20f)
            lineTo(16f, 20f)
        }
    }

    val Cafe: ImageVector by lazy {
        crearIcono("Cafe") {
            moveTo(4f, 8f)
            lineTo(17f, 8f)
            lineTo(17f, 14f)
            curveTo(17f, 18f, 14f, 20f, 10.5f, 20f)
            curveTo(7f, 20f, 4f, 18f, 4f, 14f)
            close()
            moveTo(17f, 9f)
            lineTo(19f, 9f)
            curveTo(23f, 9f, 23f, 15f, 17f, 15f)
            moveTo(8f, 3f)
            lineTo(8f, 6f)
            moveTo(13f, 3f)
            lineTo(13f, 6f)
        }
    }

    val Auditorio: ImageVector by lazy {
        crearIcono("Auditorio") {
            moveTo(3f, 5f)
            lineTo(21f, 5f)
            lineTo(21f, 17f)
            lineTo(3f, 17f)
            close()
            moveTo(12f, 17f)
            lineTo(12f, 21f)
            moveTo(8f, 21f)
            lineTo(16f, 21f)
            moveTo(8f, 13f)
            lineTo(11f, 10f)
            lineTo(14f, 12f)
            lineTo(17f, 8f)
        }
    }

    val Calendario: ImageVector by lazy {
        crearIcono("Calendario") {
            moveTo(4f, 5f)
            lineTo(20f, 5f)
            lineTo(20f, 21f)
            lineTo(4f, 21f)
            close()
            moveTo(8f, 3f)
            lineTo(8f, 7f)
            moveTo(16f, 3f)
            lineTo(16f, 7f)
            moveTo(4f, 10f)
            lineTo(20f, 10f)
            moveTo(8f, 14f)
            lineTo(10f, 14f)
            moveTo(14f, 14f)
            lineTo(16f, 14f)
        }
    }

    val Servicios: ImageVector by lazy {
        crearIcono("Servicios") {
            moveTo(14f, 5f)
            curveTo(17f, 3f, 21f, 6f, 19f, 10f)
            lineTo(10f, 19f)
            curveTo(8f, 21f, 5f, 21f, 4f, 19f)
            curveTo(3f, 17f, 4f, 15f, 6f, 14f)
            lineTo(15f, 5f)
            moveTo(6f, 17f)
            lineTo(7f, 18f)
        }
    }

    val Estacionamiento: ImageVector by lazy {
        crearIcono("Estacionamiento") {
            moveTo(6f, 3f)
            lineTo(6f, 21f)
            moveTo(6f, 4f)
            lineTo(13f, 4f)
            curveTo(21f, 4f, 21f, 14f, 13f, 14f)
            lineTo(6f, 14f)
        }
    }

    val Navegacion: ImageVector by lazy {
        crearIcono("Navegacion") {
            moveTo(21f, 3f)
            lineTo(10f, 21f)
            lineTo(8f, 13f)
            lineTo(3f, 10f)
            close()
        }
    }
}

/**
 * [G] Obtiene el icono más específico posible.
 *
 * Primero identifica el tipo de lugar por su nombre.
 * Si no encuentra coincidencia, utiliza su categoría.
 *
 * No modifica la lógica de ubicación ni de distancias.
 */
fun obtenerIconoLugar(
    nombre: String,
    categoria: String
): ImageVector {

    val nombreNormalizado = nombre.trim().lowercase()
    val categoriaNormalizada = categoria.trim().lowercase()

    return when {

        "biblioteca" in nombreNormalizado ->
            IconosCampus.Biblioteca

        "laboratorio" in nombreNormalizado ||
                "computo" in nombreNormalizado ||
                "cómputo" in nombreNormalizado ->
            IconosCampus.Computadora

        "cafeteria" in nombreNormalizado ||
                "cafetería" in nombreNormalizado ||
                "comedor" in nombreNormalizado ->
            IconosCampus.Cafe

        "auditorio" in nombreNormalizado ->
            IconosCampus.Auditorio

        "estacionamiento" in nombreNormalizado ||
                "parking" in nombreNormalizado ->
            IconosCampus.Estacionamiento

        categoriaNormalizada == "comida" ->
            IconosCampus.Cafe

        categoriaNormalizada == "eventos" ->
            IconosCampus.Calendario

        categoriaNormalizada == "servicios" ->
            IconosCampus.Servicios

        categoriaNormalizada == "estudio" ->
            IconosCampus.Biblioteca

        else -> Icons.Default.Place
    }
}

/**
 * [G] Componente reutilizable y sin estado.
 *
 * Recibe el nombre, categoría, tamaño y colores.
 * Dibuja un icono dentro de un contenedor redondeado.
 */
@Composable
fun IconoLugar(
    nombre: String,
    categoria: String,
    modifier: Modifier = Modifier,
    colorIcono: Color = Color(0xFF6547F5),
    colorFondo: Color = Color(0xFFEAE4FF)
) {
    Box(
        modifier = modifier
            .size(44.dp)
            .background(
                color = colorFondo,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = obtenerIconoLugar(nombre, categoria),
            contentDescription = null,
            tint = colorIcono,
            modifier = Modifier.size(24.dp)
        )
    }
}

/**
 * [G] Vista previa del icono de biblioteca.
 */
@Preview(showBackground = true)
@Composable
fun IconoLugarPreview() {
    IconoLugar(
        nombre = "Biblioteca Central",
        categoria = "Estudio"
    )
}