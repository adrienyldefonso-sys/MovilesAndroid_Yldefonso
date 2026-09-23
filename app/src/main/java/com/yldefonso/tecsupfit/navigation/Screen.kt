package com.yldefonso.tecsupfit.navigation

//Sealed class con todas las rutas. Los destinos con parámetro exponen createRoute() para construir
// la ruta real con el id correspondiente
sealed class Screen(val route: String) {
    object Home : Screen("home")
    // Recibe el id de la clase elegida en Inicio
    object ClassDetail : Screen("class_detail/{classId}") {
        fun createRoute(classId: Int) = "class_detail/$classId"
    }
    // Recibe el id de la reserva ya creada
    object Confirmation : Screen("confirmation/{reservaId}") {
        fun createRoute(reservaId: Int) = "confirmation/$reservaId"
    }
    object Reservas : Screen("reservas")
    object Rutinas : Screen("rutinas")
    object Perfil : Screen("perfil")
}