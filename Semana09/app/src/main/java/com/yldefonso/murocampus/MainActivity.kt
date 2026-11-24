package com.yldefonso.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yldefonso.murocampus.data.PostRepositoryRemote
import com.yldefonso.murocampus.data.remote.RetrofitClient
import com.yldefonso.murocampus.ui.PantallaCrear
import com.yldefonso.murocampus.ui.PantallaDetalle
import com.yldefonso.murocampus.ui.PantallaLista
import com.yldefonso.murocampus.ui.theme.MuroCampusTheme
import com.yldefonso.murocampus.viewmodel.PostsViewModel
import com.yldefonso.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {

    private val vm: PostsViewModel by viewModels {
        PostsViewModelFactory(PostRepositoryRemote(RetrofitClient.api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MuroCampusTheme {
                PantallaBase(vm)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaBase(vm: PostsViewModel) {
    // Estado de navegación. rememberSaveable: sobrevive a la rotación.
    var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) } // null = lista
    var creando by rememberSaveable { mutableStateOf(false) }             // true = pantalla crear

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MuroCampus") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            // El + solo aparece en la lista: no en el detalle ni en crear.
            if (!creando && idSeleccionado == null) {
                FloatingActionButton(onClick = {
                    vm.limpiarCrear() // formulario nuevo, sin el "Publicado" anterior
                    creando = true
                }) {
                    Text("+")
                }
            }
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            val id = idSeleccionado
            // Orden importa: primero crear, luego lista o detalle.
            when {
                creando -> PantallaCrear(vm, onVolver = { creando = false })
                id == null -> PantallaLista(vm, onAbrir = { idSeleccionado = it })
                else -> PantallaDetalle(vm, idPost = id, onVolver = { idSeleccionado = null })
            }
        }
    }
}