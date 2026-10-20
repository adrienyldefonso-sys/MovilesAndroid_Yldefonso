package com.yldefonso.lavacampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yldefonso.lavacampus.model.Rol
import com.yldefonso.lavacampus.ui.navigation.NavLava
import com.yldefonso.lavacampus.ui.theme.LavaTheme
import com.yldefonso.lavacampus.viewmodel.LavaViewModelFactory
import com.yldefonso.lavacampus.viewmodel.SesionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val app = application as LavaApp
            val factory = remember { LavaViewModelFactory(app.repository, app.preferencias) }
            val sesionVm: SesionViewModel = viewModel(factory = factory)

            val usuario by sesionVm.usuarioActual.collectAsState()
            val oscuro by sesionVm.modoOscuro.collectAsState()
            val rolTema = if (usuario?.rol == Rol.ADMIN.name) Rol.ADMIN else Rol.CLIENTE

            LavaTheme(rol = rolTema, modoOscuro = oscuro) {
                NavLava(sesionVm = sesionVm, factory = factory)
            }
        }
    }
}