
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yldefonso.model.PrimosUi
import com.yldefonso.viewmodel.TareasViewModel

// Colores independientes para esta tarjeta.
private object PrimosColors {
    val Azul = Color(0xFF2563EB)
    val AzulOscuro = Color(0xFF172554)
    val AzulClaro = Color(0xFFEFF6FF)
    val Texto = Color(0xFF0F172A)
    val Secundario = Color(0xFF64748B)
    val Borde = Color(0xFFE2E8F0)
    val Verde = Color(0xFF15803D)
    val VerdeClaro = Color(0xFFDCFCE7)
    val Naranja = Color(0xFFB45309)
    val NaranjaClaro = Color(0xFFFFF7ED)
    val Blanco = Color.White
}

/**
 * Tarjeta para contar números primos.
 *
 * Permite:
 * - Ingresar un número límite.
 * - Validar la entrada.
 * - Iniciar el cálculo.
 * - Cancelar el procesamiento.
 * - Mostrar resultados y tiempo de ejecución.
 *
 * La lógica del cálculo permanece en el ViewModel.
 */
@Composable
fun TarjetaPrimos(vm: TareasViewModel) {

    // Observamos el estado del cálculo.
    val primos by vm.primos.collectAsState()

    // Número ingresado por el usuario.
    var texto by rememberSaveable {
        mutableStateOf("500000")
    }

    // Guardamos el límite del último cálculo iniciado.
    var calculado by rememberSaveable {
        mutableIntStateOf(0)
    }

    // Validación para evitar entradas incorrectas.
    val numero = texto.toIntOrNull()
    val valido = numero != null && numero >= 2

    // Identificamos si el proceso sigue ejecutándose.
    val calculando = primos is PrimosUi.Calculando

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = PrimosColors.Blanco
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
                        .background(PrimosColors.AzulClaro),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "#",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimosColors.Azul
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Contar números primos",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimosColors.Texto
                    )

                    Text(
                        text = "Procesamiento matemático",
                        fontSize = 12.sp,
                        color = PrimosColors.Secundario
                    )
                }
            }

            // Explicación breve del módulo.
            Text(
                text = "Calcula cuántos números primos existen hasta el límite que indiques.",
                fontSize = 13.sp,
                color = PrimosColors.Secundario
            )

            // Campo de entrada numérica.
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = "Número límite",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimosColors.Texto
                )

                OutlinedTextField(
                    value = texto,
                    onValueChange = { nuevo ->
                        // Acepta únicamente dígitos y máximo 9 caracteres.
                        texto = nuevo
                            .filter { it.isDigit() }
                            .take(9)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !calculando,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    placeholder = {
                        Text("Ejemplo: 500000")
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = PrimosColors.Blanco,
                        unfocusedContainerColor = PrimosColors.Blanco,
                        focusedIndicatorColor = PrimosColors.Azul,
                        unfocusedIndicatorColor = PrimosColors.Borde
                    ),
                    isError = texto.isNotEmpty() && !valido
                )

                // Mensaje de validación.
                if (!valido) {
                    Text(
                        text = "Ingresa un número válido mayor o igual a 2.",
                        fontSize = 11.sp,
                        color = PrimosColors.Naranja
                    )
                }
            }

            // Botones para iniciar y cancelar el cálculo.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Button(
                    onClick = {
                        if (valido && numero != null) {
                            calculado = numero
                            vm.contar(numero)
                        }
                    },
                    enabled = valido && !calculando,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimosColors.Azul,
                        contentColor = PrimosColors.Blanco
                    )
                ) {
                    Text(
                        text = if (calculando) {
                            "Calculando..."
                        } else {
                            "Contar primos"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                }

                OutlinedButton(
                    onClick = {
                        vm.cancelarPrimos()
                    },
                    enabled = calculando,
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

            // Representación del estado real del cálculo.
            when (val estado = primos) {

                is PrimosUi.Inicial -> {

                    EstadoPrimos(
                        titulo = "Listo para calcular",
                        descripcion = "Ingresa un límite y comienza el procesamiento.",
                        colorFondo = PrimosColors.AzulClaro,
                        colorTexto = PrimosColors.AzulOscuro
                    )
                }

                is PrimosUi.Calculando -> {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(PrimosColors.AzulClaro)
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        CircularProgressIndicator(
                            modifier = Modifier.size(38.dp),
                            color = PrimosColors.Azul,
                            strokeWidth = 4.dp
                        )

                        Text(
                            text = "Calculando números primos...",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimosColors.AzulOscuro
                        )

                        Text(
                            text = "El cálculo se ejecuta mientras la interfaz permanece disponible.",
                            fontSize = 12.sp,
                            color = PrimosColors.Secundario,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                is PrimosUi.Cancelado -> {

                    EstadoPrimos(
                        titulo = "Cálculo cancelado",
                        descripcion = "La operación fue detenida. Puedes iniciar otra.",
                        colorFondo = PrimosColors.NaranjaClaro,
                        colorTexto = PrimosColors.Naranja
                    )
                }

                is PrimosUi.Listo -> {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(PrimosColors.VerdeClaro)
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(PrimosColors.Verde),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "✓",
                                    color = PrimosColors.Blanco,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                text = "Cálculo completado",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimosColors.Verde
                            )
                        }

                        Text(
                            text = "Números primos encontrados",
                            fontSize = 12.sp,
                            color = PrimosColors.Verde
                        )

                        // Cantidad real obtenida del ViewModel.
                        Text(
                            text = "${estado.cantidad}",
                            fontSize = 34.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimosColors.Verde
                        )

                        Text(
                            text = "En el intervalo de 2 hasta $calculado",
                            fontSize = 12.sp,
                            color = PrimosColors.Verde
                        )

                        // Tiempo real que tardó la operación.
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "Tiempo de ejecución",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = PrimosColors.Verde
                            )

                            Text(
                                text = "${estado.milisegundos} ms",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimosColors.Verde
                            )
                        }
                    }
                }
            }

            // Nota informativa para el usuario.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(PrimosColors.AzulClaro)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Text(
                    text = "ⓘ",
                    fontSize = 20.sp,
                    color = PrimosColors.Azul
                )

                Text(
                    text = "Los números primos son mayores que 1 y solo tienen dos divisores positivos: 1 y ellos mismos.",
                    fontSize = 11.sp,
                    color = PrimosColors.Secundario
                )
            }
        }
    }
}

/**
 * Componente reutilizable para mostrar mensajes
 * del estado de procesamiento de números primos.
 */
@Composable
private fun EstadoPrimos(
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
