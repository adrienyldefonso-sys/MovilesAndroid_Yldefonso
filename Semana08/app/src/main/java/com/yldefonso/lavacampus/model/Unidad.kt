package com.yldefonso.lavacampus.model

enum class Unidad(val etiqueta: String) {
    KG("kg"), PRENDA("prenda"), UNIDAD("unidad");

    companion object {
        fun desde(valor: String): Unidad = entries.firstOrNull { it.name == valor } ?: UNIDAD
    }
}
