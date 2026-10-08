package com.yldefonso.clinicasaludplus.repository

import androidx.compose.runtime.mutableStateListOf
import com.yldefonso.clinicasaludplus.model.*

object Repositorio {

    // === AUTENTICACIÓN ===
    val usuarios = mutableListOf(
        Usuario(1, "Becker Yldefonso", "987654321", "becker@correo.com", "123456")
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
        Medico(1, "Dr. Carlos Mendoza", 1, "CMP 45892", "https://images.unsplash.com/photo-1622253692010-333f2da6031d?w=300&auto=format&fit=crop&q=80", listOf("08:00 AM", "09:00 AM", "10:30 AM", "11:30 AM", "02:00 PM", "03:00 PM", "04:30 PM", "05:30 PM")),
        Medico(2, "Dra. Ana Torres", 1, "CMP 38210", "https://images.unsplash.com/photo-1614608682850-e0d6ed316d47?w=300&auto=format&fit=crop&q=80", listOf("08:30 AM", "09:30 AM", "11:00 AM", "01:00 PM", "02:30 PM", "03:30 PM", "05:00 PM", "06:00 PM")),
        Medico(3, "Dr. Roberto Gómez", 2, "CMP 51204", "https://images.unsplash.com/photo-1537368910025-700350fe46c7?w=300&auto=format&fit=crop&q=80", listOf("09:00 AM", "10:00 AM", "11:30 AM", "02:00 PM", "03:30 PM", "04:30 PM", "05:30 PM")),
        Medico(4, "Dra. Elena Ramos", 3, "CMP 29481", "https://images.unsplash.com/photo-1559839734-2b71ea197ec2?w=300&auto=format&fit=crop&q=80", listOf("08:00 AM", "09:30 AM", "11:00 AM", "02:00 PM", "03:00 PM", "04:30 PM", "06:00 PM")),
        Medico(5, "Dr. Luis Paredes", 4, "CMP 60312", "https://images.unsplash.com/photo-1612349317150-e413f6a5b16d?w=300&auto=format&fit=crop&q=80", listOf("08:30 AM", "10:00 AM", "11:30 AM", "01:00 PM", "03:00 PM", "04:00 PM", "05:30 PM"))
    )

    // === ESTADO Y AGENDAMIENTO ===
    var especialidadSeleccionada: Especialidad? = null
    var medicoSeleccionado: Medico? = null
    var fechaSeleccionada: String = ""
    var fechaSeleccionadaIso: String = ""
    var fechaSeleccionadaTexto: String = ""
    var horaSeleccionada: String = ""

    val citasReservadas = mutableStateListOf<Cita>()

    // === OPERACIONES DE COLECCIONES (RÚBRICA) ===
    fun especialidadesDestacadas(): List<Especialidad> {
        return especialidades.take(3)
    }

    fun buscarEspecialidades(query: String): List<Especialidad> {
        if (query.isBlank()) return especialidades
        return especialidades.filter { it.nombre.contains(query, ignoreCase = true) }
    }

    fun buscarMedicos(especialidadId: Int, query: String = ""): List<Medico> {
        val porEsp = medicos.filter { it.especialidadId == especialidadId }
        val filtrados = if (query.isBlank()) porEsp else porEsp.filter { it.nombre.contains(query, ignoreCase = true) }
        return filtrados.sortedByDescending { it.nombre }
    }

    // Filtra horarios ya reservados para ese médico y fecha exacta (ISO o texto)
    fun horariosDisponibles(medicoId: Int, fechaIso: String): List<String> {
        val medico = medicos.find { it.id == medicoId } ?: return emptyList()
        val horasOcupadas = citasReservadas
            .filter { it.medico.id == medicoId && (it.fecha == fechaIso || (fechaSeleccionadaIso == fechaIso && (it.fecha == fechaSeleccionadaTexto || it.fecha == fechaSeleccionada))) }
            .map { it.hora }

        return medico.disponibilidad.filter { it !in horasOcupadas }
    }

    fun agendarCitaActual(): Cita? {
        val user = usuarioActual ?: return null
        val esp = especialidadSeleccionada ?: return null
        val med = medicoSeleccionado ?: return null
        val fechaFinal = when {
            fechaSeleccionadaTexto.isNotBlank() -> fechaSeleccionadaTexto
            fechaSeleccionada.isNotBlank() -> fechaSeleccionada
            else -> fechaSeleccionadaIso
        }
        if (fechaFinal.isBlank() || horaSeleccionada.isBlank()) return null

        val nuevoId = citasReservadas.size + 1
        val nuevaCita = Cita(
            id = nuevoId,
            codigoReserva = "CIT-${1000 + nuevoId}",
            usuario = user,
            especialidad = esp,
            medico = med,
            fecha = fechaFinal,
            hora = horaSeleccionada
        )
        citasReservadas.add(nuevaCita)
        return nuevaCita
    }

    fun citasDelUsuario(): List<Cita> {
        return citasReservadas.filter { it.usuario.id == usuarioActual?.id }
    }

    fun cancelarCita(citaId: Int): Boolean {
        return citasReservadas.removeIf { it.id == citaId }
    }

    fun limpiarProcesoAgendamiento() {
        especialidadSeleccionada = null
        medicoSeleccionado = null
        fechaSeleccionada = ""
        fechaSeleccionadaIso = ""
        fechaSeleccionadaTexto = ""
        horaSeleccionada = ""
    }
}