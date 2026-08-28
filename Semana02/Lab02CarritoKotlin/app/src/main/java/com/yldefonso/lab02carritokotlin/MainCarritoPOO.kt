package com.yldefonso.lab02carritokotlin

import kotlin.system.exitProcess

fun leerDouble(mensaje: String): Double {
    while (true) {
        print(mensaje)
        val valor = readLine()?.toDoubleOrNull()
        if (valor != null) return valor
        println("Valor invalido, ingresa un numero.")
    }
}

fun leerInt(mensaje: String): Int {
    while (true) {
        print(mensaje)
        val valor = readLine()?.toIntOrNull()
        if (valor != null) return valor
        println("Valor invalido, ingresa un numero entero.")
    }
}

fun mostrarMenu() {
    println()
    println("=========================================")
    println(" CARRITO DE COMPRAS POO - TIENDA TECSUP ")
    println("=========================================")
    println("1. Agregar Producto Fisico")
    println("2. Agregar Producto Digital")
    println("3. Buscar producto")
    println("4. Eliminar producto")
    println("5. Mostrar detalle del carrito")
    println("6. Mostrar totales (subtotal, IGV, descuento)")
    println("7. Salir")
    print("Elige una opcion: ")
}

fun main() {
    val carritoo = Carritoo()
    println("Bienvenido, Becker Yldefonso Solis")

    while (true) {
        mostrarMenu()
        val opcion = readLine()?.trim()

        when (opcion) {
            "1" -> {
                print("Nombre del producto fisico: ")
                val nombre = readLine() ?: ""
                val precio = leerDouble("Precio: ")
                val cantidad = leerInt("Cantidad: ")
                print("Tiene garantia extendida? (s/n): ")
                val garantia = readLine()?.trim()?.lowercase() == "s"
                carritoo.agregarProducto(ProductoFisico(nombre, precio, cantidad, garantia))
            }
            "2" -> {
                print("Nombre del producto digital: ")
                val nombre = readLine() ?: ""
                val precio = leerDouble("Precio: ")
                val cantidad = leerInt("Cantidad: ")
                print("Tipo de licencia (Mensual/Anual/Perpetua): ")
                val licencia = readLine() ?: "Mensual"
                carritoo.agregarProducto(ProductoDigital(nombre, precio, cantidad, licencia))
            }
            "3" -> {
                print("Nombre del producto a buscar: ")
                val nombre = readLine() ?: ""
                val encontrado = carritoo.buscarProducto(nombre)
                if (encontrado != null) {
                    println("Encontrado: ${encontrado.mostrarInfo()}")
                } else {
                    println("Producto '$nombre' no encontrado.")
                }
            }
            "4" -> {
                print("Nombre del producto a eliminar: ")
                val nombre = readLine() ?: ""
                val eliminado = carritoo.eliminarProducto(nombre)
                if (eliminado) {
                    println("Producto '$nombre' eliminado correctamente.")
                } else {
                    println("Producto '$nombre' no encontrado, no se elimino nada.")
                }
            }
            "5" -> carritoo.mostrarDetalle()
            "6" -> carritoo.mostrarTotales()
            "7" -> {
                println("Gracias por usar el carrito. Hasta pronto!")
                exitProcess(0)
            }
            else -> println("Opcion invalida, intenta de nuevo.")
        }
    }
}}