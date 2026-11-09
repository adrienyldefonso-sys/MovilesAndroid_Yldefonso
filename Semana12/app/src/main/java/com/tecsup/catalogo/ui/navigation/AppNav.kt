package com.tecsup.catalogo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.catalogo.domain.model.Rol
import com.tecsup.catalogo.ui.admin.AdminScreen
import com.tecsup.catalogo.ui.detalle.DetalleScreen
import com.tecsup.catalogo.ui.favoritos.FavoritosScreen
import com.tecsup.catalogo.ui.lista.ListaScreen
import com.tecsup.catalogo.ui.login.LoginScreen
import com.tecsup.catalogo.ui.login.LoginViewModel

/** H14: login, lista, detalle, favoritos y panel Admin (solo para el rol ADMIN). */
@Composable
fun AppNav(factory: ViewModelProvider.Factory) {
    val navController = rememberNavController()

    // Rol de la sesión actual. NO se decide aquí: lo lee FirebaseAuthRepository de Firestore
    // y llega con la Sesion. Aquí solo se guarda para decidir qué pantallas se muestran.
    var rol by rememberSaveable { mutableStateOf<String?>(null) }
    val esAdmin = rol == Rol.ADMIN.name

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                factory = factory,
                onSesionIniciada = { sesion ->
                    rol = sesion.rol.name
                    // popUpTo: saca el login de la pila para que "Atrás" no vuelva a él.
                    navController.navigate("lista") { popUpTo("login") { inclusive = true } }
                }
            )
        }
        composable("lista") {
            // LoginViewModel es el ViewModel de sesión: aquí solo se usa para cerrar la sesión.
            val sesionVm: LoginViewModel = viewModel(factory = factory)
            ListaScreen(
                factory = factory,
                onItemClick = { id -> navController.navigate("detalle/$id") },
                onVerFavoritos = { navController.navigate("favoritos") },
                esAdmin = esAdmin,
                onAdmin = { navController.navigate("admin") },
                onCerrarSesion = {
                    sesionVm.cerrarSesion()
                    rol = null
                    navController.navigate("login") { popUpTo("lista") { inclusive = true } }
                }
            )
        }
        composable("favoritos") {
            FavoritosScreen(
                factory = factory,
                onItemClick = { id -> navController.navigate("detalle/$id") },
                onVolver = { navController.popBackStack() }
            )
        }
        composable("admin") {
            if (esAdmin) {
                AdminScreen(factory = factory, onVolver = { navController.popBackStack() })
            } else {
                // Segunda barrera: un usuario sin rol ADMIN no puede quedarse en esta ruta.
                LaunchedEffect(Unit) { navController.popBackStack() }
            }
        }
        composable(
            route = "detalle/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            DetalleScreen(
                factory = factory,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}