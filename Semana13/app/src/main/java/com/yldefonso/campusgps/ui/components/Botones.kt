
package com.yldefonso.campusgps.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yldefonso.campusgps.ui.theme.Dimens
import com.yldefonso.campusgps.ui.theme.Primary
import com.yldefonso.campusgps.ui.theme.PrimaryDark
import com.yldefonso.campusgps.ui.theme.PrimarySoft
import com.yldefonso.campusgps.ui.theme.PrimaryBorder
import com.yldefonso.campusgps.ui.theme.ButtonDisabledBackground
import com.yldefonso.campusgps.ui.theme.ButtonDisabledContent
import com.yldefonso.campusgps.ui.theme.ButtonDisabledBorder

/**
 * [G] Botón principal reutilizable de CampusGPS.
 *
 * Recibe texto, evento de pulsación, estado habilitado
 * e icono opcional.
 *
 * No contiene estado interno ni lógica de negocio.
 */
@Composable
fun BototonPrincipal(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icono: ImageVector? = Icons.Default.LocationOn
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = Dimens.alturaMinimaTactil),
        shape = RoundedCornerShape(Dimens.radioEsquina),
        colors = ButtonDefaults.buttonColors(
            containerColor = Primary,
            contentColor = androidx.compose.ui.graphics.Color.White,
            disabledContainerColor = ButtonDisabledBackground,
            disabledContentColor = ButtonDisabledContent
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 1.dp,
            pressedElevation = 0.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icono != null) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(Modifier.width(Dimens.espacioS))
            }

            Text(
                text = texto,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * [G] Botón secundario reutilizable de CampusGPS.
 *
 * Utiliza un fondo lila, borde morado y un icono
 * vectorial de navegación.
 *
 * Recibe los datos y comunica el evento onClick.
 */
@Composable
fun BotonSecundario(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icono: ImageVector? = IconosCampus.Navegacion
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = Dimens.alturaMinimaTactil),
        shape = RoundedCornerShape(Dimens.radioEsquina),
        border = BorderStroke(
            width = 1.5.dp,
            color = if (enabled) PrimaryBorder
            else ButtonDisabledBorder
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = PrimarySoft,
            contentColor = PrimaryDark,
            disabledContainerColor = ButtonDisabledBackground,
            disabledContentColor = ButtonDisabledContent
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icono != null) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(Modifier.width(Dimens.espacioS))
            }

            Text(
                text = texto,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * [G] Vista previa de ambos botones reutilizables.
 *
 * Incluye el estado habilitado y deshabilitado
 * del botón secundario.
 */
@Preview(showBackground = true)
@Composable
fun BotonesPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(Dimens.espacioL),
            verticalArrangement = Arrangement.spacedBy(
                Dimens.espacioS
            )
        ) {
            BototonPrincipal(
                texto = "Obtener mi ubicación",
                onClick = {}
            )

            BotonSecundario(
                texto = "Ir al más cercano",
                onClick = {},
                enabled = true
            )

            BotonSecundario(
                texto = "Ir al más cercano",
                onClick = {},
                enabled = false
            )
        }
    }
}
