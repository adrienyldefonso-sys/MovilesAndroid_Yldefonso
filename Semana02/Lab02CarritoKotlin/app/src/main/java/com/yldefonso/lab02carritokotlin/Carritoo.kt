package com.yldefonso.lab02carritokotlin

class Carritoo {
    private val productos = mutableListOf<Producto>()

    fun agregarProducto(producto: Producto) {
        productos.add(producto)
        println("Producto agregado: ${producto.nombre}")
    }

    fun eliminarProducto(nombre: String): Boolean {
        return productos.removeIf { it.nombre.equals(nombre, ignoreCase = true) }
    }

    fun buscarProducto(nombre: String): Producto? {
        return productos.find { it.nombre.equals(nombre, ignoreCase = true) }
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
        if (productos.isEmpty()) {
            println("El carrito esta vacio.")
            return
        }
        println("--------- DETALLE DEL CARRITO ---------")
        var i = 1
        for (p in productos) {
            println("$i. ${p.mostrarInfo()}")
            i++
        }
        println("---------------------------------------")
    }

    fun mostrarTotales() {
        if (productos.isEmpty()) {
            println("El carrito esta vacio, no hay totales que calcular.")
            return
        }
        val subtotal = calcularSubtotal()
        val igv = calcularIGV(subtotal)
        val total = calcularTotal(subtotal, igv)
        val descuento = calcularDescuento(total)
        val totalConDescuento = total - descuento

        println(String.format("Subtotal:        S/ %8.2f", subtotal))
        println(String.format("IGV (18%%):       S/ %8.2f", igv))
        println(String.format("TOTAL A PAGAR:   S/ %8.2f", total))
        if (descuento > 0) {
            println(String.format("Descuento aplicado: S/ %.2f", descuento))
        }
        println(String.format("TOTAL CON DESCUENTO: S/ %8.2f", totalConDescuento))

        val masCaro = productoMasCaro()
        if (masCaro != null) {
            println("Producto mas caro: ${masCaro.nombre} " +
                    String.format("(S/ %.2f)", masCaro.precio))
        }
    }
}