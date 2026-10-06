package com.yldefonso.clinicasaludplus.model

data class Medico(
    val id: Int,
    val nombre: String,
    val especialidadId: Int,
    val cmp: String,
    val disponibilidad: List<String> = emptyList()
)