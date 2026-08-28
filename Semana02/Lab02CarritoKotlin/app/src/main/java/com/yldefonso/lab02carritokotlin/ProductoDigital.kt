package com.yldefonso.lab02carritokotlin

class ProductoDigital(
    nombre: String,
    precio: Double,
    cantidad: Int
) : Producto(nombre, precio, cantidad) {

    override fun calcularImporte(): Double {
        return precio * cantidad
    }
}