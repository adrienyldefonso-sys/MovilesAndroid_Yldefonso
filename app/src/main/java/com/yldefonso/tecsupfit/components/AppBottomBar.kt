package com.yldefonso.tecsupfit.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.tecsupfit.ui.theme.TealPrimary
import com.yldefonso.tecsupfit.navigation.Screen

private data class BottomItem(val label: String, val route: String)
private val bottomItems = listOf(
    BottomItem("Inicio", Screen.Home.route),
    BottomItem("Reservas", Screen.Reservas.route),
    BottomItem("Rutinas", Screen.Rutinas.route),
    BottomItem("Perfil", Screen.Perfil.route)
)
// Componente reutilizable de bottomBar.No recibe la ruta actual como
// parámetro: la obtiene ella misma observando el navController concurrentBackStackEntryAsState().
@Composable
fun AppBottomBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        bottomItems.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            popUpTo(Screen.Home.route)
                            launchSingleTop = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) Icons.Filled.RadioButtonChecked
                        else Icons.Outlined.RadioButtonUnchecked,
                        contentDescription = item.label,
                        tint = if (selected) TealPrimary else Color.Gray
                    )
                },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = TealPrimary,
                    selectedTextColor = TealPrimary,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}