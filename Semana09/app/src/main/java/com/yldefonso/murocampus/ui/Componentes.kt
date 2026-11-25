package com.yldefonso.murocampus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yldefonso.murocampus.model.Post
import com.yldefonso.murocampus.ui.theme.*

// Color del usuario según su número (ciclo de 5 colores).
fun colorUsuario(userId: Int): Color = ColoresUsuario[(userId - 1).mod(ColoresUsuario.size)]

// Avatar circular con el número del usuario.
@Composable
fun AvatarUsuario(userId: Int, tamano: androidx.compose.ui.unit.Dp = 40.dp) {
    Box(
        modifier = Modifier
            .size(tamano)
            .clip(CircleShape)
            .background(colorUsuario(userId)),
        contentAlignment = Alignment.Center
    ) {
        Text("$userId", color = Color.White, fontWeight = FontWeight.Medium)
    }
}

// Píldora con "Usuario N". sobreOscuro = true la invierte para usarla sobre la tarjeta índigo.
@Composable
fun EtiquetaUsuario(userId: Int, sobreOscuro: Boolean = false) {
    val fondo = if (sobreOscuro) Color.White else colorUsuario(userId)
    val texto = if (sobreOscuro) IndigoProfundo else Color.White
    Surface(shape = FormaPildora, color = fondo) {
        Text(
            text = "Usuario $userId",
            color = texto,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

// Tarjeta de publicación. destacada = true usa el fondo índigo para resaltar la primera.
@Composable
fun TarjetaPost(post: Post, destacada: Boolean = false, onClick: () -> Unit = {}) {
    Card(
        onClick = onClick,
        shape = MuroShapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (destacada) Indigo else FondoTarjeta
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Medidas.L, vertical = Medidas.S)
    ) {
        if (destacada) {
            Column(Modifier.padding(Medidas.L)) {
                EtiquetaUsuario(post.userId, sobreOscuro = true)
                Spacer(Modifier.height(Medidas.M))
                Text(
                    text = post.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Spacer(Modifier.height(Medidas.S))
                Text(
                    text = post.body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = IndigoClaro,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        } else {
            // Fila: avatar a la izquierda, texto a la derecha.
            Row(Modifier.padding(Medidas.L), verticalAlignment = Alignment.Top) {
                AvatarUsuario(post.userId)
                Spacer(Modifier.width(Medidas.M))
                Column(Modifier.fillMaxWidth()) {
                    Text(
                        text = post.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextoPrimario,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(Medidas.XS))
                    Text(
                        text = post.body,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoSecundario,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

// Indicador de carga dentro de una tarjeta centrada.
@Composable
fun Cargando() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            shape = MuroShapes.medium,
            colors = CardDefaults.cardColors(containerColor = FondoTarjeta)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = IndigoProfundo)
                Spacer(Modifier.height(Medidas.M))
                Text("Cargando publicaciones…", color = TextoSecundario)
            }
        }
    }
}

// Recuadro de error con el mensaje centrado y el botón Reintentar.
@Composable
fun ErrorConReintento(mensaje: String, onReintentar: () -> Unit) {
    Card(
        shape = MuroShapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
        modifier = Modifier
            .fillMaxWidth()
            .padding(Medidas.L)
    ) {
        Column(
            modifier = Modifier.padding(Medidas.L),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(Medidas.M))
            Button(
                onClick = onReintentar,
                shape = FormaPildora,
                colors = ButtonDefaults.buttonColors(containerColor = IndigoProfundo)
            ) {
                Text("Reintentar")
            }
        }
    }
}