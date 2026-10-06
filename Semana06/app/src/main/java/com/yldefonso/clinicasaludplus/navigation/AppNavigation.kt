package com.yldefonso.clinicasaludplus.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yldefonso.clinicasaludplus.ui.*
import com.yldefonso.clinicasaludplus.ui.components.agendamiento.*
import com.yldefonso.clinicasaludplus.ui.components.home.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rutas.Splash.ruta
    ) {
        composable(Rutas.Splash.ruta) { SplashScreen(navController) }
        composable(Rutas.Login.ruta) { LoginScreen(navController) }
        composable(Rutas.Registro.ruta) { RegistroScreen(navController) }
        composable(Rutas.Terminos.ruta) { TerminosScreen(navController) }

        // Hito B
        composable(Rutas.Home.ruta) { HomeScreen(navController) }

        // Hito A & D
        composable(Rutas.Especialidades.ruta) { EspecialidadesScreen(navController) }
        composable(
            route = Rutas.Medicos.ruta,
            arguments = listOf(navArgument("especialidadId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("especialidadId") ?: 1
            MedicosScreen(navController = navController, especialidadId = id)
        }

        composable(Rutas.FechaHora.ruta) { FechaHoraScreen(navController) }
        composable(Rutas.ConfirmarCita.ruta) { ConfirmarCitaScreen(navController) }
        composable(Rutas.CitaExitosa.ruta) { CitaExitosaScreen(navController) }
    }
}