package com.yldefonso.lab02carritokotlin

class Carritoo {
    private val productos = mutableListOf<Producto>()

    fun agregarProducto(producto: Producto) {
        productos.add(producto)
        println("Producto agregado: ${producto.nombre}")
    }

    fun eliminarProducto(nombre: String) {
        productos.removeIf { it.nombre == nombre }
    }

    fun obtenerCantidadProductos(): Int = productos.size

    fun calcularSubtotal(): Double {
        var subtotal = 0.0
        for (p in productos) {
            subtotal += p.calcularImporte()
        }
        return subtotal
    }

    fun calcularIGV(subtotal: Double): Double = subtotal * 0.18

    fun calcularTotal(subtotal: Double, igv: Double): Double = subtotal + igv

    fun calcularDescuento(total: Double): Double {
        return when {
            total > 5000 -> total * 0.10
            total > 3000 -> total * 0.05
            else -> 0.0
        }
    }

    fun productoMasCaro(): Producto? {
        return productos.maxByOrNull { it.precio }
    }

    fun mostrarDetalle() {
        println("--------- DETALLE DEL CARRITO ---------")
        var i = 1
        for (p in productos) {
            println("$i. ${p.mostrarInfo()}")
            i++
        }
        println("---------------------------------------")
    }
}