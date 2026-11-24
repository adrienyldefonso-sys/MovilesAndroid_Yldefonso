//Este archivo se encarga de todas las rutas a traves de sealed class.Recibe route como
//parametro el cual es el IDENTIFICADOR UNICO de cada pantalla.Al ser sealed,el compilador conoce
//todas las rutas posibles en compilacion.
package com.yldefonso.navlab.navigation
import com.yldefonso.navlab.navigation.Screen
sealed class Screen (val route:String) {

    //PANTALLA DE INICIO
    object Home : Screen("home")

    //LISTA DE ELEMENTOS
    object List : Screen("list")

    //PERFIL USUARIO
    object Profile : Screen("profile")

    //ruta con argumento: itemid es el placeholder(elemento temporal)  el cual Navigation reemplaza
    //con el valor real cuando se navega

    object Detail : Screen("detail/{itemId}") {
        fun createRoute(itemId: Int): String = "detail/$itemId"
    }
}
      //Construye la ruta final y el placeholder reemplaza la ruta con el valor real(id del item)
      //Ejemplo:detail/5-> viene siendo ese String el que pasa a navController