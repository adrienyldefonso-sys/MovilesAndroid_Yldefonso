package com.yldefonso.lab02carritokotlin

class ProductoFisico(
    nombre: String,
    precio: Double,
    cantidad: Int,
    val costoEnvio: Double
) : Producto(nombre, precio, cantidad) {

    override fun calcularImporte(): Double {
        return (precio * cantidad) + costoEnvio
    }
    override fun mostrarInfo(): String {
        return String.format("%-20s x%d S/ %8.2f (incluye envio S/ %.2f)",
            nombre, cantidad, calcularImporte(), costoEnvio)
    }
}