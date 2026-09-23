package com.yldefonso.clinicasaludplus.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Diálogo interactivo para calificar la atención de una cita médica completada.
 *
 * @param onDismiss Acción a ejecutar al cerrar o cancelar el diálogo.
 * @param onConfirm Acción a ejecutar al confirmar la calificación seleccionada (1 a 5).
 */
@Composable
fun RatingDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
) {
    // Estado local para almacenar la cantidad de estrellas seleccionadas (0 = ninguna, 1 a 5)
    var estrellasSeleccionadas by remember { mutableStateOf(0) }

    AlertDialog(
        // Cierra el diálogo al tocar fuera de él o presionar el botón de atrás
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Califica tu atención",
                style = MaterialTheme.typography.titleMedium,
            )
        },
        text = {
            // Contenedor horizontal centrado para mostrar las 5 estrellas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Iteramos del 1 al 5 para generar cada estrella del rating
                for (i in 1..5) {
                    val seleccionada = i <= estrellasSeleccionadas
                    Icon(
                        // Muestra la estrella rellena si está seleccionada, o en contorno si no lo está
                        imageVector = if (seleccionada) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Estrella $i",
                        // Aplica el color morado del tema a las estrellas seleccionadas
                        tint = if (seleccionada) MaterialTheme.colorScheme.primary else Color.Gray,
                        modifier = Modifier
                            .size(36.dp)
                            .padding(4.dp)
                            // Al hacer clic, actualiza el estado con el número de estrella tocada
                            .clickable { estrellasSeleccionadas = i },
                    )
                }
            }
        },
        confirmButton = {
            // Botón habilitado solo cuando el usuario haya elegido al menos 1 estrella (> 0)
            Button(
                onClick = { onConfirm(estrellasSeleccionadas) },
                enabled = estrellasSeleccionadas > 0,
            ) {
                Text("Calificar")
            }
        },
        dismissButton = {
            // Botón de cancelación que llama al callback onDismiss
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
    )
}
