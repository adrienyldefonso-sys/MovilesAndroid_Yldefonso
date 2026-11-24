package com.yldefonso.murocampus.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yldefonso.murocampus.viewmodel.PostsViewModel
import com.yldefonso.murocampus.viewmodel.UiState

// onAbrir: se llama con el id de la publicación tocada. La pantalla no sabe navegar, solo avisa.
@Composable
fun PantallaLista(vm: PostsViewModel, onAbrir: (Int) -> Unit) {
    val estado by vm.lista.collectAsStateWithLifecycle()

    when (val s = estado) {
        is UiState.Cargando -> Cargando()
        is UiState.Error -> ErrorConReintento(s.mensaje, onReintentar = { vm.cargar() })
        is UiState.Exito -> LazyColumn {
            items(s.datos) { post ->
                // Al tocar la tarjeta, avisamos a MainActivity con el id de la publicación.
                TarjetaPost(post, onClick = { onAbrir(post.id) })
            }
        }
    }
}