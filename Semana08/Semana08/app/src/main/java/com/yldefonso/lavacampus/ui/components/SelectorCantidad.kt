package com.yldefonso.lavacampus.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Control con botones − y +.
 * - Con valor 0 el servicio está apagado; "+" lo activa en [minimo].
 * - "−" por debajo de [minimo] vuelve a 0.
 * Devuelve el nuevo valor en [onValorChange].
 */
@Composable
fun SelectorCantidad(
    valor: Double,
    paso: Double,
    minimo: Double,
    maximo: Double,
    onValorChange: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        FilledTonalIconButton(
            onClick = {
                val nuevo = valor - paso
                onValorChange(if (nuevo < minimo) 0.0 else nuevo)
            },
            enabled = valor > 0.0
        ) { Icon(Icons.Filled.Remove, contentDescription = "Menos") }

        Text(
            text = if (valor % 1.0 == 0.0) "%.0f".format(valor) else "%.1f".format(valor),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(min = 48.dp)
        )

        FilledTonalIconButton(
            onClick = { onValorChange(if (valor <= 0.0) minimo else minOf(valor + paso, maximo)) },
            enabled = valor < maximo
        ) { Icon(Icons.Filled.Add, contentDescription = "Más") }
    }
}
