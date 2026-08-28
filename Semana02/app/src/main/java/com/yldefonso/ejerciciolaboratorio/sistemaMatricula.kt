package com.yldefonso.ejerciciolaboratorio

fun leerTexto(mensaje: String): String {
    print(mensaje)
    return readLine() ?: ""
}

fun leerEntero(mensaje: String): Int {
    while (true) {
        print(mensaje)
        val valor = readLine()?.toIntOrNull()
        if (valor != null && valor > 0) return valor
        println("Valor invalido, ingresa un numero entero mayor a 0.")
    }
}

fun leerDouble(mensaje: String): Double {
    while (true) {
        print(mensaje)
        val valor = readLine()?.toDoubleOrNull()
        if (valor != null && valor > 0) return valor
        println("Valor invalido, ingresa un numero mayor a 0.")
    }
}

fun main() {
    println("=========================================")
    println(" SISTEMA DE MATRICULA - TECSUP ")
    println("=========================================")

    val nombre = leerTexto("Nombre del estudiante: ")
    val cantidadCursos = leerEntero("Cantidad de cursos a matricular: ")

    var totalCreditos = 0
    for (i in 1..cantidadCursos) {
        val creditosCurso = leerEntero("Creditos del curso $i: ")
        totalCreditos += creditosCurso
    }

    val valorCredito = leerDouble("Valor de cada credito (S/): ")

    println("Datos ingresados correctamente.")
}