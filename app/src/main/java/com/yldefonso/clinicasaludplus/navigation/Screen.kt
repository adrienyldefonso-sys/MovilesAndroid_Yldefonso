package com.yldefonso.clinicasaludplus.navigation

sealed class Screen (val route : String){
    object Home: Screen ("home")
    //Aca recibira el id del medico que se eligira
    object DoctorProfile : Screen("doctor_profile/{doctorId}"){
        fun createRoute(doctorId : Int) = "doctor_profile/$doctorId"
    }

    //Recibe el id del medico para saber a quien se le agenda la cita
    object BookAppointment : Screen("book_appointment/{doctorId}"){
        fun createRoute(doctorId: Int) = "book_appointment/$doctorId"
    }

    //En este caso recibe el id de la cita creada
    object Confirmation : Screen ("confirmation/{citaId}"){
        fun createRoute(citaId : Int) = "confirmation/$citaId"
    }

    object MisCitas : Screen ("mis_citas")
    object HistorialMedico: Screen("historial_medico")
}