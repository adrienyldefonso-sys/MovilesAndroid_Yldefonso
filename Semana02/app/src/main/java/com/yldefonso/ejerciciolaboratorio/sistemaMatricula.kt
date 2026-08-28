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
fun determinarCargaAcademica(totalCreditos: Int): String {
    return if (totalCreditos <= 12) {
        "Malla regular"
    } else if (totalCreditos in 13..18) {
        "Carga completa"
    } else {
        "Requiere autorizacion"
    }
}

fun determinarFormaPago(totalAPagar: Double): String {
    return if (totalAPagar > 1500.0) {
        val montoCuota = totalAPagar / 3
        String.format("3 cuotas de S/ %.2f cada una", montoCuota)
    } else {
        val montoCuota = totalAPagar / 2
        String.format("2 cuotas de S/ %.2f cada una", montoCuota)
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

    val totalAPagar = totalCreditos * valorCredito
    val cargaAcademica = determinarCargaAcademica(totalCreditos)
    val formaPago = determinarFormaPago(totalAPagar)

    println()
    println("=========================================")
    println(" RESUMEN DE MATRICULA ")
    println("=========================================")
    println("Estudiante: $nombre")
    println("Cursos matriculados: $cantidadCursos")
    println("Total de creditos: $totalCreditos")
    println(String.format("Total a pagar: S/ %.2f", totalAPagar))
    println("Carga academica: $cargaAcademica")
    println("Forma de pago: $formaPago")
}