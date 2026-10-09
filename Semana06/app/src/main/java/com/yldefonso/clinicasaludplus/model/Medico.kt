package com.yldefonso.clinicasaludplus.model

import java.time.DayOfWeek

data class Medico(
    val id: Int,
    val nombre: String,
    val especialidadId: Int,
    val cmp: String,
    val fotoUrl: String = "",
    val disponibilidad: List<String> = emptyList(),
    val diasAtencion: List<DayOfWeek> = emptyList()
)