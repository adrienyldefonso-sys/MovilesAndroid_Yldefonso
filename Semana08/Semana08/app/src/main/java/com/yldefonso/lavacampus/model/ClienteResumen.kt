package com.yldefonso.lavacampus.model

data class ClienteResumen(
    val id: Int,
    val nombre: String,
    val codigo: String,
    val cantidadPedidos: Int,
    val totalGastado: Double
)
