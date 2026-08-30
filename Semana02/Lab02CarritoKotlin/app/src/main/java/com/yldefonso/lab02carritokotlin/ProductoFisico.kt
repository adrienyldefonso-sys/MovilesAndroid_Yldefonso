package com.yldefonso.lab02carritokotlin

class ProductoFisico(
    nombre: String,
    precio: Double,
    cantidad: Int,
    val tieneGarantia: Boolean
) : Producto(nombre, precio, cantidad) {

    override fun calcularImporte(): Double {
        val recargoGarantia = if (tieneGarantia) precio * 0.05 else 0.0
        return (precio * cantidad) + recargoGarantia
    }

    override fun mostrarInfo(): String {
        val garantiaTexto = if (tieneGarantia) "con garantia extendida" else "sin garantia"
        return String.format("%-20s x%d S/ %8.2f (%s)",
            nombre, cantidad, calcularImporte(), garantiaTexto)
    }
}