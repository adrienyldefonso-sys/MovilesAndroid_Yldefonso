package com.yldefonso.murocampus.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.item
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yldefonso.murocampus.viewmodel.PostsViewModel
import com.yldefonso.murocampus.viewmodel.UiState
import androidx.activity.compose.BackHandler as _BackHandlerAlias

@Composable
fun PantallaDetalle(vm: PostsViewModel, idPost: Int, onVolver: () -> Unit) {
    val estado by vm.detalle.collectAsStateWithLifecycle()
    // Controla si el diálogo de edición está abierto.
    var editando by remember { mutableStateOf(false) }

    LaunchedEffect(idPost) { vm.abrir(idPost) }
    BackHandler(onBack = onVolver)

    when (val s = estado) {
        is UiState.Cargando -> Cargando()
        is UiState.Error -> ErrorConReintento(s.mensaje, onReintentar = { vm.abrir(idPost) })
        is UiState.Exito -> LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        EtiquetaUsuario(s.datos.post.userId)
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = s.datos.post.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(s.datos.post.body, style = MaterialTheme.typography.bodyLarge)
                        // Botón que abre el diálogo de edición con PATCH.
                        TextButton(onClick = { editando = true }) {
                            Text("Editar título")
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Comentarios (${s.datos.comentarios.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(s.datos.comentarios) { c ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(
                            text = c.email,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(c.body, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            // Diálogo: solo existe mientras editando sea true.
            item {
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
}