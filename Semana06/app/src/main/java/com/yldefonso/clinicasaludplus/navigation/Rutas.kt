package com.yldefonso.clinicasaludplus.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Rutas(val ruta: String, val titulo: String = "", val icono: ImageVector? = null) {
    // Autenticación
    object Splash : Rutas("splash_screen")
    object Login : Rutas("login_screen")
    object Registro : Rutas("registro_screen")
    object Terminos : Rutas("terminos_screen")

    // Destinos de la NavigationBar (BottomBar)
    object Home : Rutas("home_screen", "Inicio", Icons.Default.Home)
    object Especialidades : Rutas("especialidades_screen", "Especialidades", Icons.Default.List)
    object MisCitas : Rutas("mis_citas_screen", "Mis Citas", Icons.Default.DateRange)
    object Perfil : Rutas("perfil_screen", "Perfil", Icons.Default.Person)

    // Flujo de Agendamiento
    object Medicos : Rutas("medicos_screen/{especialidadId}") {
        fun crearRuta(especialidadId: Int) = "medicos_screen/$especialidadId"
    }
    object FechaHora : Rutas("fecha_hora_screen")
    object ConfirmarCita : Rutas("confirmar_cita_screen")
    object CitaExitosa : Rutas("cita_exitosa_screen")
    object DetalleCita : Rutas("detalle_cita_screen")

    // Módulos Complementarios
    object Notificaciones : Rutas("notificaciones_screen")
    object Resultados : Rutas("resultados_screen")
}