package com.yldefonso.lab02carritokotlin

class ProductoDigital(
    nombre: String,
    precio: Double,
    cantidad: Int
) : Producto(nombre, precio, cantidad) {

    override fun calcularImporte(): Double {
        return precio * cantidad
    }
    override fun mostrarInfo(): String {
        return String.format("%-20s x%d S/ %8.2f (descarga digital)",
            nombre, cantidad, calcularImporte())
    }
}