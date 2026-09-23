package com.yldefonso.clinicasaludplus.model

//Representa al medico disponible y se establecen sus atributos
data class Medico(
    val id : Int,
    val nombre : String,
    val especialidad: String,
    val calificacion : Double,
    val resenas : Int,
    val experienciaAnios: Int,
    val descripcion : String
)
