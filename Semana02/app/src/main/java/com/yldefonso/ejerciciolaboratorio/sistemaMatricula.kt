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

// ---------- NUEVO: TURNO ----------

fun leerTurno(mensaje: String): String {
    while (true) {
        print(mensaje)
        val valor = readLine()?.trim()?.uppercase()

        when (valor) {
            "M", "MAÑANA" -> return "MAÑANA"
            "T", "TARDE" -> return "TARDE"
            "N", "NOCHE" -> return "NOCHE"
        }

        println("Turno invalido. Ingresa M (Manana), T (Tarde) o N (Noche).")
    }
}

fun obtenerRecargoTurno(turno: String): Double {
    return when (turno) {
        "MAÑANA" -> 0.10
        "TARDE" -> 0.15
        "NOCHE" -> 0.20
        else -> 0.0
    }
}

// -----------------------------------

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

    if (totalCreditos > 18) {
        println()
        println("Se requiere autorizacion")
        return
    }

    val valorCredito = leerDouble("Valor de cada credito (S/): ")

    // NUEVO: turno y recargo
    val turno = leerTurno("Turno (M=Mañana / T=Tarde / N=Noche): ")
    val recargoTurno = obtenerRecargoTurno(turno)

    val subtotalCursos = totalCreditos * valorCredito
    val montoRecargoTurno = subtotalCursos * recargoTurno
    val totalAPagar = subtotalCursos + montoRecargoTurno

    val cargaAcademica = determinarCargaAcademica(totalCreditos)
    val formaPago = determinarFormaPago(totalAPagar)

    println()
    println("=========================================")
    println(" RESUMEN DE MATRICULA ")
    println("=========================================")

    println("ESTUDIANTE: $nombre")
    println("TURNO: $turno")
    println()

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

    println("CURSOS MATRICULADOS: $cantidadCursos")
    println("TOTAL CREDITOS: $totalCreditos")
    println(String.format("SUBTOTAL CURSOS: S/ %.2f", subtotalCursos))
    println(String.format("RECARGO POR TURNO (%.0f%%): S/ %.2f", recargoTurno * 100, montoRecargoTurno))
    println(String.format("TOTAL A PAGAR: S/ %.2f", totalAPagar))
    println("CARGA ACADEMICA: $cargaAcademica")
    println("FORMA DE PAGO: $formaPago")
}
