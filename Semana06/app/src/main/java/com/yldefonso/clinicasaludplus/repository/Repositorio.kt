package com.yldefonso.clinicasaludplus.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.yldefonso.clinicasaludplus.model.*
import java.time.DayOfWeek
import java.time.LocalDate

// Modelo de Notificación
data class NotificacionItem(
    val id: Int,
    val titulo: String,
    val mensaje: String,
    val fecha: String,
    val estado: String = "Pendiente"
)

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
        // No se inicia sesión automáticamente
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
    val sedes = listOf(
        Sede(
            id = 1,
            nombre = "Sede San Juan de Lurigancho",
            direccion = "Av. Próceres de la Independencia 1480",
            distrito = "San Juan de Lurigancho",
            telefono = "(01) 619-7000",
            horario = "Lun - Sáb: 07:00 am - 08:00 pm",
            especialidadesIds = listOf(1, 2, 5)
        ),
        Sede(
            id = 2,
            nombre = "Sede Independencia",
            direccion = "Av. Carlos Izaguirre 150",
            distrito = "Independencia",
            telefono = "(01) 619-7001",
            horario = "Lun - Sáb: 07:00 am - 08:00 pm",
            especialidadesIds = listOf(1, 2, 6)
        ),
        Sede(
            id = 3,
            nombre = "Sede Miraflores",
            direccion = "Av. José Pardo 620",
            distrito = "Miraflores",
            telefono = "(01) 619-7002",
            horario = "Atención 24 Horas",
            especialidadesIds = listOf(1, 2, 3, 4, 5, 6, 7)
        ),
        Sede(
            id = 4,
            nombre = "Sede Los Olivos",
            direccion = "Av. Antúnez de Mayolo 1245",
            distrito = "Los Olivos",
            telefono = "(01) 619-7003",
            horario = "Lun - Sáb: 07:00 am - 09:00 pm",
            especialidadesIds = listOf(1, 3, 4)
        )
    )

    val especialidades = listOf(
        Especialidad(1, "Medicina General", "Atención primaria e integral para toda la familia."),
        Especialidad(2, "Pediatría", "Cuidado médico especializado para niños y adolescentes."),
        Especialidad(3, "Ginecología", "Atención médica integral para la salud de la mujer."),
        Especialidad(4, "Cardiología", "Diagnóstico y tratamiento de enfermedades del corazón."),
        Especialidad(5, "Odontología", "Salud bucal, prevención y tratamiento dental."),
        Especialidad(6, "Traumatología", "Evaluación y cuidado del sistema osteomuscular."),
        Especialidad(7, "Oftalmología", "Cuidado integral y tratamiento de la visión.")
    )

    val medicos = listOf(
        Medico(
            1,
            "Dr. Carlos Mendoza",
            1,
            "CMP 45892",
            "https://images.unsplash.com/photo-1622253692010-333f2da6031d?w=300&auto=format&fit=crop&q=80",
            listOf("08:00", "09:00", "10:30", "11:30", "14:00", "15:00", "16:30", "17:30"),
            listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)
        ),
        Medico(
            2,
            "Dra. Ana Torres",
            1,
            "CMP 38210",
            "https://images.unsplash.com/photo-1614608682850-e0d6ed316d47?w=300&auto=format&fit=crop&q=80",
            listOf("08:30", "09:30", "11:00", "13:00", "14:30", "15:30", "17:00", "18:00"),
            listOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
        ),
        Medico(
            3,
            "Dr. Roberto Gómez",
            2,
            "CMP 51204",
            "https://images.unsplash.com/photo-1537368910025-700350fe46c7?w=300&auto=format&fit=crop&q=80",
            listOf("09:00", "10:00", "11:30", "14:00", "15:30", "16:30", "17:30"),
            listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
        ),
        Medico(
            4,
            "Dra. Elena Ramos",
            3,
            "CMP 29481",
            "https://images.unsplash.com/photo-1559839734-2b71ea197ec2?w=300&auto=format&fit=crop&q=80",
            listOf("08:00", "09:30", "11:00", "14:00", "15:00", "16:30", "18:00"),
            listOf(DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)
        ),
        Medico(
            5,
            "Dr. Luis Paredes",
            4,
            "CMP 60312",
            "https://images.unsplash.com/photo-1612349317150-e413f6a5b16d?w=300&auto=format&fit=crop&q=80",
            listOf("08:30", "10:00", "11:30", "13:00", "15:00", "16:00", "17:30"),
            listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)
        )
    )

    // === ESTADO Y AGENDAMIENTO ===
    var sedeSeleccionada: Sede? by mutableStateOf(null)
    var especialidadSeleccionada: Especialidad? by mutableStateOf(null)
    var medicoSeleccionado: Medico? by mutableStateOf(null)
    var citaSeleccionada: Cita? by mutableStateOf(null)
    var fechaSeleccionada: String by mutableStateOf("")
    var fechaSeleccionadaIso: String by mutableStateOf("")
    var fechaSeleccionadaTexto: String by mutableStateOf("")
    var horaSeleccionada: String by mutableStateOf("")

    val citasReservadas = mutableStateListOf<Cita>()

    // === CONTADOR DE NOTIFICACIONES NO LEÍDAS ===
    var notificacionesNoLeidas by mutableStateOf(0)

    val notificaciones = mutableStateListOf(
        NotificacionItem(
            id = 1,
            titulo = "Bienvenido a Salud Plus",
            mensaje = "Gracias por registrarte. Aquí podrás gestionar todas tus citas médicas.",
            fecha = "Hace 2 días",
            estado = "Informativo"
        )
    )

    fun limpiarNotificacionesNoLeidas() {
        notificacionesNoLeidas = 0
    }

    // === OPERACIONES DE COLECCIONES ===
    fun especialidadesDestacadas(): List<Especialidad> {
        return especialidades.take(3)
    }

    fun esDiaDisponible(medicoId: Int, fecha: LocalDate): Boolean {
        if (fecha.isBefore(LocalDate.now())) return false
        val medico = medicos.find { it.id == medicoId } ?: return false
        return fecha.dayOfWeek in medico.diasAtencion
    }

    fun buscarSedes(query: String): List<Sede> {
        if (query.isBlank()) return sedes
        return sedes.filter {
            it.nombre.contains(query, ignoreCase = true) ||
            it.distrito.contains(query, ignoreCase = true) ||
            it.direccion.contains(query, ignoreCase = true)
        }
    }

    fun especialidadesDisponiblesPorSede(sedeId: Int?, query: String = ""): List<Especialidad> {
        val disponibles = if (sedeId == null) {
            especialidades
        } else {
            val sede = sedes.find { it.id == sedeId }
            if (sede == null || sede.especialidadesIds.isEmpty()) {
                especialidades
            } else {
                especialidades.filter { it.id in sede.especialidadesIds }
            }
        }

        if (query.isBlank()) return disponibles
        return disponibles.filter { it.nombre.contains(query, ignoreCase = true) }
    }

    fun buscarEspecialidades(query: String): List<Especialidad> {
        return especialidadesDisponiblesPorSede(sedeSeleccionada?.id, query)
    }

    fun buscarMedicos(especialidadId: Int, query: String = ""): List<Medico> {
        val porEsp = medicos.filter { it.especialidadId == especialidadId }
        val filtrados = if (query.isBlank()) porEsp else porEsp.filter { it.nombre.contains(query, ignoreCase = true) }
        return filtrados.sortedByDescending { it.nombre }
    }

    fun buscarTodosLosMedicos(query: String = ""): List<Medico> {
        if (query.isBlank()) return medicos
        return medicos.filter { med ->
            val espNombre = especialidades.find { it.id == med.especialidadId }?.nombre ?: ""
            med.nombre.contains(query, ignoreCase = true) ||
            med.cmp.contains(query, ignoreCase = true) ||
            espNombre.contains(query, ignoreCase = true)
        }
    }

    fun horariosDisponibles(medicoId: Int, fechaIso: String): List<String> {
        val medico = medicos.find { it.id == medicoId } ?: return emptyList()
        val horasOcupadas = citasReservadas
            .filter { it.medico.id == medicoId && (it.fecha == fechaIso || it.fecha == fechaSeleccionadaTexto || it.fecha == fechaSeleccionada) }
            .map { it.hora }

        return medico.disponibilidad.filter { it !in horasOcupadas }
    }

    fun agendarCitaActual(): Cita? {
        val user = usuarioActual ?: return null
        val esp = especialidadSeleccionada ?: especialidades.find { it.id == medicoSeleccionado?.especialidadId } ?: return null
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

        val nuevaNotificacion = NotificacionItem(
            id = notificaciones.size + 1,
            titulo = "Cita Asignada - ${esp.nombre}",
            mensaje = "Tu cita con ${med.nombre} ha sido asignada para el $fechaFinal a las $horaSeleccionada. Estado: Pendiente.",
            fecha = "Hace un momento",
            estado = "Pendiente"
        )
        notificaciones.add(0, nuevaNotificacion)
        notificacionesNoLeidas++

        return nuevaCita
    }

    fun citasDelUsuario(): List<Cita> {
        return citasReservadas.filter { it.usuario.id == usuarioActual?.id }
    }

    fun cancelarCita(citaId: Int): Boolean {
        return citasReservadas.removeIf { it.id == citaId }
    }

    fun limpiarProcesoAgendamiento() {
        sedeSeleccionada = null
        especialidadSeleccionada = null
        medicoSeleccionado = null
        citaSeleccionada = null
        fechaSeleccionada = ""
        fechaSeleccionadaIso = ""
        fechaSeleccionadaTexto = ""
        horaSeleccionada = ""
    }
}