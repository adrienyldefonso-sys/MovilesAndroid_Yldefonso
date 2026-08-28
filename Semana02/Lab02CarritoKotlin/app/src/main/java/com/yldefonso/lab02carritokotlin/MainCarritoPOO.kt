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
    println("1. Agregar Accesorio")
    println("2. Agregar Software")
    println("3. Buscar producto")
    println("4. Eliminar producto")
    println("5. Mostrar detalle del carrito")
    println("6. Mostrar totales (subtotal, IGV, descuento)")
    println("7. Salir")
    print("Elige una opcion: ")
}

fun main() {
    val tienda = TiendaCarrito()
    println("Bienvenido, Becker Yldefonso Solis")

    while (true) {
        mostrarMenu()
        val opcion = readLine()?.trim()

        when (opcion) {
            "1" -> {
                print("Nombre del accesorio: ")
                val nombre = readLine() ?: ""
                val precio = leerDouble("Precio: ")
                val cantidad = leerInt("Cantidad: ")
                print("Tiene garantia extendida? (s/n): ")
                val garantia = readLine()?.trim()?.lowercase() == "s"
                tienda.agregarProducto(Accesorio(nombre, precio, cantidad, garantia))
            }
            "2" -> {
                print("Nombre del software: ")
                val nombre = readLine() ?: ""
                val precio = leerDouble("Precio: ")
                val cantidad = leerInt("Cantidad: ")
                print("Tipo de licencia (Mensual/Anual/Perpetua): ")
                val licencia = readLine() ?: "Mensual"
                tienda.agregarProducto(Software(nombre, precio, cantidad, licencia))
            }
            "3" -> {
                print("Nombre del producto a buscar: ")
                val nombre = readLine() ?: ""
                val encontrado = tienda.buscarProducto(nombre)
                if (encontrado != null) {
                    println("Encontrado: ${encontrado.mostrarInfo()}")
                } else {
                    println("Producto '$nombre' no encontrado.")
                }
            }
            "4" -> {
                print("Nombre del producto a eliminar: ")
                val nombre = readLine() ?: ""
                val eliminado = tienda.eliminarProducto(nombre)
                if (eliminado) {
                    println("Producto '$nombre' eliminado correctamente.")
                } else {
                    println("Producto '$nombre' no encontrado, no se elimino nada.")
                }
            }
            "5" -> tienda.mostrarDetalle()
            "6" -> tienda.mostrarTotales()
            "7" -> {
                println("Gracias por usar el carrito. Hasta pronto!")
                exitProcess(0)
            }
            else -> println("Opcion invalida, intenta de nuevo.")
        }
    }
}