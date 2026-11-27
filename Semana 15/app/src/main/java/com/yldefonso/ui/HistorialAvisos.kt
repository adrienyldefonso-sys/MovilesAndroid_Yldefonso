// [A] Commit 8: tarjeta del historial. No conoce el ViewModel: recibe la lista ya lista para mostrar.
package com.yldefonso.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yldefonso.model.Aviso

@Composable
fun HistorialAvisos(avisos: List<Aviso>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Historial",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            if (avisos.isEmpty()) {
                Text("Sin avisos todavía")
            } else {
                // take(20): solo los 20 más recientes. Como la lista ya viene con lo más nuevo arriba,
                // se muestran en ese orden. Column + forEach (no LazyColumn): la pantalla ya hace scroll.
                avisos.take(20).forEach { aviso ->
                    Column {
                        // Etiqueta de origen: Botón, Recordatorio o Modo avión
                        Text(
                            text = aviso.origen.texto,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = aviso.titulo,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = aviso.texto)
                        Text(
                            text = aviso.hora,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}