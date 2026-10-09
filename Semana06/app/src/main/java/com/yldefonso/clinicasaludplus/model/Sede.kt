package com.yldefonso.clinicasaludplus.model

data class Sede(
    val id: Int,
    val nombre: String,
    val direccion: String,
    val distrito: String,
    val telefono: String,
    val horario: String,
    val especialidadesIds: List<Int> = emptyList()
)
