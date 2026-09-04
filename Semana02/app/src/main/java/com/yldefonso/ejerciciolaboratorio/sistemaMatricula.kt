package com.yldefonso.ejerciciolaboratorio

// NUEVO: leerTexto ahora valida que no se ingrese un valor vacio o solo espacios
fun leerTexto(mensaje: String): String {
    while (true) {
        print(mensaje)
        val valor = readLine()?.trim()

        if (!valor.isNullOrEmpty()) {
            return valor
        }

        println("Este campo no puede estar vacio, ingresa un valor.")
    }
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

fun leerCategoria(mensaje: String): String {
    while (true) {
        print(mensaje)
        val valor = readLine()?.trim()?.uppercase()

        when (valor) {
            "O", "ORDINARIO" -> return "ORDINARIO"
            "B", "BECADO" -> return "BECADO"
        }

        println("Categoria invalida. Ingresa O (Ordinario) o B (Becado).")
    }
}

fun leerConfirmacion(mensaje: String): Boolean {
    while (true) {
        print(mensaje)
        val valor = readLine()?.trim()?.uppercase()

        when (valor) {
            "S" -> return true
            "N" -> return false
        }

        println("Respuesta invalida. Ingresa S (Si) o N (No).")
    }
}

fun calcularIGV(montoBase: Double): Double {
    val TASA_IGV = 0.18
    return montoBase * TASA_IGV
}

fun main() {

    println("=========================================")
    println(" SISTEMA DE MATRICULA - TECSUP ")
    println("=========================================")

    var aforoDisponible = leerEntero("Ingrese el aforo disponible: ")

    if (aforoDisponible <= 0) {
        println()
        println("No hay vacantes disponibles. No se puede matricular.")
        return
    }

    var continuarMatriculando = true

    while (continuarMatriculando && aforoDisponible > 0) {

        println()

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
            continuarMatriculando = leerConfirmacion("Desea continuar matriculando a otro estudiante? (S/N): ")
            continue
        }

        val valorCredito = leerDouble("Valor de cada credito (S/): ")

        val turno = leerTurno("Turno (M=Mañana / T=Tarde / N=Noche): ")
        val recargoTurno = obtenerRecargoTurno(turno)

        val categoria = leerCategoria("Categoria (O=Ordinario / B=Becado): ")
        var montoMatricula = 0.0

        if (categoria == "ORDINARIO") {
            montoMatricula = leerDouble("Costo de la matricula (S/): ")
        } else {
            println("Matricula becada: S/ 0.00")
        }

        val subtotalCursos = totalCreditos * valorCredito
        val montoRecargoTurno = subtotalCursos * recargoTurno

        val totalSinIGV = subtotalCursos + montoRecargoTurno + montoMatricula
        val igv = calcularIGV(totalSinIGV)
        val totalAPagar = totalSinIGV + igv

        val cargaAcademica = determinarCargaAcademica(totalCreditos)
        val formaPago = determinarFormaPago(totalAPagar)

        println()
        println("=========================================")
        println(" RESUMEN DE MATRICULA ")
        println("=========================================")

        println("ESTUDIANTE: $nombre")
        println("TURNO: $turno")
        println("CATEGORIA: $categoria")
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
        println(String.format("PAGO DE MATRICULA: S/ %.2f", montoMatricula))
        println(String.format("IGV (18%%): S/ %.2f", igv))
        println(String.format("TOTAL A PAGAR: S/ %.2f", totalAPagar))
        println("CARGA ACADEMICA: $cargaAcademica")
        println("FORMA DE PAGO: $formaPago")

        aforoDisponible--
        println("AFORO RESTANTE: $aforoDisponible vacante(s).")

        if (aforoDisponible > 0) {
            continuarMatriculando = leerConfirmacion("Desea continuar matriculando a otro estudiante? (S/N): ")
        } else {
            println()
            println("Ya no hay aforo disponible. Se finaliza el proceso de matriculas.")
            continuarMatriculando = false
        }
    }

    println()
    println("Fin del proceso de matriculas.")
}