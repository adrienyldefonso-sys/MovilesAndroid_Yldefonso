package com.yldefonso.clinicasaludplus.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yldefonso.clinicasaludplus.components.AppDrawer
import com.yldefonso.clinicasaludplus.data.MedicosRepository
import com.yldefonso.clinicasaludplus.model.Cita
import com.yldefonso.clinicasaludplus.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Estado global de la app SIN ViewModel. Precargado con dato de prueba
    // para la sustentación/demostración de la funcionalidad de calificación de citas completadas.
    val citas = remember {
        mutableStateListOf(
            Cita(
                id = 1,
                medicoId = 2,
                medicoNombre = "Dr. Luis Vega",
                especialidad = "Pediatría",
                fecha = "Mié 15",
                hora = "3:00 pm",
                estado = "Completada"
            )
        )
    }

    // Estado y control del drawer (menú lateral)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Ruta actual, usada por AppDrawer para resaltar el ítem seleccionado
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    // El drawer ENVUELVE todo el NavHost: así está disponible en
    // cualquier destino sin tener que recrearlo en cada pantalla.
    ModalNavigationDrawer(
        modifier = Modifier.fillMaxSize(),
        drawerState = drawerState,
        drawerContent = {
            // AppDrawer ahora recibe navController + drawerState + scope
            // directamente (mismo patrón que usamos en el resto de pantallas),
            // por eso ya NO se pasan onDestinationClick ni onCloseDrawer.
            AppDrawer(
                navController = navController,
                currentRoute = currentRoute,
                drawerState = drawerState,
                scope = scope
            )
        }
    ) {
        NavHost(navController = navController, startDestination = Screen.Login.route) {

            composable(Screen.Login.route) {
                LoginScreen(navController)
            }

            composable(Screen.Home.route) {
                HomeScreen(navController, drawerState, scope)
            }

            composable(
                route = Screen.DoctorProfile.route,
                arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
            ) { backStackEntry ->
                val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
                val medico = MedicosRepository.medicos.find { it.id == doctorId }
                    ?: MedicosRepository.medicos.first()
                DoctorProfileScreen(navController, medico)
            }

            composable(
                route = Screen.BookAppointment.route,
                arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
            ) { backStackEntry ->
                val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
                val medico = MedicosRepository.medicos.find { it.id == doctorId }
                    ?: MedicosRepository.medicos.first()
                BookAppointmentScreen(navController, medico, citas)
            }

            composable(
                route = Screen.Confirmation.route,
                arguments = listOf(navArgument("citaId") { type = NavType.IntType })
            ) { backStackEntry ->
                val citaId = backStackEntry.arguments?.getInt("citaId") ?: 0
                val cita = citas.find { it.id == citaId }
                ConfirmationScreen(navController, cita)
            }

            composable(Screen.MisCitas.route) {
                MisCitasScreen(navController, drawerState, scope, citas)
            }

            composable(Screen.HistorialMedico.route) {
                HistorialMedicoScreen(navController, drawerState, scope, citas)
            }

            // Destino agregado en la última corrección (4to ítem del drawer)
            composable(Screen.Perfil.route) {
                PerfilScreen(navController, drawerState, scope, citas)
            }
        }
    }
}