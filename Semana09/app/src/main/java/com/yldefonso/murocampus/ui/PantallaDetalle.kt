package com.yldefonso.murocampus.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yldefonso.murocampus.viewmodel.PostsViewModel
import com.yldefonso.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(vm: PostsViewModel, idPost: Int, onVolver: () -> Unit) {
    val estado by vm.detalle.collectAsStateWithLifecycle()

    // LaunchedEffect(idPost): pide el detalle al entrar. Se vuelve a ejecutar solo si cambia el id.
    LaunchedEffect(idPost) { vm.abrir(idPost) }

    // BackHandler: el botón Atrás del sistema vuelve a la lista (sin recargarla).
    BackHandler(onBack = onVolver)

    when (val s = estado) {
        is UiState.Cargando -> Cargando()
        is UiState.Error -> ErrorConReintento(s.mensaje, onReintentar = { vm.abrir(idPost) })
        is UiState.Exito -> LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
            // item: un único elemento con el título, el texto y el encabezado de comentarios.
            item {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = s.datos.post.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(s.datos.post.body, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Comentarios (${s.datos.comentarios.size})",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            // items: una fila por comentario, con el correo en color primario.
            items(s.datos.comentarios) { c ->
                Column(Modifier.padding(vertical = 8.dp)) {
                    Text(
                        text = c.email,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(c.body, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}