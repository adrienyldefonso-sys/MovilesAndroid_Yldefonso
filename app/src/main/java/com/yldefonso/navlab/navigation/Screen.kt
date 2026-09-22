// Este archivo se encarga de todas las rutas a traves de sealed class. Recibe route como
// parametro el cual es el IDENTIFICADOR UNICO de cada pantalla. Al ser sealed, el compilador conoce
// todas las rutas posibles en compilacion.
package com.yldefonso.navlab.navigation

sealed class Screen(val route: String) {

    // PANTALLA DE LOGIN
    object Login : Screen("login")

    // PANTALLA DE INICIO
    object Home : Screen("home")

    // LISTA DE ELEMENTOS
    object List : Screen("list")

    // PERFIL USUARIO
    object Profile : Screen("profile")

    // Ruta con argumento: itemId es el placeholder que Navigation reemplaza con el valor real
    object Detail : Screen("detail/{itemId}") {
        // Construye la ruta final pasando el id del estudiante como parametro
        fun createRoute(itemId: Int): String = "detail/$itemId"
    }
}
