package com.yldefonso.clinicasaludplus.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")

    object DoctorProfile : Screen("doctor_profile/{doctorId}") {
        fun createRoute(doctorId: Int) = "doctor_profile/$doctorId"
    }

    object BookAppointment : Screen("book_appointment/{doctorId}") {
        fun createRoute(doctorId: Int) = "book_appointment/$doctorId"
    }

    object Confirmation : Screen("confirmation/{citaId}") {
        fun createRoute(citaId: Int) = "confirmation/$citaId"
    }

    object MisCitas : Screen("mis_citas")
    object HistorialMedico : Screen("historial_medico")

    object Perfil : Screen("perfil")
}