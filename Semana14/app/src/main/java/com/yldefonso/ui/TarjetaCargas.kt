
package com.yldefonso.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.yldefonso.model.CargaUi
import com.yldefonso.viewmodel.TareasViewModel

// Paleta independiente para evitar conflictos con otras tarjetas.
private object CargasColors {
    val Azul = Color(0xFF2563EB)
    val AzulOscuro = Color(0xFF172554)
    val AzulClaro = Color(0xFFEFF6FF)
    val Texto = Color(0xFF0F172A)
    val Secundario = Color(0xFF64748B)
    val Borde = Color(0xFFE2E8F0)
    val Verde = Color(0xFF15803D)
    val VerdeClaro = Color(0xFFDCFCE7)
    val Rojo = Color(0xFFB91C1C)
    val RojoClaro = Color(0xFFFEE2E2)
    val Blanco = Color.White
}

/**
 * Tarjeta para demostrar la diferencia entre ejecutar
 * dos operaciones secuencialmente y en paralelo.
 *
 * Las operaciones son administradas por el ViewModel.
 * Este componente únicamente presenta sus estados.
 */
@Composable
fun TarjetaCargas(vm: TareasViewModel) {

    // Estados reales provenientes del ViewModel.
    val carga by vm.carga.collectAsState()
    val simularError by vm.simularError.collectAsState()

    val cargando = carga is CargaUi.Cargando

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = CargasColors.Blanco
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

            // Encabezado de la tarjeta.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(CargasColors.AzulClaro),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⇄",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = CargasColors.Azul
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Dos cargas",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = CargasColors.Texto
                    )

                    Text(
                        text = "Comparación de ejecución",
                        fontSize = 12.sp,
                        color = CargasColors.Secundario
                    )
                }
            }

            // Descripción de los dos modos disponibles.
            Text(
                text = "Compara el tiempo de ejecución de dos operaciones realizadas de forma secuencial o simultánea.",
                fontSize = 13.sp,
                color = CargasColors.Secundario
            )

            // Interruptor para simular errores.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CargasColors.AzulClaro)
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Simular error",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CargasColors.Texto
                    )

                    Text(
                        text = if (simularError) {
                            "Simulación activada"
                        } else {
                            "Operaciones normales"
                        },
                        fontSize = 11.sp,
                        color = CargasColors.Secundario
                    )
                }

                Switch(
                    checked = simularError,
                    onCheckedChange = {
                        vm.cambiarSimularError(it)
                    },
                    enabled = !cargando,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = CargasColors.Blanco,
                        checkedTrackColor = CargasColors.Azul,
                        uncheckedTrackColor = CargasColors.Borde
                    )
                )
            }

            // Botones para seleccionar el modo de ejecución.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                OutlinedButton(
                    onClick = {
                        vm.cargarUnaPorUna()
                    },
                    enabled = !cargando,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Una por una",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                }

                Button(
                    onClick = {
                        vm.cargarALaVez()
                    },
                    enabled = !cargando,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CargasColors.Azul,
                        contentColor = CargasColors.Blanco
                    )
                ) {
                    Text(
                        text = "A la vez",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Representación visual del estado de ejecución.
            when (val estado = carga) {

                is CargaUi.Inicial -> {

                    EstadoCargas(
                        titulo = "Esperando ejecución",
                        descripcion = "Selecciona un modo para comenzar.",
                        colorFondo = CargasColors.AzulClaro,
                        colorTexto = CargasColors.AzulOscuro
                    )
                }

                is CargaUi.Cargando -> {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(CargasColors.AzulClaro)
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        CircularProgressIndicator(
                            modifier = Modifier.size(38.dp),
                            color = CargasColors.Azul,
                            strokeWidth = 4.dp
                        )

                        Text(
                            text = "Procesando cargas...",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = CargasColors.AzulOscuro
                        )

                        Text(
                            text = "Espera mientras se completan las operaciones.",
                            fontSize = 12.sp,
                            color = CargasColors.Secundario,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                is CargaUi.Exito -> {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(CargasColors.VerdeClaro)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(CargasColors.Verde),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "✓",
                                    color = CargasColors.Blanco,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                text = "Operaciones completadas",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = CargasColors.Verde
                            )
                        }

                        // Datos obtenidos del resultado real.
                        ResultadoCargaFila(
                            etiqueta = "Modo",
                            valor = estado.modo.toString()
                        )

                        ResultadoCargaFila(
                            etiqueta = "Perfil",
                            valor = estado.perfil.toString()
                        )

                        ResultadoCargaFila(
                            etiqueta = "Pedidos",
                            valor = estado.pedidos.toString()
                        )

                        // Tiempo medido por el ViewModel.
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(
                                text = "Tiempo total",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = CargasColors.Verde
                            )

                            Text(
                                text = "${estado.milisegundos} ms",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = CargasColors.Verde
                            )
                        }
                    }
                }

                is CargaUi.Error -> {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(CargasColors.RojoClaro)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Text(
                            text = "No se completó la operación",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = CargasColors.Rojo
                        )

                        Text(
                            text = estado.mensaje,
                            fontSize = 12.sp,
                            color = CargasColors.Rojo
                        )

                        // Reintenta utilizando la ejecución simultánea.
                        OutlinedButton(
                            onClick = {
                                vm.cargarALaVez()
                            },
                            enabled = !cargando,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Reintentar operación",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Información orientativa del laboratorio.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CargasColors.AzulClaro)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Text(
                    text = "ⓘ",
                    fontSize = 20.sp,
                    color = CargasColors.Azul
                )

                Text(
                    text = "La ejecución una por una tarda aproximadamente 5000 ms. Compara ambos modos para observar la diferencia.",
                    fontSize = 11.sp,
                    color = CargasColors.Secundario
                )
            }
        }
    }
}

/**
 * Componente reutilizable para mostrar un estado informativo.
 */
@Composable
private fun EstadoCargas(
    titulo: String,
    descripcion: String,
    colorFondo: Color,
    colorTexto: Color
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colorFondo)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        Text(
            text = titulo,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = colorTexto
        )

        Text(
            text = descripcion,
            fontSize = 12.sp,
            color = colorTexto,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Fila utilizada para mostrar los resultados de una carga.
 */
@Composable
private fun ResultadoCargaFila(
    etiqueta: String,
    valor: String
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = etiqueta,
            fontSize = 12.sp,
            color = CargasColors.Verde
        )

        Text(
            text = valor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = CargasColors.Verde
        )
    }
}
