package com.yldefonso.tecsupfit.data

import com.yldefonso.tecsupfit.model.Clase

//Fuente de datos mock (sin backend).
object ClasesRepository {

    //Chips de filtro del LazyRow de Inicio (mínimo 2)
    val filtros = listOf("Hoy", "Esta semana")

    val clases = listOf(
        Clase(
            id = 1,
            nombre = "Yoga funcional",
            horario = "7:00 am",
            sala = "Sala 2",
            duracionMin = 50,
            descripcion = "Trabajo de movilidad y respiración para empezar el día.",
            cuposTotales = 15,
            cuposDisponibles = 6,
            categoria = "Hoy"
        ),
        Clase(
            id = 2,
            nombre = "Cross Training",
            horario = "6:00 pm",
            sala = "Sala 1",
            duracionMin = 45,
            descripcion = "Entrenamiento funcional de alta intensidad. Cupos limitados.",
            cuposTotales = 12,
            cuposDisponibles = 8,
            categoria = "Hoy"
        ),
        Clase(
            id = 3,
            nombre = "Spinning",
            horario = "7:30 pm",
            sala = "Sala 3",
            duracionMin = 40,
            descripcion = "Cardio intenso en bicicleta estática, ideal para resistencia.",
            cuposTotales = 20,
            cuposDisponibles = 10,
            categoria = "Esta semana"
        )
    )
}