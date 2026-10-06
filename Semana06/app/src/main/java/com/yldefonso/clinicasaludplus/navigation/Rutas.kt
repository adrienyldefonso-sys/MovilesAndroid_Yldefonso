package com.yldefonso.clinicasaludplus.navigation

sealed class Rutas(val ruta: String) {
    object Splash : Rutas("splash_screen")
    object Login : Rutas("login_screen")
    object Registro : Rutas("registro_screen")
    object Terminos : Rutas("terminos_screen")
    object Home : Rutas("home_screen")

    object Especialidades : Rutas("especialidades_screen")
    object Medicos : Rutas("medicos_screen/{especialidadId}") {
        fun crearRuta(especialidadId: Int) = "medicos_screen/$especialidadId"
    }
    object FechaHora : Rutas("fecha_hora_screen")
    object ConfirmarCita : Rutas("confirmar_cita_screen")
    object CitaExitosa : Rutas("cita_exitosa_screen")
}