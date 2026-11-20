
package com.yldefonso.ui

import androidx.compose.foundation.background
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
 * Paleta de colores de la tarjeta de progreso.
 * Es privada para evitar conflictos con otras tarjetas.
 */
private object ProgresoColors {
    val Azul = Color(0xFF2563EB)
    val AzulOscuro = Color(0xFF172554)
    val AzulClaro = Color(0xFFEFF6FF)
    val FondoBarra = Color(0xFFDBEAFE)
    val Texto = Color(0xFF0F172A)
    val Secundario = Color(0xFF64748B)
    val Verde = Color(0xFF15803D)
    val Blanco = Color.White
}

/**
 * Tarjeta de ejecución de tareas con progreso.
 *
 * Funcionalidades:
 * - Iniciar una tarea larga.
 * - Cancelar una tarea en ejecución.
 * - Visualizar el progreso en porcentaje.
 * - Mostrar los mensajes del ViewModel.
 * - Contar pulsaciones para comprobar que la UI responde.
 *
 * La lógica de ejecución permanece en TareasViewModel.
 */
@Composable
fun TarjetaProgreso(vm: TareasViewModel) {

    // Estados provenientes del ViewModel.
    val mensaje by vm.mensaje.collectAsState()
    val progreso by vm.progreso.collectAsState()
    val corriendo by vm.corriendo.collectAsState()

    // Contador local para la prueba de respuesta.
    var toques by remember {
        mutableIntStateOf(0)
    }

    // Limitamos el progreso al intervalo válido.
    val progresoSeguro = progreso.coerceIn(0f, 1f)
    val porcentaje = (progresoSeguro * 100).toInt()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = ProgresoColors.Blanco
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

            // Encabezado principal.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(ProgresoColors.AzulClaro),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "▶",
                        fontSize = 23.sp,
                        color = ProgresoColors.Azul
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Tarea con progreso",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = ProgresoColors.Texto
                    )

                    Text(
                        text = "Ejecución en tiempo real",
                        fontSize = 12.sp,
                        color = ProgresoColors.Secundario
                    )
                }
            }

            // Estado visual de la tarea.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                corriendo -> ProgresoColors.Azul
                                porcentaje == 100 -> ProgresoColors.Verde
                                else -> ProgresoColors.Secundario
                            }
                        )
                )

                Text(
                    text = when {
                        corriendo -> "Tarea en ejecución"
                        porcentaje == 100 -> "Tarea completada"
                        else -> "En espera"
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = ProgresoColors.Secundario
                )
            }

            // Barra de progreso y porcentaje.
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Progreso actual",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = ProgresoColors.Secundario
                    )

                    Text(
                        text = "$porcentaje%",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = ProgresoColors.AzulOscuro
                    )
                }

                // Progreso actualizado desde el StateFlow.
                LinearProgressIndicator(
                    progress = { progresoSeguro },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    color = ProgresoColors.Azul,
                    trackColor = ProgresoColors.FondoBarra
                )
            }

            // Mensaje del proceso.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(ProgresoColors.AzulClaro)
                    .padding(14.dp)
            ) {

                Text(
                    text = mensaje,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = ProgresoColors.AzulOscuro,
                    textAlign = TextAlign.Center
                )
            }

            // Botones para controlar la tarea.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Button(
                    onClick = {
                        vm.tareaLarga()
                    },
                    enabled = !corriendo,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ProgresoColors.Azul,
                        contentColor = ProgresoColors.Blanco
                    )
                ) {

                    Text(
                        text = if (corriendo) {
                            "Ejecutando..."
                        } else {
                            "Iniciar tarea"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                }

                OutlinedButton(
                    onClick = {
                        vm.cancelar()
                    },
                    enabled = corriendo,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {

                    Text(
                        text = "Cancelar",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            // Prueba de respuesta de la interfaz.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(ProgresoColors.AzulClaro)
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Text(
                    text = "Prueba de respuesta",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = ProgresoColors.Texto
                )

                Text(
                    text = "Comprueba que la pantalla responde mientras se ejecuta la tarea.",
                    fontSize = 12.sp,
                    color = ProgresoColors.Secundario
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Button(
                        onClick = {
                            toques++
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ProgresoColors.AzulOscuro
                        )
                    ) {

                        Text(
                            text = "Tócame",
                            fontSize = 12.sp
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {

                        Text(
                            text = "$toques",
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Bold,
                            color = ProgresoColors.AzulOscuro
                        )

                        Text(
                            text = "Pulsaciones",
                            fontSize = 11.sp,
                            color = ProgresoColors.Secundario
                        )
                    }
                }
            }
        }
    }
}
