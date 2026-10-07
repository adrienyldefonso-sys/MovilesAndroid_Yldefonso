package com.yldefonso.clinicasaludplus.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.yldefonso.clinicasaludplus.ui.*
import com.yldefonso.clinicasaludplus.ui.components.agendamiento.*
import com.yldefonso.clinicasaludplus.ui.components.home.HomeScreen
import com.yldefonso.clinicasaludplus.ui.components.user.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Rutas donde se debe mostrar la BottomBar
    val bottomBarItems = listOf(
        Rutas.Home,
        Rutas.Especialidades,
        Rutas.MisCitas,
        Rutas.Perfil
    )

    val mostrarBottomBar = currentRoute in bottomBarItems.map { it.ruta }

    Scaffold(
        bottomBar = {
            if (mostrarBottomBar) {
                NavigationBar {
                    bottomBarItems.forEach { item ->
                        val selected = currentRoute == item.ruta
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (currentRoute != item.ruta) {
                                    navController.navigate(item.ruta) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = { item.icono?.let { Icon(it, contentDescription = item.titulo) } },
                            label = { Text(item.titulo) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rutas.Splash.ruta,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Rutas.Splash.ruta) { SplashScreen(navController) }
            composable(Rutas.Login.ruta) { LoginScreen(navController) }
            composable(Rutas.Registro.ruta) { RegistroScreen(navController) }
            composable(Rutas.Terminos.ruta) { TerminosScreen(navController) }

            // Pestañas principales de BottomBar
            composable(Rutas.Home.ruta) { HomeScreen(navController) }
            composable(Rutas.Especialidades.ruta) { EspecialidadesScreen(navController) }
            composable(Rutas.MisCitas.ruta) { MisCitasScreen(navController) }
            composable(Rutas.Perfil.ruta) { PerfilScreen(navController) }

            // Navegación parametrizada de médicos
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
}