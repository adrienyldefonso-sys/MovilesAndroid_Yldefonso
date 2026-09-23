package com.yldefonso.tecsupfit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yldefonso.tecsupfit.data.ClasesRepository
import com.yldefonso.tecsupfit.model.Reserva
import com.yldefonso.tecsupfit.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Estado global crea aquí y se pasa por parámetro a cada pantalla que lo necesite
    val reservas = remember { mutableStateListOf<Reserva>() }

    //lA navegación secundaria es el bottomBar, que vive dentro del Scaffold
    // de cada pantalla, porque cada pantalla decide  si lo muestra o no
    NavHost(navController = navController, startDestination = Screen.Home.route) {

        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        composable(
            route = Screen.ClassDetail.route,
            arguments = listOf(navArgument("classId") { type = NavType.IntType })
        ) { backStackEntry ->
            val classId = backStackEntry.arguments?.getInt("classId") ?: 0
            val clase = ClasesRepository.clases.find { it.id == classId }
                ?: ClasesRepository.clases.first()
            ClassDetailScreen(navController, clase, reservas)
        }

        composable(
            route = Screen.Confirmation.route,
            arguments = listOf(navArgument("reservaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val reservaId = backStackEntry.arguments?.getInt("reservaId") ?: 0
            val reserva = reservas.find { it.id == reservaId }
            ConfirmationScreen(navController, reserva)
        }

        composable(Screen.Reservas.route) {
            ReservasScreen(navController, reservas)
        }

        composable(Screen.Rutinas.route) {
            RutinasScreen(navController)
        }

        composable(Screen.Perfil.route) {
            PerfilScreen(navController, reservas)
        }
    }
}