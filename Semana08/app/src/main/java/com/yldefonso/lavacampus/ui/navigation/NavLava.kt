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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yldefonso.lavacampus.model.Rol
import com.yldefonso.lavacampus.ui.administrador.BandejaScreen
import com.yldefonso.lavacampus.ui.administrador.GestionPedidoScreen
import com.yldefonso.lavacampus.ui.administrador.PanelScreen
import com.yldefonso.lavacampus.ui.administrador.ServiciosScreen
import com.yldefonso.lavacampus.ui.components.AppTopBar
import com.yldefonso.lavacampus.ui.components.BarraNavAdmin
import com.yldefonso.lavacampus.ui.components.BarraNavEstudiante
import com.yldefonso.lavacampus.ui.estudiante.DetallePedidoScreen
import com.yldefonso.lavacampus.ui.estudiante.InicioScreen
import com.yldefonso.lavacampus.ui.estudiante.LoginScreen
import com.yldefonso.lavacampus.ui.estudiante.MisPedidosScreen
import com.yldefonso.lavacampus.ui.estudiante.NuevoPedidoScreen
import com.yldefonso.lavacampus.ui.estudiante.SplashScreen
import com.yldefonso.lavacampus.ui.estudiante.TarifasScreen
import com.yldefonso.lavacampus.viewmodel.BandejaViewModel
import com.yldefonso.lavacampus.viewmodel.DetallePedidoViewModel
import com.yldefonso.lavacampus.viewmodel.GestionPedidoViewModel
import com.yldefonso.lavacampus.viewmodel.InicioViewModel
import com.yldefonso.lavacampus.viewmodel.LavaViewModelFactory
import com.yldefonso.lavacampus.viewmodel.MisPedidosViewModel
import com.yldefonso.lavacampus.viewmodel.NuevoPedidoViewModel
import com.yldefonso.lavacampus.viewmodel.PanelViewModel
import com.yldefonso.lavacampus.viewmodel.ServiciosViewModel
import com.yldefonso.lavacampus.viewmodel.SesionViewModel
import kotlinx.coroutines.launch

@Composable
fun NavLava(sesionVm: SesionViewModel, factory: LavaViewModelFactory) {
    val nav = rememberNavController()
    val scope = rememberCoroutineScope()

    /**
     * Navegación entre pestañas, igual para botones y barra inferior:
     * - a la raíz (Inicio o Panel): vuelve atrás hasta ella (siempre funciona);
     * - a otra pestaña: reemplaza la pantalla actual encima de la raíz.
     */
    fun irATab(destino: String, raiz: String) {
        if (destino == raiz) {
            if (!nav.popBackStack(raiz, inclusive = false)) {
                nav.navigate(raiz) { popUpTo(0) }
            }
        } else {
            nav.navigate(destino) {
                popUpTo(raiz)
                launchSingleTop = true
            }
        }
    }

    fun cerrarSesion() {
        sesionVm.cerrarSesion()
        nav.navigate(Rutas.LOGIN) { popUpTo(0) }
    }

    val barraEstudiante: @Composable () -> Unit = {
        val ruta = nav.currentBackStackEntryAsState().value?.destination?.route
        BarraNavEstudiante(rutaActual = ruta, onIr = { irATab(it, Rutas.INICIO) })
    }
    val barraAdmin: @Composable () -> Unit = {
        val ruta = nav.currentBackStackEntryAsState().value?.destination?.route
        BarraNavAdmin(rutaActual = ruta, onIr = { irATab(it, Rutas.PANEL) })
    }

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

        // ================= ESTUDIANTE =================
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
                    onMisPedidos = { irATab(Rutas.MIS_PEDIDOS, Rutas.INICIO) },
                    onTarifas = { irATab(Rutas.TARIFAS, Rutas.INICIO) },
                    onPedido = { id -> nav.navigate(Rutas.detalle(id)) },
                    onCerrarSesion = { cerrarSesion() },
                    barraInferior = barraEstudiante
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
                    onPedidoCreado = {
                        // Deja la pila como Inicio -> Mis pedidos (sin duplicados)
                        nav.navigate(Rutas.MIS_PEDIDOS) {
                            popUpTo(Rutas.INICIO)
                            launchSingleTop = true
                        }
                    }
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

        composable(Rutas.MIS_PEDIDOS) {
            val usuario by sesionVm.usuarioActual.collectAsState()
            usuario?.let { u ->
                val vm: MisPedidosViewModel = viewModel(
                    key = "mis-${u.id}",
                    factory = factory.paraUsuario(u.id, ::MisPedidosViewModel)
                )
                MisPedidosScreen(
                    vm = vm,
                    onAtras = { irATab(Rutas.INICIO, Rutas.INICIO) },
                    onPedido = { id -> nav.navigate(Rutas.detalle(id)) },
                    onNuevoPedido = { nav.navigate(Rutas.NUEVO_PEDIDO) },
                    barraInferior = barraEstudiante
                )
            }
        }

        composable(Rutas.TARIFAS) {
            val vm: ServiciosViewModel = viewModel(factory = factory)
            TarifasScreen(
                vm = vm,
                onAtras = { irATab(Rutas.INICIO, Rutas.INICIO) },
                barraInferior = barraEstudiante
            )
        }

        composable(Rutas.PERFIL) {
            PantallaTemporal("Perfil del estudiante (Paso 13)", barraEstudiante) { cerrarSesion() }
        }

        // ================= ADMINISTRADOR =================
        composable(Rutas.PANEL) {
            val vm: PanelViewModel = viewModel(factory = factory)
            PanelScreen(
                vm = vm,
                onVerListos = { irATab(Rutas.BANDEJA, Rutas.PANEL) },
                barraInferior = barraAdmin
            )
        }

        composable(Rutas.BANDEJA) {
            val vm: BandejaViewModel = viewModel(factory = factory)
            BandejaScreen(
                vm = vm,
                onPedido = { id -> nav.navigate(Rutas.gestion(id)) },
                barraInferior = barraAdmin
            )
        }

        composable(
            route = Rutas.GESTION,
            arguments = listOf(navArgument("pedidoId") { type = NavType.IntType })
        ) { entrada ->
            val pedidoId = entrada.arguments?.getInt("pedidoId") ?: return@composable
            val vm: GestionPedidoViewModel = viewModel(
                key = "gestion-$pedidoId",
                factory = factory.paraUsuario(pedidoId, ::GestionPedidoViewModel)
            )
            GestionPedidoScreen(vm = vm, onAtras = { nav.popBackStack() })
        }

        composable(Rutas.SERVICIOS) {
            val vm: ServiciosViewModel = viewModel(factory = factory)
            Scaffold(
                topBar = { AppTopBar("Servicios") },
                bottomBar = barraAdmin
            ) { padding ->
                Box(Modifier.padding(padding)) {
                    ServiciosScreen(vm = vm, onEditar = { /* Paso 13 */ })
                }
            }
        }

        composable(Rutas.CLIENTES) {
            PantallaTemporal("Clientes (Paso 13)", barraAdmin)
        }

        composable(Rutas.AJUSTES) {
            PantallaTemporal("Ajustes del administrador (Paso 13)", barraAdmin) { cerrarSesion() }
        }
    }
}

/** Pantalla provisional con barra inferior; el botón de cerrar sesión es solo para pruebas. */
@Composable
private fun PantallaTemporal(
    texto: String,
    barra: @Composable () -> Unit,
    onCerrarSesion: (() -> Unit)? = null
) {
    Scaffold(bottomBar = barra) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Text(texto)
            onCerrarSesion?.let { Button(onClick = it) { Text("Cerrar sesión (temporal)") } }
        }
    }
}