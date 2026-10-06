package com.yldefonso.clinicasaludplus.model

data class Especialidad(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val iconoRes: Int = 0
)