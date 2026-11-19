package com.yldefonso.campusgps.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * [G] Selector compacto del radio de cercanía (100m, 200m, 500m) y conteo de lugares.
 * Solo DIBUJA y avisa al ViewModel mediante la lambda [onRadioChange].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorRadio(
    radioActual: Int,
    onRadioChange: (Int) -> Unit,
    cantidadDentro: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val opciones = listOf(100, 200, 500)
        SingleChoiceSegmentedButtonRow {
            opciones.forEachIndexed { index, metros ->
                SegmentedButton(
                    selected = radioActual == metros,
                    onClick = { onRadioChange(metros) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = opciones.size)
                ) {
                    Text("$metros m")
                }
            }
        }
        Spacer(Modifier.height(2.dp))
        Text(
            text = "$cantidadDentro lugares dentro de $radioActual m",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
