package com.tecsup.catalogo.ui.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.catalogo.data.repository.ItemRepository
import com.tecsup.catalogo.domain.model.Item
import com.tecsup.catalogo.ui.UiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritosViewModel(
    private val repo: ItemRepository
) : ViewModel() {

    // Room vuelve a emitir cada vez que se marca o desmarca un favorito, así que la pantalla se actualiza sola.
    val estado: StateFlow<UiState<List<Item>>> = repo.favoritos
        .map<List<Item>, UiState<List<Item>>> { UiState.Exito(it) }
        .catch { emit(UiState.Error("No se pudieron leer los favoritos")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Cargando)

    fun alternarFavorito(id: Int) {
        viewModelScope.launch { repo.alternarFavorito(id) }
    }
}