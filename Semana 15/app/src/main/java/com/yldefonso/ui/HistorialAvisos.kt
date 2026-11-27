// Historial en línea de tiempo: un punto de color por origen y el aviso al lado.
package com.yldefonso.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yldefonso.model.Aviso
import com.yldefonso.ui.theme.Ambar
import com.yldefonso.ui.theme.Coral
import com.yldefonso.ui.theme.Texto
import com.yldefonso.ui.theme.TextoSuave

@Composable
fun HistorialAvisos(avisos: List<Aviso>) {
    GlassCard(acento = Brush.horizontalGradient(listOf(Ambar, Coral))) {
        SeccionTitulo(
            titulo = "Historial",
            descripcion = "Los 20 avisos más recientes, con lo último arriba"
        )

        if (avisos.isEmpty()) {
            Text(
                text = "Sin avisos todavía",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSuave
            )
        } else {
            avisos.take(20).forEach { aviso ->
                ItemHistorial(aviso)
            }
        }
    }
}

// Una fila de la línea de tiempo: punto de color, etiqueta de origen, título, texto y hora.
@Composable
private fun ItemHistorial(aviso: Aviso) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .size(10.dp)
                .clip(CircleShape)
                .background(colorDeOrigen(aviso.origen))
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = aviso.origen.texto.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = colorDeOrigen(aviso.origen),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = aviso.hora,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextoSuave
                )
            }
            Text(
                text = aviso.titulo,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = Texto
            )
            Text(
                text = aviso.texto,
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSuave
            )
        }
    }
}