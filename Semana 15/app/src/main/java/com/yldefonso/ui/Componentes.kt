// Componentes reutilizables del rediseño: tarjeta con acento, botones con degradado, chip de estado.
package com.yldefonso.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yldefonso.model.Origen
import com.yldefonso.ui.theme.Ambar
import com.yldefonso.ui.theme.Apagado
import com.yldefonso.ui.theme.Borde
import com.yldefonso.ui.theme.Cian
import com.yldefonso.ui.theme.Coral
import com.yldefonso.ui.theme.Fondo
import com.yldefonso.ui.theme.Indigo
import com.yldefonso.ui.theme.Superficie
import com.yldefonso.ui.theme.Texto
import com.yldefonso.ui.theme.TextoSuave

// Degradados reutilizables para acentos y botones.
val BrilloPrimario: Brush = Brush.horizontalGradient(listOf(Indigo, Cian))
val BrilloAlerta: Brush = Brush.horizontalGradient(listOf(Ambar, Coral))

// Tarjeta con borde suave, esquinas grandes y una franja de color arriba (el acento).
@Composable
fun GlassCard(acento: Brush, contenido: @Composable ColumnScope.() -> Unit) {
    val forma = RoundedCornerShape(24.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(Superficie)
            .border(1.dp, Borde, forma)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(acento)
        )
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            content = contenido
        )
    }
}

// Título y descripción corta de una tarjeta.
@Composable
fun SeccionTitulo(titulo: String, descripcion: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Texto
        )
        Text(
            text = descripcion,
            style = MaterialTheme.typography.bodySmall,
            color = TextoSuave
        )
    }
}

// Botón principal con degradado. Si está deshabilitado se ve gris y no responde.
@Composable
fun GradientButton(texto: String, enabled: Boolean, brush: Brush, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (enabled) brush else Brush.horizontalGradient(listOf(Apagado, Apagado)))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 22.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            color = if (enabled) Fondo else TextoSuave,
            fontWeight = FontWeight.Bold
        )
    }
}

// Botón secundario: solo borde de color.
@Composable
fun SecondaryButton(texto: String, enabled: Boolean, onClick: () -> Unit) {
    val color = if (enabled) Cian else Apagado
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, color, RoundedCornerShape(16.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 22.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            color = if (enabled) Cian else TextoSuave,
            fontWeight = FontWeight.Bold
        )
    }
}

// Chip de estado: un punto de color y el texto, sobre un fondo del mismo color con poca opacidad.
@Composable
fun StatusPill(texto: String, color: Color) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = texto,
            color = color,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

// Cada origen tiene su propio color: botón (índigo), recordatorio (ámbar) y modo avión (cian).
fun colorDeOrigen(origen: Origen): Color = when (origen) {
    Origen.BOTON -> Indigo
    Origen.RECORDATORIO -> Ambar
    Origen.MODO_AVION -> Cian
}