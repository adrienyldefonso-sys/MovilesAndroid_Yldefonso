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

    // Destinos exactos del NavigationBar según la rúbrica
    object Home : Rutas("home_screen", "Inicio", Icons.Default.Home)
    object MisCitas : Rutas("mis_citas_screen", "Citas", Icons.Default.DateRange)
    object Resultados : Rutas("resultados_screen", "Resultados", Icons.Default.List)
    object Perfil : Rutas("perfil_screen", "Perfil", Icons.Default.Person)

    // Agendamiento y vistas secundarias
    object Especialidades : Rutas("especialidades_screen")
    object Medicos : Rutas("medicos_screen/{especialidadId}") {
        fun crearRuta(especialidadId: Int) = "medicos_screen/$especialidadId"
    }
    object FechaHora : Rutas("fecha_hora_screen")
    object ConfirmarCita : Rutas("confirmar_cita_screen")
    object CitaExitosa : Rutas("cita_exitosa_screen")
    object DetalleCita : Rutas("detalle_cita_screen")
    object Notificaciones : Rutas("notificaciones_screen")
}