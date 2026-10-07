package com.yldefonso.clinicasaludplus.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Rutas(val ruta: String, val titulo: String = "", val icono: ImageVector? = null) {
    object Splash : Rutas("splash_screen")
    object Login : Rutas("login_screen")
    object Registro : Rutas("registro_screen")
    object Terminos : Rutas("terminos_screen")

    // Destinos de la NavigationBar (Hito C)
    object Home : Rutas("home_screen", "Inicio", Icons.Default.Home)
    object Especialidades : Rutas("especialidades_screen", "Especialidades", Icons.Default.List)
    object MisCitas : Rutas("mis_citas_screen", "Mis Citas", Icons.Default.DateRange)
    object Perfil : Rutas("perfil_screen", "Perfil", Icons.Default.Person)

    // Rutas con parámetros o secundarias (Hito D)
    object Medicos : Rutas("medicos_screen/{especialidadId}") {
        fun crearRuta(especialidadId: Int) = "medicos_screen/$especialidadId"
    }
    object FechaHora : Rutas("fecha_hora_screen")
    object ConfirmarCita : Rutas("confirmar_cita_screen")
    object CitaExitosa : Rutas("cita_exitosa_screen")
}