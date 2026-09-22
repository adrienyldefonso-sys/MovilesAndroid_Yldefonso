package com.yldefonso.navlab.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yldefonso.navlab.model.StudentRepository
import com.yldefonso.navlab.screens.DetailScreen
import com.yldefonso.navlab.screens.HomeScreen
import com.yldefonso.navlab.screens.ListScreen
import com.yldefonso.navlab.screens.LoginScreen
import com.yldefonso.navlab.screens.ProfileScreen

@Composable
fun AppNavigation() {
    // Crea y recuerda el controlador de navegacion a traves de las recomposiciones
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        // Se establece LoginScreen como el destino inicial de la aplicacion
        startDestination = Screen.Login.route
    ) {
        // Registra la pantalla de Login
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        // Registra HomeScreen como la ruta "home"
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        // Registra ListScreen bajo la ruta "list"
        composable(Screen.List.route) {
            ListScreen(navController)
        }

        // Registra ProfileScreen como la ruta "profile"
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

        // Registra DetailScreen recibiendo itemId como argumento Int
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument(name = "itemId") {
                    type = NavType.IntType // El argumento itemId es de tipo Int
                    defaultValue = 1
                }
            )
        ) { backStackEntry ->
            // Extrae el itemId pasado en la ruta (por defecto 1 si no se encuentra)
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 1
            // Busca el estudiante por ID en el repositorio
            val student = StudentRepository.students.find { it.id == itemId }
                ?: StudentRepository.students.first()

            // Pasa el objeto Student encontrado a la pantalla de detalle (cambia la firma a DetailScreen(navController, student))
            DetailScreen(navController, student)
        }
    }
}
