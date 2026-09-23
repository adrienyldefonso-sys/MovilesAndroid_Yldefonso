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

    // Estado global de reservas precargado con datos de prueba para demostración y sustentación
    val reservas = remember {
        mutableStateListOf(
            Reserva(
                id = 1,
                claseId = 2,
                claseNombre = "Cross Training",
                horario = "6:00 pm",
                sala = "Sala 1",
                estado = "Confirmada"
            ),
            Reserva(
                id = 2,
                claseId = 1,
                claseNombre = "Yoga funcional",
                horario = "7:00 am",
                sala = "Sala 2",
                estado = "Completada"
            )
        )
    }

    // La navegación secundaria es el bottomBar, que vive dentro del Scaffold
    // de cada pantalla, porque cada pantalla decide si lo muestra o no
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
