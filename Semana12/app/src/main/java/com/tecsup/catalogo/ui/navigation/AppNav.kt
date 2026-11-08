package com.tecsup.catalogo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.catalogo.ui.detalle.DetalleScreen
import com.tecsup.catalogo.ui.favoritos.FavoritosScreen
import com.tecsup.catalogo.ui.lista.ListaScreen
import com.tecsup.catalogo.ui.login.LoginScreen
import com.tecsup.catalogo.ui.login.LoginViewModel

/** H13: login, lista, detalle y favoritos. H14 agrega el panel Admin según el rol. */
@Composable
fun AppNav(factory: ViewModelProvider.Factory) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                factory = factory,
                onSesionIniciada = {
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
                onCerrarSesion = {
                    sesionVm.cerrarSesion()
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