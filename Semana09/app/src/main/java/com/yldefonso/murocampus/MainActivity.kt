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
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.yldefonso.murocampus.data.PostRepositoryRemote
import com.yldefonso.murocampus.data.local.AppDatabase
import com.yldefonso.murocampus.data.remote.RetrofitClient
import com.yldefonso.murocampus.ui.PantallaCrear
import com.yldefonso.murocampus.ui.PantallaDetalle
import com.yldefonso.murocampus.ui.PantallaLista
import com.yldefonso.murocampus.ui.theme.Ambar
import com.yldefonso.murocampus.ui.theme.AmbarOscuro
import com.yldefonso.murocampus.ui.theme.IndigoProfundo
import com.yldefonso.murocampus.ui.theme.MuroCampusTheme
import com.yldefonso.murocampus.viewmodel.PostsViewModel
import com.yldefonso.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {

    // Base de datos local (Room). Se crea una sola vez.
    private val db by lazy { AppDatabase.crear(applicationContext) }

    private val vm: PostsViewModel by viewModels {
        PostsViewModelFactory(PostRepositoryRemote(RetrofitClient.api, db.postDao()))
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
    var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
    var creando by rememberSaveable { mutableStateOf(false) }

    // La cabecera cambia según la pantalla: título y flecha de volver en las subpantallas.
    val enSubpantalla = creando || idSeleccionado != null
    val titulo = when {
        creando -> "Nueva publicación"
        idSeleccionado != null -> "Publicación"
        else -> "MuroCampus"
    }

    // Un solo punto para volver: lo usan la flecha de la cabecera y las pantallas.
    val volver: () -> Unit = {
        if (creando) {
            creando = false
        } else {
            idSeleccionado = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titulo, fontWeight = FontWeight.Medium) },
                navigationIcon = {
                    if (enSubpantalla) {
                        IconButton(onClick = volver) {
                            Text("‹", fontSize = 28.sp)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = IndigoProfundo,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            // El + solo aparece en la lista.
            if (!enSubpantalla) {
                FloatingActionButton(
                    onClick = {
                        vm.limpiarCrear()
                        creando = true
                    },
                    containerColor = Ambar,
                    contentColor = AmbarOscuro
                ) {
                    Text("+", fontSize = 26.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            val id = idSeleccionado
            when {
                creando -> PantallaCrear(vm, onVolver = volver)
                id == null -> PantallaLista(vm, onAbrir = { idSeleccionado = it })
                else -> PantallaDetalle(vm, idPost = id, onVolver = volver)
            }
        }
    }
}