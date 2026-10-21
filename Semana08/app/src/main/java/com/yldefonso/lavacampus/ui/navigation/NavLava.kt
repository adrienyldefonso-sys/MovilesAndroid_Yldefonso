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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yldefonso.lavacampus.model.Rol
import com.yldefonso.lavacampus.ui.administrador.ServiciosScreen
import com.yldefonso.lavacampus.ui.components.AppTopBar
import com.yldefonso.lavacampus.ui.estudiante.DetallePedidoScreen
import com.yldefonso.lavacampus.ui.estudiante.InicioScreen
import com.yldefonso.lavacampus.ui.estudiante.LoginScreen
import com.yldefonso.lavacampus.ui.estudiante.NuevoPedidoScreen
import com.yldefonso.lavacampus.ui.estudiante.SplashScreen
import com.yldefonso.lavacampus.viewmodel.DetallePedidoViewModel
import com.yldefonso.lavacampus.viewmodel.InicioViewModel
import com.yldefonso.lavacampus.viewmodel.LavaViewModelFactory
import com.yldefonso.lavacampus.viewmodel.NuevoPedidoViewModel
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

        // ---- Estudiante ----
        composable(Rutas.INICIO) {
            val usuario by sesionVm.usuarioActual.collectAsState()
            usuario?.let { u ->
                val vm: InicioViewModel = viewModel(
                    key = "inicio-${u.id}",
                    factory = factory.paraUsuario(u.id, ::InicioViewModel)
                )
                InicioScreen(
                    vm = vm,
                    usuario = u,
                    onNuevoPedido = { nav.navigate(Rutas.NUEVO_PEDIDO) },
                    onMisPedidos = { /* Paso 11: Mis pedidos */ },
                    onTarifas = { /* Paso 11: Tarifas */ },
                    onPedido = { id -> nav.navigate(Rutas.detalle(id)) },
                    onCerrarSesion = {
                        sesionVm.cerrarSesion()
                        nav.navigate(Rutas.LOGIN) { popUpTo(0) }
                    }
                )
            }
        }

        composable(Rutas.NUEVO_PEDIDO) {
            val usuario by sesionVm.usuarioActual.collectAsState()
            usuario?.let { u ->
                val vm: NuevoPedidoViewModel = viewModel(
                    key = "nuevo-${u.id}",
                    factory = factory.paraUsuario(u.id, ::NuevoPedidoViewModel)
                )
                NuevoPedidoScreen(
                    vm = vm,
                    onAtras = { nav.popBackStack() },
                    // Paso 11: aquí irá a Mis pedidos; por ahora vuelve a Inicio
                    onPedidoCreado = { nav.popBackStack() }
                )
            }
        }

        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument("pedidoId") { type = NavType.IntType })
        ) { entrada ->
            val pedidoId = entrada.arguments?.getInt("pedidoId") ?: return@composable
            val vm: DetallePedidoViewModel = viewModel(
                key = "detalle-$pedidoId",
                factory = factory.paraUsuario(pedidoId, ::DetallePedidoViewModel)
            )
            DetallePedidoScreen(vm = vm, onAtras = { nav.popBackStack() })
        }

        // ---- Administrador (marcador temporal hasta el Paso 12) ----
        composable(Rutas.PANEL) {
            Marcador("Panel del administrador (Paso 12)") {
                sesionVm.cerrarSesion()
                nav.navigate(Rutas.LOGIN) { popUpTo(0) }
            }
        }

        composable(Rutas.SERVICIOS) {
            val vm: ServiciosViewModel = viewModel(factory = factory)
            Scaffold(topBar = { AppTopBar("Servicios") }) { padding ->
                Box(Modifier.padding(padding)) {
                    ServiciosScreen(vm = vm, onEditar = { /* Paso 14 */ })
                }
            }
        }
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