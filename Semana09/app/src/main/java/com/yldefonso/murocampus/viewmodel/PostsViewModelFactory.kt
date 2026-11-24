package com.yldefonso.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yldefonso.murocampus.data.PostRepository

// [E] Entrega el repositorio al ViewModel
class PostsViewModelFactory(private val repo: PostRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PostsViewModel(repo) as T
    }
}
