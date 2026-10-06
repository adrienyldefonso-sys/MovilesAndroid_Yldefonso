package com.yldefonso.clinicasaludplus.navigation

sealed class Rutas(val ruta: String) {
    object Splash : Rutas("splash")
    object Registro : Rutas("registro")
    object Login : Rutas("login")
    object Terminos : Rutas("terminos")
    object Home : Rutas("home")
    object Especialidades : Rutas("especialidades")
    object Medicos : Rutas("medicos/{especialidadId}") {
        fun crearRuta(especialidadId: Int) = "medicos/$especialidadId"
    }
    object FechaHora : Rutas("fecha_hora/{medicoId}") {
        fun crearRuta(medicoId: Int) = "fecha_hora/$medicoId"
    }
    object ConfirmarCita : Rutas("confirmar_cita/{medicoId}/{fecha}/{hora}") {
        fun crearRuta(medicoId: Int, fecha: String, hora: String) = "confirmar_cita/$medicoId/$fecha/$hora"
    }
    object CitaExitosa : Rutas("cita_exitosa")
    object MisCitas : Rutas("mis_citas")
    object Perfil : Rutas("perfil")
    object Resultados : Rutas("resultados")
    object Notificaciones : Rutas("notificaciones")
    object DetalleCita : Rutas("detalle_cita/{citaId}") {
        fun crearRuta(citaId: Int) = "detalle_cita/$citaId"
    }
}