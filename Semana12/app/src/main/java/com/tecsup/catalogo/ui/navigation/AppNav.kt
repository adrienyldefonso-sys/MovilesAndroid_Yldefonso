package com.tecsup.catalogo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.catalogo.ui.detalle.DetalleScreen
import com.tecsup.catalogo.ui.lista.ListaScreen

/** H7: lista y detalle. H13/H14 agregan login, favoritos y panel Admin según el rol. */
@Composable
fun AppNav(factory: ViewModelProvider.Factory) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            ListaScreen(
                factory = factory,
                onItemClick = { id -> navController.navigate("detalle/$id") }
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