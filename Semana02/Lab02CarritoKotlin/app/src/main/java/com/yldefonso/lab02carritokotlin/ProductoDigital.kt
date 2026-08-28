package com.yldefonso.lab02carritokotlin

class Software(
    nombre: String,
    precio: Double,
    cantidad: Int,
    val tipoLicencia: String
) : Producto(nombre, precio, cantidad) {

    override fun calcularImporte(): Double {
        return precio * cantidad
    }

    override fun mostrarInfo(): String {
        return String.format("%-20s x%d S/ %8.2f (licencia %s)",
            nombre, cantidad, calcularImporte(), tipoLicencia)
    }
}