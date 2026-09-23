package com.yldefonso.tecsupfit.model

// Representa una clase ya reservada por el usuario.
data class Reserva(
    val id: Int,
    val claseId: Int,
    val claseNombre: String,
    val horario: String,
    val sala: String,
    val estado: String   // "Confirmada" o "Completada"
)