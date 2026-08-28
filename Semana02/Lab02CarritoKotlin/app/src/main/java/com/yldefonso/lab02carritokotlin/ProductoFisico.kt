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
}