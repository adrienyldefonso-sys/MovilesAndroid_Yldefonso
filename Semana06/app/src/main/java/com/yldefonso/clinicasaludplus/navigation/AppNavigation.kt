package com.yldefonso.clinicasaludplus.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.yldefonso.clinicasaludplus.ui.RegistroScreen
import com.yldefonso.clinicasaludplus.ui.SplashScreen
import com.yldefonso.clinicasaludplus.ui.TerminosScreen
import com.yldefonso.clinicasaludplus.ui.components.agendamiento.*
import com.yldefonso.clinicasaludplus.ui.components.auth.LoginScreen
import com.yldefonso.clinicasaludplus.ui.components.citas.*
import com.yldefonso.clinicasaludplus.ui.components.home.HomeScreen
import com.yldefonso.clinicasaludplus.ui.components.notificaciones.NotificacionesScreen
import com.yldefonso.clinicasaludplus.ui.components.perfil.PerfilScreen
import com.yldefonso.clinicasaludplus.ui.components.resultados.ResultadosScreen

// Paleta de colores para la barra inferior
private val AzulPrimario = Color(0xFF2F6BEA)
private val AzulPastelIndicador = Color(0xFFEAF1FF)
private val TextoInactivo = Color(0xFF8C98A9)
private val BordeSuperior = Color(0xFFEEF2F6)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarItems = listOf(
        Rutas.Home,
        Rutas.MisCitas,
        Rutas.Resultados,
        Rutas.Perfil
    )

    val mostrarBottomBar = currentRoute in bottomBarItems.map { it.ruta }

    Scaffold(
        bottomBar = {
            if (mostrarBottomBar) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 0.dp,
                    modifier = Modifier
                        .height(72.dp)
                        .drawWithContent {
                            drawContent()
                            // Línea suave superior para separar la barra del contenido
                            drawLine(
                                color = BordeSuperior,
                                start = Offset(0f, 0f),
                                end = Offset(size.width, 0f),
                                strokeWidth = 1.dp.toPx()
                            )
                        }
                ) {
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
                            icon = {
                                item.icono?.let {
                                    Icon(
                                        imageVector = it,
                                        contentDescription = item.titulo,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = item.titulo,
                                    fontSize = 12.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = AzulPrimario,
                                selectedTextColor = AzulPrimario,
                                indicatorColor = AzulPastelIndicador,
                                unselectedIconColor = TextoInactivo,
                                unselectedTextColor = TextoInactivo
                            )
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

            composable(Rutas.Home.ruta) { HomeScreen(navController) }
            composable(Rutas.Especialidades.ruta) { EspecialidadesScreen(navController) }
            composable(Rutas.MisCitas.ruta) { MisCitasScreen(navController) }
            composable(Rutas.Perfil.ruta) { PerfilScreen(navController) }
            composable(Rutas.Resultados.ruta) { ResultadosScreen(navController) }
            composable(Rutas.Notificaciones.ruta) { NotificacionesScreen(navController) }

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
            composable(Rutas.DetalleCita.ruta) { DetalleCitaScreen(navController) }
        }
    }
}