package com.yldefonso.tecsupfit.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/**
 * Diálogo de confirmación para cancelar una reserva.
 *
 * @param claseNombre Nombre de la clase/actividad reservada.
 * @param onDismiss Callback ejecutado al descartar o cerrar el diálogo.
 * @param onConfirm Callback ejecutado al confirmar la cancelación de la reserva.
 */
@Composable
fun CancelDialog(
    claseNombre: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    // AlertDialog de Material 3 para solicitar confirmación antes de realizar la cancelación
    AlertDialog(
        // Cierra el diálogo al presionar fuera de él o pulsar el botón 'Atrás'
        onDismissRequest = onDismiss,
        // Título principal del diálogo
        title = {
            Text(text = "Cancelar reserva")
        },
        // Mensaje descriptivo con interpolación del nombre de la clase
        text = {
            Text(
                text = "¿Seguro que deseas cancelar tu reserva de $claseNombre? Esta acción no se puede deshacer."
            )
        },
        // Botón de confirmación con color de contenedor de tipo error (destructivo)
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(text = "Sí, cancelar")
            }
        },
        // Botón secundario para descartar la cancelación y mantener la reserva
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = "No, mantener")
            }
        }
    )
}
