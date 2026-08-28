package com.yldefonso.ejerciciolaboratorio

fun leerTexto(mensaje: String): String {
    print(mensaje)
    return readLine() ?: ""
}

fun leerEntero(mensaje: String): Int {
    while (true) {
        print(mensaje)
        val valor = readLine()?.toIntOrNull()

        if (valor != null && valor > 0) {
            return valor
        }

        println("Valor invalido, ingresa un numero entero mayor a 0.")
    }
}

fun leerDouble(mensaje: String): Double {
    while (true) {
        print(mensaje)
        val valor = readLine()?.toDoubleOrNull()

        if (valor != null && valor > 0) {
            return valor
        }

        println("Valor invalido, ingresa un numero mayor a 0.")
    }
}

fun determinarCargaAcademica(totalCreditos: Int): String {
    return if (totalCreditos <= 12) {
        "Malla regular"
    } else {
        "Carga completa"
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

    val nombresCursos = mutableListOf<String>()
    val creditosCursos = mutableListOf<Int>()

    var totalCreditos = 0

    for (i in 1..cantidadCursos) {

        val nombreCurso = leerTexto("Nombre del curso $i: ")
        val creditosCurso = leerEntero("Creditos del curso $i: ")

        nombresCursos.add(nombreCurso)
        creditosCursos.add(creditosCurso)

        totalCreditos += creditosCurso
    }

    // Si supera los 18 créditos, termina el programa
    if (totalCreditos > 18) {
        println()
        println("Se requiere autorizacion")
        return
    }

    val valorCredito = leerDouble("Valor de cada credito (S/): ")

    val totalAPagar = totalCreditos * valorCredito
    val cargaAcademica = determinarCargaAcademica(totalCreditos)
    val formaPago = determinarFormaPago(totalAPagar)

    println()
    println("=========================================")
    println(" RESUMEN DE MATRICULA ")
    println("=========================================")

    println("ESTUDIANTE: $nombre")
    println()

    // TABLA DE CURSOS
    println("CURSO                         CREDITOS       COSTO")
    println("-------------------------------------------------------")

    for (i in nombresCursos.indices) {

        val costoCurso = creditosCursos[i] * valorCredito

        println(
            String.format(
                "%-30s %8d     S/ %8.2f",
                nombresCursos[i],
                creditosCursos[i],
                costoCurso
            )
        )
    }

    println("-------------------------------------------------------")

    // RESUMEN FINAL SIN TABLA
    println("CURSOS MATRICULADOS: $cantidadCursos")
    println("TOTAL CREDITOS: $totalCreditos")
    println(String.format("TOTAL A PAGAR: S/ %.2f", totalAPagar))
    println("CARGA ACADEMICA: $cargaAcademica")
    println("FORMA DE PAGO: $formaPago")
}
