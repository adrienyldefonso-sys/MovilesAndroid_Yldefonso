package com.yldefonso.clinicasaludplus.repository

import androidx.compose.runtime.mutableStateListOf
import com.yldefonso.clinicasaludplus.model.*

object Repositorio {

    // === AUTENTICACIÓN ===
    val usuarios = mutableListOf(
        Usuario(1, "Becker Yldefonso", "987654321",
            "becker@correo.com", "123456")
    )
    var usuarioActual: Usuario? = usuarios.firstOrNull()

    fun registrarUsuario(usuario: Usuario): Boolean {
        val existeCorreo = usuarios.any { it.correo.equals(usuario.correo.trim(), ignoreCase = true) }
        if (existeCorreo) return false
        val nuevoUsuario = usuario.copy(id = usuarios.size + 1)
        usuarios.add(nuevoUsuario)
        usuarioActual = nuevoUsuario
        return true
    }

    fun iniciarSesion(correo: String, contrasena: String): Boolean {
        val usuarioEncontrado = usuarios.find {
            it.correo.equals(correo.trim(), ignoreCase = true) && it.contrasena == contrasena
        }
        return if (usuarioEncontrado != null) {
            usuarioActual = usuarioEncontrado
            true
        } else false
    }

    fun cerrarSesion() {
        usuarioActual = null
    }

    // === CATÁLOGOS ===
    val especialidades = listOf(
        Especialidad(1, "Medicina General", "Atención primaria e integral para toda la familia."),
        Especialidad(2, "Cardiología", "Diagnóstico y tratamiento de enfermedades del corazón."),
        Especialidad(3, "Pediatría", "Cuidado médico especializado para niños y adolescentes."),
        Especialidad(4, "Dermatología", "Tratamiento de afecciones en la piel, cabello y uñas.")
    )

    val medicos = listOf(
        Medico(1, "Dr. Carlos Mendoza", 1, "CMP 45892", listOf("09:00 AM", "10:30 AM", "03:00 PM")),
        Medico(2, "Dra. Ana Torres", 1, "CMP 38210", listOf("08:00 AM", "11:00 AM", "04:00 PM")),
        Medico(3, "Dr. Roberto Gómez", 2, "CMP 51204", listOf("10:00 AM", "02:30 PM", "05:00 PM")),
        Medico(4, "Dra. Elena Ramos", 3, "CMP 29481", listOf("09:30 AM", "11:30 AM", "03:30 PM")),
        Medico(5, "Dr. Luis Paredes", 4, "CMP 60312", listOf("08:30 AM", "01:00 PM", "04:30 PM"))
    )

    // === ESTADO Y AGENDAMIENTO ===
    var especialidadSeleccionada: Especialidad? = null
    var medicoSeleccionado: Medico? = null
    var fechaSeleccionada: String = ""
    var horaSeleccionada: String = ""

    // Uso de mutableStateListOf para reactividad en Jetpack Compose
    val citasReservadas = mutableStateListOf<Cita>()

    // Búsquedas y Filtros
    fun buscarEspecialidades(query: String): List<Especialidad> {
        if (query.isBlank()) return especialidades
        return especialidades.filter { it.nombre.contains(query, ignoreCase = true) }
    }

    fun buscarMedicos(especialidadId: Int, query: String = ""): List<Medico> {
        val porEsp = medicos.filter { it.especialidadId == especialidadId }
        if (query.isBlank()) return porEsp
        return porEsp.filter { it.nombre.contains(query, ignoreCase = true) }
    }

    fun agendarCitaActual(): Cita? {
        val user = usuarioActual ?: return null
        val esp = especialidadSeleccionada ?: return null
        val med = medicoSeleccionado ?: return null
        if (fechaSeleccionada.isBlank() || horaSeleccionada.isBlank()) return null

        val nuevoId = citasReservadas.size + 1
        val nuevaCita = Cita(
            id = nuevoId,
            codigoReserva = "CIT-${1000 + nuevoId}",
            usuario = user, // Corregido a 'usuario'
            especialidad = esp,
            medico = med,
            fecha = fechaSeleccionada,
            hora = horaSeleccionada
        )
        citasReservadas.add(nuevaCita)
        return nuevaCita
    }

    fun limpiarProcesoAgendamiento() {
        especialidadSeleccionada = null
        medicoSeleccionado = null
        fechaSeleccionada = ""
        horaSeleccionada = ""
    }
}