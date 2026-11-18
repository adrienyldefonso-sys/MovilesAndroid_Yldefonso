package com.yldefonso.campusgps.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yldefonso.campusgps.model.TipoError
import com.yldefonso.campusgps.model.UbicacionUi

/**
 * [A] Paso 8: recuadro de error con el mensaje y, debajo, el botón que corresponde al tipo.
 * No conoce el ViewModel: el reintento llega como callback (onReintentar).
 */
@Composable
fun MensajeError(
    error: UbicacionUi.Error,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    // La View SÍ puede usar Context (solo el ViewModel tiene prohibido recibirlo).
    val contexto = LocalContext.current

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Recuadro con el color de error del tema.
        Surface(
            color = MaterialTheme.colorScheme.errorContainer,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = error.mensaje,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(12.dp)
            )
        }

        Spacer(Modifier.height(8.dp))

        // El botón depende del tipo de error.
        when (error.tipo) {
            // Permiso denegado: lleva a la pantalla de ajustes de ESTA app para activarlo a mano.
            TipoError.SIN_PERMISO -> OutlinedButton(onClick = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", contexto.packageName, null)
                }
                contexto.startActivity(intent)
            }) { Text("Abrir ajustes") }

            // GPS apagado u otro fallo: el usuario lo arregla y vuelve a intentar.
            TipoError.GPS_APAGADO, TipoError.OTRO ->
                OutlinedButton(onClick = onReintentar) { Text("Reintentar") }

            // Sin Play Services: reintentar no sirve, por eso no hay botón.
            TipoError.SIN_PLAY_SERVICES -> Unit
        }
    }
}