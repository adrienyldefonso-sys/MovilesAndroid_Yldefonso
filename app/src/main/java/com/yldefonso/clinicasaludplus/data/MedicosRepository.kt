package com.yldefonso.clinicasaludplus.data

import com.yldefonso.clinicasaludplus.model.Medico
//Simula lo que vendria de una API o una bd siendo una fuente de datos
object MedicoRepository{
    val especialidades = listOf("Todos", "Cardiología", "Pediatría", "Dermatología")
    val medicos= listOf(
        Medico(
            id=1,
            nombre= "Dra Ana Torres",
            especialidad = "Cardiologia",
            calificacion = 4.9,
            resenas=128,
            experienciaAnios =12,
            descripcion = "Especialista en arritmias e hipertension,formacion en la Clinica 2 de Mayo"
        ),Medico(
            id = 2,
            nombre = "Dr. Luis Vega",
            especialidad = "Pediatría",
            calificacion =4.7,
            resenas =96,
            experienciaAnios =8,
            descripcion ="Enfocado en el desarrollo infantil y control de niño sano."
        ),
        Medico(
            id = 3,
            nombre ="Dra. Rosa Díaz",
            especialidad="Dermatología",
            calificacion =4.8,
            resenas =74,
            experienciaAnios= 10,
            descripcion ="Especialista en dermatología clínica y estética."
        )
    )
}
