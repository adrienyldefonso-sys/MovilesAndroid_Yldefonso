package com.yldefonso.clinicasaludplus.model

data class Cita(
    val id: Int,
    val codigoReserva: String,
    val usuarioId: Int,
    val medico: Medico,
    val especialidad: Especialidad,
    val fecha: String,
    val hora: String,
    val estado: String = "Confirmada"
)