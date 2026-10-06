package com.yldefonso.clinicasaludplus.model

data class Usuario(
    val id: Int,
    val nombre: String,
    val telefono: String,
    val correo: String,
    val contrasena: String
)