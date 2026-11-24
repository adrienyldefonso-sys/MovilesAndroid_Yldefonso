package com.yldefonso.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yldefonso.murocampus.viewmodel.PostsViewModel
import com.yldefonso.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(vm: PostsViewModel, onAbrir: (Int) -> Unit) {
    val estado by vm.lista.collectAsStateWithLifecycle()
    val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()

    Column {
        // Fila de chips desplazable. selected = true se ve relleno (el filtro activo).
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = filtro == null,
                    onClick = { vm.filtrar(null) },
                    label = { Text("Todos") }
                )
            }
            items((1..10).toList()) { uid ->
                FilterChip(
                    selected = filtro == uid,
                    onClick = { vm.filtrar(uid) },
                    label = { Text("Usuario $uid") }
                )
            }
        }

        // weight(1f): ocupa el espacio que sobra debajo de los chips.
        // Sin esto, LazyColumn dentro de Column tendría altura infinita y la app se cierra.
        Box(Modifier.weight(1f)) {
            when (val s = estado) {
                is UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorConReintento(s.mensaje, onReintentar = { vm.cargar() })
                is UiState.Exito -> if (s.datos.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Sin publicaciones")
                    }
                } else {
                    LazyColumn {
                        items(s.datos) { post ->
                            TarjetaPost(post, onClick = { onAbrir(post.id) })
                        }
                    }
                }
            }
        }
    }
}