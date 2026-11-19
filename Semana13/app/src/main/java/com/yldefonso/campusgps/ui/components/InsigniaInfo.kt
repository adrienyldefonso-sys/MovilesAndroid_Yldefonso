package com.yldefonso.campusgps.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yldefonso.campusgps.ui.theme.Dimens

/**
 * [G] Insignia informativa reutilizable y compacta con texto, color e icono opcional.
 * @param texto Texto a mostrar en la insignia.
 * @param modifier Modificador opcional.
 * @param colorFondo Color de fondo de la insignia.
 * @param colorTexto Color del texto de la insignia.
 * @param icono Icono vectorial opcional a mostrar al inicio.
 */
@Composable
fun InsigniaInfo(
    texto: String,
    modifier: Modifier = Modifier,
    colorFondo: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
    colorTexto: Color = MaterialTheme.colorScheme.primary,
    icono: ImageVector? = null
) {
    Box(
        modifier = modifier
            .background(colorFondo, RoundedCornerShape(Dimens.espacioXs))
            .padding(horizontal = Dimens.espacioS, vertical = 3.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icono != null) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = colorTexto,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(Modifier.width(Dimens.espacioXs))
            }
            Text(
                text = texto,
                style = MaterialTheme.typography.labelSmall,
                color = colorTexto,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InsigniaInfoPreview() {
    InsigniaInfo(texto = "Dentro del radio", icono = Icons.Default.Place)
}
