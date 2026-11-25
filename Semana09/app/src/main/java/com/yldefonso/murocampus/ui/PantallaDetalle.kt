package com.yldefonso.murocampus.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yldefonso.murocampus.ui.theme.*
import com.yldefonso.murocampus.viewmodel.PostsViewModel
import com.yldefonso.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(vm: PostsViewModel, idPost: Int, onVolver: () -> Unit) {
    val estado by vm.detalle.collectAsStateWithLifecycle()
    // Controla si el diálogo de edición está abierto.
    var editando by remember { mutableStateOf(false) }

    LaunchedEffect(idPost) { vm.abrir(idPost) }

    // El botón Atrás del sistema vuelve a la lista sin recargarla.
    BackHandler(onBack = onVolver)

    when (val s = estado) {
        is UiState.Cargando -> Cargando()
        is UiState.Error -> ErrorConReintento(s.mensaje, onReintentar = { vm.abrir(idPost) })
        is UiState.Exito -> LazyColumn(
            contentPadding = PaddingValues(horizontal = Medidas.L, vertical = Medidas.L),
            verticalArrangement = Arrangement.spacedBy(Medidas.S)
        ) {
            // Tarjeta principal: borde índigo suave, etiqueta, título y texto completo.
            item {
                Card(
                    shape = MuroShapes.medium,
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, IndigoSuave),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(Medidas.L)) {
                        EtiquetaUsuario(s.datos.post.userId)
                        Spacer(Modifier.height(Medidas.M))
                        Text(
                            text = s.datos.post.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium,
                            color = TextoPrimario
                        )
                        Spacer(Modifier.height(Medidas.S))
                        Text(
                            text = s.datos.post.body,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextoSecundario
                        )
                        TextButton(onClick = { editando = true }) {
                            Text("Editar título", color = IndigoProfundo, fontWeight = FontWeight.Medium)
                        }
                        // El diálogo vive dentro de la tarjeta, que siempre está visible.
                        if (editando) {
                            EditarTituloDialog(
                                vm = vm,
                                idPost = idPost,
                                tituloActual = s.datos.post.title,
                                onCerrar = { editando = false }
                            )
                        }
                    }
                }
            }

            // Encabezado de comentarios con contador en ámbar.
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Medidas.S),
                    modifier = Modifier.padding(top = Medidas.S)
                ) {
                    Text(
                        text = "Comentarios",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = IndigoProfundo
                    )
                    Surface(shape = FormaPildora, color = Ambar) {
                        Text(
                            text = "${s.datos.comentarios.size}",
                            color = AmbarOscuro,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Una tarjeta por comentario: avatar con la inicial, correo en índigo y texto.
            items(s.datos.comentarios) { c ->
                Card(
                    shape = MuroShapes.small,
                    colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(Modifier.padding(Medidas.M), verticalAlignment = Alignment.Top) {
                        AvatarLetra(c.email.firstOrNull() ?: '?', colorUsuario(c.id))
                        Spacer(Modifier.width(Medidas.M))
                        Column(Modifier.fillMaxWidth()) {
                            Text(
                                text = c.email,
                                color = IndigoProfundo,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(Modifier.height(Medidas.XS))
                            Text(c.body, style = MaterialTheme.typography.bodyMedium, color = TextoPrimario)
                        }
                    }
                }
            }
        }
    }
}

// Avatar circular con una letra, para comentarios (no tienen número de usuario).
@Composable
private fun AvatarLetra(letra: Char, color: Color) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(letra.uppercase(), color = Color.White, fontWeight = FontWeight.Medium)
    }
}