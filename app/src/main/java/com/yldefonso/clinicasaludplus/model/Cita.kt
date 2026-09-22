package com.yldefonso.clinicasaludplus.model
//Representa la cita agendada por el usuario y el estado es sera para las opciones
//Confirmada y completada
data class Cita(
    val id: Int,
    val medicoId : Int,
    val medicoNombre: String,
    val especialidad:String,
    val fecha:String,
    val hora:String,
    val estado:String
)
