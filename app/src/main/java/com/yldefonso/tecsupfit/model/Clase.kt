package com.yldefonso.tecsupfit.model

// Representa una clase de gimnasio disponible para reservar.
data class Clase(
    val id: Int,
    val nombre: String,
    val horario: String,        // ejemplo 6:00 pm
    val sala: String,           // ejemplo Sala 1
    val duracionMin: Int,       // ejempl 45
    val descripcion: String,
    val cuposTotales: Int,
    val cuposDisponibles: Int,
    val categoria: String       //"Hoy" o "Esta semana",es usado por el filtro
)