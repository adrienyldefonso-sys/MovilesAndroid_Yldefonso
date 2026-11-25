package com.yldefonso.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yldefonso.murocampus.ui.theme.*
import com.yldefonso.murocampus.viewmodel.PostsViewModel
import com.yldefonso.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(vm: PostsViewModel, onAbrir: (Int) -> Unit) {
    val estado by vm.lista.collectAsStateWithLifecycle()
    val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()
    val hayMas by vm.hayMas.collectAsStateWithLifecycle()
    val cargandoMas by vm.cargandoMas.collectAsStateWithLifecycle()
    val errorMas by vm.errorMas.collectAsStateWithLifecycle()

    // Chip activo en ámbar, los demás en índigo suave.
    val colorChip = FilterChipDefaults.filterChipColors(
        containerColor = IndigoSuave,
        labelColor = IndigoProfundo,
        selectedContainerColor = Ambar,
        selectedLabelColor = AmbarOscuro
    )

    Column {
        LazyRow(
            contentPadding = PaddingValues(horizontal = Medidas.L, vertical = Medidas.M),
            horizontalArrangement = Arrangement.spacedBy(Medidas.S)
        ) {
            item {
                FilterChip(
                    selected = filtro == null,
                    onClick = { vm.filtrar(null) },
                    label = { Text("Todos") },
                    colors = colorChip
                )
            }
            items((1..10).toList()) { uid ->
                FilterChip(
                    selected = filtro == uid,
                    onClick = { vm.filtrar(uid) },
                    label = { Text("Usuario $uid") },
                    colors = colorChip
                )
            }
        }

        // weight(1f): la lista ocupa el resto de la pantalla sin altura infinita.
        Box(Modifier.weight(1f)) {
            when (val s = estado) {
                is UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorConReintento(s.mensaje, onReintentar = { vm.cargar() })
                is UiState.Exito -> if (s.datos.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Sin publicaciones", color = TextoSecundario)
                    }
                } else {
                    // bottom = 96.dp: deja espacio para el botón + sin tapar la última tarjeta.
                    LazyColumn(contentPadding = PaddingValues(bottom = 96.dp)) {
                        // La primera publicación se destaca, solo cuando se ven todas.
                        itemsIndexed(s.datos) { indice, post ->
                            TarjetaPost(
                                post = post,
                                destacada = indice == 0 && filtro == null,
                                onClick = { onAbrir(post.id) }
                            )
                        }
                        // Pie: error de la página (si lo hay) y el botón Cargar más.
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(Medidas.L),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                errorMas?.let {
                                    Text(it, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
                                    Spacer(Modifier.height(Medidas.S))
                                }
                                if (hayMas) {
                                    Button(
                                        onClick = { vm.cargarMas() },
                                        enabled = !cargandoMas,
                                        shape = FormaPildora,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Ambar,
                                            contentColor = AmbarOscuro
                                        )
                                    ) {
                                        Text(
                                            if (cargandoMas) "Cargando…" else "Cargar más",
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}