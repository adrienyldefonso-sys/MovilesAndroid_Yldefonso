package com.yldefonso.clinicasaludplus.model

data class Cita(
    val id: Int,
    val codigoReserva: String,
    val usuario: Usuario,
    val especialidad: Especialidad,
    val medico: Medico,
    val fecha: String,
    val hora: String
)