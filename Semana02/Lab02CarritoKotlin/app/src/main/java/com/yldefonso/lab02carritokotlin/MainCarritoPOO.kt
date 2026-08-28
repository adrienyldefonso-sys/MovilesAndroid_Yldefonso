package com.yldefonso.lab02carritokotlin

fun main() {
    println("=========================================")
    println(" CARRITO DE COMPRAS POO - TIENDA TECSUP ")
    println("=========================================")

    val nombreCliente = "Becker Yldefonso Solis"
    println("Cliente: $nombreCliente")
    println()

    val carritoo = Carritoo()

    carritoo.agregarProducto(ProductoFisico("Laptop HP", 2500.0, 1, 50.0))
    carritoo.agregarProducto(ProductoFisico("Mouse Logitech", 45.5, 2, 10.0))
    carritoo.agregarProducto(ProductoDigital("Curso Kotlin Online", 120.0, 1))
    carritoo.agregarProducto(ProductoDigital("Antivirus Licencia", 90.0, 1))

    println()
    carritoo.mostrarDetalle()
    println("Cantidad de productos: ${carritoo.obtenerCantidadProductos()}")
    println()

    val subtotal = carritoo.calcularSubtotal()
    val igv = carritoo.calcularIGV(subtotal)
    val total = carritoo.calcularTotal(subtotal, igv)

    println(String.format("Subtotal:        S/ %8.2f", subtotal))
    println(String.format("IGV (18%%):       S/ %8.2f", igv))
    println(String.format("TOTAL A PAGAR:   S/ %8.2f", total))
    println()

    val masCaro = carritoo.productoMasCaro()
    if (masCaro != null) {
        println("Producto mas caro: ${masCaro.nombre} " +
                String.format("(S/ %.2f)", masCaro.precio))
    }

    val descuento = carritoo.calcularDescuento(total)
    val totalConDescuento = total - descuento
    if (descuento > 0) {
        println(String.format("Descuento aplicado: S/ %.2f", descuento))
    }
    println(String.format("TOTAL CON DESCUENTO: S/ %8.2f", totalConDescuento))
}