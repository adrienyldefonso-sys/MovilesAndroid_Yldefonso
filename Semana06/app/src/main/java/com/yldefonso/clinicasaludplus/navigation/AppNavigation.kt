package com.yldefonso.clinicasaludplus.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yldefonso.clinicasaludplus.ui.components.auth.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rutas.Splash.ruta
    ) {
        composable(Rutas.Splash.ruta) {
            SplashScreen(
                onComenzar = { navController.navigate(Rutas.Registro.ruta) },
                onTengoCuenta = { navController.navigate(Rutas.Login.ruta) }
            )
        }
        composable(Rutas.Registro.ruta) {
            RegistroScreen(
                onRegistroExitoso = {
                    navController.navigate(Rutas.Home.ruta) {
                        popUpTo(Rutas.Splash.ruta) { inclusive = true }
                    }
                },
                onIrALogin = { navController.navigate(Rutas.Login.ruta) },
                onVerTerminos = { navController.navigate(Rutas.Terminos.ruta) }
            )
        }
        composable(Rutas.Login.ruta) {
            LoginScreen(
                onLoginExitoso = {
                    navController.navigate(Rutas.Home.ruta) {
                        popUpTo(Rutas.Splash.ruta) { inclusive = true }
                    }
                },
                onIrARegistro = { navController.navigate(Rutas.Registro.ruta) }
            )
        }
        composable(Rutas.Terminos.ruta) {
            TerminosScreen(
                onVolver = { navController.popBackStack() }
            )
        }
        composable(Rutas.Home.ruta) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("HomeScreen (Se implementará en los siguientes commits)")
            }
        }
    }
}