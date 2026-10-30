package com.yldefonso.lavacampus.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yldefonso.lavacampus.model.Rol
import com.yldefonso.lavacampus.ui.administrador.AjustesScreen
import com.yldefonso.lavacampus.ui.administrador.BandejaScreen
import com.yldefonso.lavacampus.ui.administrador.ClientesScreen
import com.yldefonso.lavacampus.ui.administrador.FormularioServicioScreen
import com.yldefonso.lavacampus.ui.administrador.GestionPedidoScreen
import com.yldefonso.lavacampus.ui.administrador.PanelScreen
import com.yldefonso.lavacampus.ui.administrador.ServiciosScreen
import com.yldefonso.lavacampus.ui.components.BarraNavAdmin
import com.yldefonso.lavacampus.ui.components.BarraNavEstudiante
import com.yldefonso.lavacampus.ui.estudiante.DetallePedidoScreen
import com.yldefonso.lavacampus.ui.estudiante.InicioScreen
import com.yldefonso.lavacampus.ui.estudiante.LoginScreen
import com.yldefonso.lavacampus.ui.estudiante.MisPedidosScreen
import com.yldefonso.lavacampus.ui.estudiante.NuevoPedidoScreen
import com.yldefonso.lavacampus.ui.estudiante.PerfilScreen
import com.yldefonso.lavacampus.ui.estudiante.SplashScreen
import com.yldefonso.lavacampus.ui.estudiante.TarifasScreen
import com.yldefonso.lavacampus.viewmodel.BandejaViewModel
import com.yldefonso.lavacampus.viewmodel.ClientesViewModel
import com.yldefonso.lavacampus.viewmodel.DetallePedidoViewModel
import com.yldefonso.lavacampus.viewmodel.GestionPedidoViewModel
import com.yldefonso.lavacampus.viewmodel.InicioViewModel
import com.yldefonso.lavacampus.viewmodel.LavaViewModelFactory
import com.yldefonso.lavacampus.viewmodel.MisPedidosViewModel
import com.yldefonso.lavacampus.viewmodel.NotificacionesViewModel
import com.yldefonso.lavacampus.viewmodel.NuevoPedidoViewModel
import com.yldefonso.lavacampus.viewmodel.PanelViewModel
import com.yldefonso.lavacampus.viewmodel.PerfilViewModel
import com.yldefonso.lavacampus.viewmodel.ServiciosViewModel
import com.yldefonso.lavacampus.viewmodel.SesionViewModel
import kotlinx.coroutines.launch

@Composable
fun NavLava(sesionVm: SesionViewModel, factory: LavaViewModelFactory) {
    val nav = rememberNavController()
    val scope = rememberCoroutineScope()

    /** Pestañas: a la raíz se vuelve con popBackStack; a otra pestaña se reemplaza la actual. */
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

    /** Guarda el otro perfil y navega a su grafo, limpiando la pila. */
    fun cambiarPerfil() {
        sesionVm.cambiarPerfil { rol ->
            val destino = if (rol == Rol.ADMIN) Rutas.PANEL else Rutas.INICIO
            nav.navigate(destino) { popUpTo(0) }
        }
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
                val notiVm: NotificacionesViewModel = viewModel(
                    key = "noti-${u.id}",
                    factory = factory.paraNotificaciones(u.id, esAdmin = false)
                )
                InicioScreen(
                    vm = vm,
                    usuario = u,
                    notificaciones = notiVm,
                    onNuevoPedido = { nav.navigate(Rutas.NUEVO_PEDIDO) },
                    onMisPedidos = { irATab(Rutas.MIS_PEDIDOS, Rutas.INICIO) },
                    onTarifas = { irATab(Rutas.TARIFAS, Rutas.INICIO) },
                    onPedido = { id -> nav.navigate(Rutas.detalle(id)) },
                    barraInferior = barraEstudiante
                )
            }
        }

        composable(
            route = Rutas.NUEVO_PEDIDO_RUTA,
            arguments = listOf(navArgument("repetir") { type = NavType.IntType; defaultValue = -1 })
        ) { entrada ->
            val repetirArg = entrada.arguments?.getInt("repetir") ?: -1
            val pedidoARepetir = if (repetirArg != -1) repetirArg else null
            val usuario by sesionVm.usuarioActual.collectAsState()
            usuario?.let { u ->
                val vm: NuevoPedidoViewModel = viewModel(
                    key = "nuevo-${u.id}-$repetirArg",
                    factory = factory.paraNuevoPedido(u.id, pedidoARepetir)
                )
                NuevoPedidoScreen(
                    vm = vm,
                    onAtras = { nav.popBackStack() },
                    onPedidoCreado = {
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
                    onRepetir = { id -> nav.navigate(Rutas.nuevoPedido(id)) },
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
            val usuario by sesionVm.usuarioActual.collectAsState()
            usuario?.let { u ->
                val vm: PerfilViewModel = viewModel(
                    key = "perfil-${u.id}",
                    factory = factory.paraUsuario(u.id, ::PerfilViewModel)
                )
                PerfilScreen(
                    vm = vm,
                    sesion = sesionVm,
                    usuario = u,
                    onCambiarPerfil = { cambiarPerfil() },
                    onCerrarSesion = { cerrarSesion() },
                    barraInferior = barraEstudiante
                )
            }
        }

        // ================= ADMINISTRADOR =================
        composable(Rutas.PANEL) {
            val usuario by sesionVm.usuarioActual.collectAsState()
            usuario?.let { u ->
                val vm: PanelViewModel = viewModel(factory = factory)
                val notiVm: NotificacionesViewModel = viewModel(
                    key = "noti-admin-${u.id}",
                    factory = factory.paraNotificaciones(u.id, esAdmin = true)
                )
                PanelScreen(
                    vm = vm,
                    notificaciones = notiVm,
                    onAbrirPedido = { id -> nav.navigate(Rutas.gestion(id)) },
                    onVerListos = { irATab(Rutas.BANDEJA, Rutas.PANEL) },
                    barraInferior = barraAdmin
                )
            }
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
            ServiciosScreen(
                vm = vm,
                onNuevo = { nav.navigate(Rutas.formulario(0)) },
                onEditar = { id -> nav.navigate(Rutas.formulario(id)) },
                barraInferior = barraAdmin
            )
        }

        composable(
            route = Rutas.FORMULARIO,
            arguments = listOf(navArgument("servicioId") { type = NavType.IntType })
        ) { entrada ->
            val servicioId = entrada.arguments?.getInt("servicioId") ?: 0
            val vm: ServiciosViewModel = viewModel(factory = factory)
            FormularioServicioScreen(vm = vm, servicioId = servicioId, onAtras = { nav.popBackStack() })
        }

        composable(Rutas.CLIENTES) {
            val vm: ClientesViewModel = viewModel(factory = factory)
            ClientesScreen(vm = vm, barraInferior = barraAdmin)
        }

        composable(Rutas.AJUSTES) {
            val usuario by sesionVm.usuarioActual.collectAsState()
            usuario?.let { u ->
                AjustesScreen(
                    sesion = sesionVm,
                    usuario = u,
                    onCambiarPerfil = { cambiarPerfil() },
                    onCerrarSesion = { cerrarSesion() },
                    barraInferior = barraAdmin
                )
            }
        }
    }
}