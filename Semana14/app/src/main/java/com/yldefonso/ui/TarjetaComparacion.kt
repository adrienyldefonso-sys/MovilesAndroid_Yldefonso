
package com.yldefonso.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yldefonso.viewmodel.TareasViewModel

/**
 * Colores independientes de la tarjeta de comparación.
 */
private object ComparacionColors {
    val Azul = Color(0xFF2563EB)
    val AzulOscuro = Color(0xFF172554)
    val AzulClaro = Color(0xFFEFF6FF)
    val Texto = Color(0xFF0F172A)
    val Secundario = Color(0xFF64748B)
    val Verde = Color(0xFF15803D)
    val VerdeClaro = Color(0xFFDCFCE7)
    val Blanco = Color.White
}

/**
 * Compara los tiempos de ejecución de las cargas.
 *
 * Muestra:
 * - Últimos cinco tiempos secuenciales.
 * - Últimos cinco tiempos paralelos.
 * - Promedios de ejecución.
 * - Diferencia de tiempo entre ambos modos.
 * - Botón para limpiar el historial.
 *
 * Los datos se obtienen del ViewModel.
 */
@Composable
fun TarjetaComparacion(vm: TareasViewModel) {

    val comparacion by vm.comparacion.collectAsState()

    // Solo mostramos la tarjeta si existen resultados.
    if (
        comparacion.unaPorUna.isEmpty() &&
        comparacion.aLaVez.isEmpty()
    ) {
        return
    }

    // Recuperamos los últimos cinco registros.
    val ultimosUna = comparacion.unaPorUna.takeLast(5)
    val ultimosAlavez = comparacion.aLaVez.takeLast(5)

    val maxFilas = maxOf(
        ultimosUna.size,
        ultimosAlavez.size
    )

    // Calculamos los promedios reales.
    val promUna = comparacion.unaPorUna
        .takeIf { it.isNotEmpty() }
        ?.average()
        ?.toLong()

    val promAlavez = comparacion.aLaVez
        .takeIf { it.isNotEmpty() }
        ?.average()
        ?.toLong()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = ComparacionColors.Blanco
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Encabezado.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(ComparacionColors.AzulClaro),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "↗",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = ComparacionColors.Azul
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Comparación de tiempos",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = ComparacionColors.Texto
                    )

                    Text(
                        text = "Rendimiento de operaciones",
                        fontSize = 12.sp,
                        color = ComparacionColors.Secundario
                    )
                }
            }

            Text(
                text = "Consulta los últimos resultados y compara el rendimiento de cada modalidad.",
                fontSize = 13.sp,
                color = ComparacionColors.Secundario
            )

            // Cabecera de la tabla.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ComparacionColors.AzulOscuro)
                    .padding(12.dp)
            ) {

                Text(
                    text = "Una por una",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = ComparacionColors.Blanco,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "A la vez",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = ComparacionColors.Blanco,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Historial de los últimos cinco intentos.
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                for (i in 0 until maxFilas) {

                    val tiempoUna = ultimosUna
                        .getOrNull(i)
                        ?.let { "$it ms" }
                        ?: "—"

                    val tiempoAlavez = ultimosAlavez
                        .getOrNull(i)
                        ?.let { "$it ms" }
                        ?: "—"

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(ComparacionColors.AzulClaro)
                            .padding(12.dp)
                    ) {

                        Text(
                            text = tiempoUna,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            color = ComparacionColors.Texto
                        )

                        Text(
                            text = tiempoAlavez,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            color = ComparacionColors.Texto
                        )
                    }
                }
            }

            // Promedios de ambas modalidades.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ComparacionColors.AzulClaro)
                    .padding(12.dp)
            ) {

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Promedio secuencial",
                        fontSize = 11.sp,
                        color = ComparacionColors.Secundario,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = promUna?.let { "$it ms" } ?: "—",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ComparacionColors.AzulOscuro
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Promedio paralelo",
                        fontSize = 11.sp,
                        color = ComparacionColors.Secundario,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = promAlavez?.let { "$it ms" } ?: "—",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ComparacionColors.AzulOscuro
                    )
                }
            }

            // Mostramos la diferencia si existen ambos promedios.
            if (promUna != null && promAlavez != null) {

                val diferencia = promUna - promAlavez

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(ComparacionColors.VerdeClaro)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = if (diferencia >= 0) {
                            "Ahorro de tiempo"
                        } else {
                            "Diferencia de rendimiento"
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = ComparacionColors.Verde
                    )

                    Text(
                        text = "${kotlin.math.abs(diferencia)} ms",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = ComparacionColors.Verde
                    )

                    Text(
                        text = if (diferencia >= 0) {
                            "La ejecución simultánea fue más rápida en promedio."
                        } else {
                            "La ejecución secuencial fue más rápida en promedio."
                        },
                        fontSize = 12.sp,
                        color = ComparacionColors.Verde,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Limpieza del historial.
            Button(
                onClick = {
                    vm.limpiarComparacion()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ComparacionColors.Azul,
                    contentColor = ComparacionColors.Blanco
                )
            ) {

                Text(
                    text = "Limpiar historial",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
