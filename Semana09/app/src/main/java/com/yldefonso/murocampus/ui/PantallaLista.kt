package com.yldefonso.murocampus.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yldefonso.murocampus.viewmodel.PostsViewModel
import com.yldefonso.murocampus.viewmodel.UiState

// La pantalla NO llama a la API: solo observa el estado del ViewModel y lo dibuja.
@Composable
fun PantallaLista(vm: PostsViewModel) {
    // Se actualiza sola cada vez que cambia el StateFlow; respeta el ciclo de vida.
    val estado by vm.lista.collectAsStateWithLifecycle()

    // when sobre el sealed interface: el compilador obliga a cubrir los tres casos.
    when (val s = estado) {
        is UiState.Cargando -> Cargando()
        is UiState.Error -> ErrorConReintento(s.mensaje, onReintentar = { vm.cargar() })
        is UiState.Exito -> LazyColumn {
            items(s.datos) { post ->
                TarjetaPost(post)
            }
        }
    }
}