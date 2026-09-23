package com.yldefonso.clinicasaludplus.navigation
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
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
import com.yldefonso.clinicasaludplus.screens.BookAppointmentScreen
import com.yldefonso.clinicasaludplus.screens.ConfirmationScreen
import com.yldefonso.clinicasaludplus.screens.DoctorProfileScreen
import com.yldefonso.clinicasaludplus.screens.HistorialMedicoScreen
import com.yldefonso.clinicasaludplus.screens.HomeScreen
import com.yldefonso.clinicasaludplus.screens.MisCitasScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    //el val citas sirve como el parametro que se pasara a cada pantalla que lo necesite
    val citas = remember { mutableStateListOf<Cita>() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope ()
    //Ruta actual usada por AppDrawer
    val currentRoute=navController.currentBackStackEntryAsState().value?.destination?.route
    //Aqui el drawer envualve a todo el navHost para que este disponible en cualquier destino
    ModalNavigationDrawer(
        modifier = Modifier.fillMaxSize(),
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                currentRoute = currentRoute,
                onDestinationClick = { route->
                    navController.navigate(route){
                        popUpTo(Screen.Home.route)
                        launchSingleTop = true
                    }
                },
                onCloseDrawer = {scope.launch { drawerState.close() }}
            )
        }
    ) {
        NavHost(navController = navController, startDestination = Screen.Home.route) {
            composable (Screen.Home.route){
                HomeScreen(navController,drawerState,scope)
            }
            composable  (
                route = Screen.DoctorProfile.route,
                arguments = listOf(navArgument("doctorId"){type = NavType.IntType})
            ){backStackEntry->
                val doctorId= backStackEntry.arguments?.getInt("doctorId")?:0
                val medico= MedicosRepository.medicos.find { it.id ==doctorId}
                    ?: MedicosRepository.medicos.first()
                DoctorProfileScreen(navController,medico)
            }
            composable (
                route = Screen.BookAppointment.route,
                arguments = listOf(navArgument("doctorId") {type= NavType.IntType})
            ){backStackEntry->
                val doctorId = backStackEntry.arguments?.getInt("doctorId")?:0
                val medico = MedicosRepository.medicos.find { it.id == doctorId }
                    ?: MedicosRepository.medicos.first()
                BookAppointmentScreen(navController,medico,citas)
                }
            composable(
                route = Screen.Confirmation.route,
                arguments = listOf(navArgument("citaId") { type = NavType.IntType })
            ) { backStackEntry ->
                val citaId = backStackEntry.arguments?.getInt("citaId") ?: 0
                val cita = citas.find { it.id == citaId }
                ConfirmationScreen(navController, cita)
            }
            composable(Screen.MisCitas.route){
                MisCitasScreen(navController,drawerState,scope,citas)
            }
            composable (Screen.HistorialMedico.route){
                HistorialMedicoScreen(navController,drawerState,scope,citas)
            }
        }
    }

}

