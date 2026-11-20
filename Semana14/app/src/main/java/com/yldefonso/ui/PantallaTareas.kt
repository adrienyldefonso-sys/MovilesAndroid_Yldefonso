
package com.yldefonso.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.yldefonso.model.CargaUi
import com.yldefonso.model.PrimosUi
import com.yldefonso.viewmodel.TareasViewModel

/**
 * Colores de la pantalla principal CentroTareas.
 * Cada tarjeta administra sus propios colores.
 */
private object ControlColors {
    val Background = Color(0xFFF3F6FC)
    val HeaderBackground = Color(0xFF101D3A)
    val HeaderEyebrow = Color(0xFF9FB8FF)
    val HeaderSubtitle = Color(0xFFBEC9DF)
    val TextPrimary = Color(0xFF1B2844)
    val TextSecondary = Color(0xFF75829A)
    val StatusActive = Color(0xFF63DDB8)
    val StatusIdle = Color(0xFF9CB4E4)
}

/**
 * Pantalla principal.
 *
 * Organiza las tarjetas sin volver a declarar
 * las funciones que están en archivos separados.
 */
@Composable
fun PantallaTareas(
    vm: TareasViewModel,
    modifier: Modifier = Modifier
) {

    // Observamos los estados reales del ViewModel.
    val corriendo by vm.corriendo.collectAsState()
    val primos by vm.primos.collectAsState()
    val carga by vm.carga.collectAsState()

    // Contamos las operaciones activas.
    val tareasActivas = listOf(
        corriendo,
        primos is PrimosUi.Calculando,
        carga is CargaUi.Cargando
    ).count { it }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ControlColors.Background)
            .verticalScroll(rememberScrollState())
    ) {

        CabeceraControl(
            tareasActivas = tareasActivas
        )

        ContenidoOperaciones(
            vm = vm
        )
    }
}

/**
 * Encabezado de la aplicación.
 */
@Composable
private fun CabeceraControl(
    tareasActivas: Int
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ControlColors.HeaderBackground)
            .padding(
                start = 22.dp,
                end = 22.dp,
                top = 28.dp,
                bottom = 30.dp
            )
    ) {

        Text(
            text = "CENTRO DE CONTROL",
            color = ControlColors.HeaderEyebrow,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "CentroTareas",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Gestiona procesos y compara resultados.",
            color = ControlColors.HeaderSubtitle,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        IndicadorEstado(
            tareasActivas = tareasActivas
        )
    }
}

/**
 * Muestra la cantidad de procesos activos.
 */
@Composable
private fun IndicadorEstado(
    tareasActivas: Int
) {

    val hayActivas = tareasActivas > 0

    val colorIndicador = if (hayActivas) {
        ControlColors.StatusActive
    } else {
        ControlColors.StatusIdle
    }

    val textoEstado = if (hayActivas) {
        "$tareasActivas proceso(s) activo(s)"
    } else {
        "Sistema listo"
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(9.dp)
                .background(
                    color = colorIndicador,
                    shape = CircleShape
                )
        )

        Spacer(modifier = Modifier.width(9.dp))

        Text(
            text = textoEstado,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
        )
    }
}

/**
 * Contenedor de las cuatro tarjetas.
 *
 * Cada función se encuentra en su propio archivo:
 * TarjetaProgreso.kt
 * TarjetaPrimos.kt
 * TarjetaCargas.kt
 * TarjetaComparacion.kt
 */
@Composable
private fun ContenidoOperaciones(
    vm: TareasViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 22.dp
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Panel de operaciones",
            color = ControlColors.TextPrimary,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Ejecuta y supervisa cada tarea en tiempo real.",
            color = ControlColors.TextSecondary,
            fontSize = 13.sp
        )

        // Tarjeta 1: tarea larga con progreso.
        TarjetaProgreso(vm)

        // Tarjeta 2: cálculo de números primos.
        TarjetaPrimos(vm)

        // Tarjeta 3: cargas secuenciales y paralelas.
        TarjetaCargas(vm)

        // Tarjeta 4: comparación de tiempos.
        TarjetaComparacion(vm)

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "CentroTareas • Kotlin + Jetpack Compose",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 11.sp,
            color = ControlColors.TextSecondary
        )
    }
}
