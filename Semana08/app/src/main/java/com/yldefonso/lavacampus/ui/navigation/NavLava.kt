package com.yldefonso.lavacampus.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yldefonso.lavacampus.model.Rol
import com.yldefonso.lavacampus.ui.administrador.ServiciosScreen
import com.yldefonso.lavacampus.ui.components.AppTopBar
import com.yldefonso.lavacampus.ui.estudiante.LoginScreen
import com.yldefonso.lavacampus.ui.estudiante.SplashScreen
import com.yldefonso.lavacampus.viewmodel.LavaViewModelFactory
import com.yldefonso.lavacampus.viewmodel.ServiciosViewModel
import com.yldefonso.lavacampus.viewmodel.SesionViewModel
import kotlinx.coroutines.launch

@Composable
fun NavLava(sesionVm: SesionViewModel, factory: LavaViewModelFactory) {
    val nav = rememberNavController()
    val scope = rememberCoroutineScope()

    NavHost(navController = nav, startDestination = Rutas.SPLASH) {

        composable(Rutas.SPLASH) {
            SplashScreen(onTerminar = {
                scope.launch {
                    // Si hay perfil guardado salta el login y va a su pantalla principal
                    val destino = when (sesionVm.restaurarSesion()) {
                        Rol.CLIENTE -> Rutas.INICIO
                        Rol.ADMIN -> Rutas.PANEL
                        null -> Rutas.LOGIN
                    }
                    nav.navigate(destino) { popUpTo(Rutas.SPLASH) { inclusive = true } }
                }
            })
        }

        composable(Rutas.LOGIN) {
            LoginScreen(vm = sesionVm, onLoginExitoso = { rol ->
                val destino = if (rol == Rol.ADMIN) Rutas.PANEL else Rutas.INICIO
                nav.navigate(destino) { popUpTo(Rutas.LOGIN) { inclusive = true } }
            })
        }

        // Marcadores temporales hasta los Pasos 8 y 12
        composable(Rutas.INICIO) {
            Marcador("Inicio del estudiante (Paso 8)") {
                sesionVm.cerrarSesion()
                nav.navigate(Rutas.LOGIN) { popUpTo(0) }
            }
        }
        composable(Rutas.PANEL) {
            Marcador("Panel del administrador (Paso 12)") {
                sesionVm.cerrarSesion()
                nav.navigate(Rutas.LOGIN) { popUpTo(0) }
            }
        }

        composable(Rutas.SERVICIOS) {
            val vm: ServiciosViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = factory)
            Scaffold(topBar = { AppTopBar("Servicios") }) { padding ->
                Box(Modifier.padding(padding)) {
                    ServiciosScreen(vm = vm, onEditar = { /* Paso 13 */ })
                }
            }
        }
        // TODO (Pasos 8 al 13): resto de destinos y grafos por perfil
    }
}

@Composable
private fun Marcador(texto: String, onCerrarSesion: () -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(texto)
        Button(onClick = onCerrarSesion) { Text("Cerrar sesión (temporal)") }
    }
}