package com.yldefonso.navlab.model

import com.yldefonso.navlab.ui.theme.BECKER_AVATAR_URL

// Data class que representa la informacion completa de un estudiante en el Portal Academico
data class Student(
    val id: Int,             // Identificador unico del estudiante
    val name: String,        // Nombre completo
    val career: String,      // Carrera profesional
    val avatarUrl: String,   // URL de la imagen de perfil/avatar con rostros reales de randomuser.me
    val studentCode: String, // Codigo de estudiante
    val email: String,       // Correo institucional
    val faculty: String,     // Facultad a la que pertenece
    val bio: String          // Breve biografia o descripcion academica
)

// Repositorio de datos ficticios con rostros reales obtenidos de randomuser.me
object StudentRepository {
    // Lista de 5 estudiantes. El primer alumno es Becker Yldefonso (usuario logueado) y comparte BECKER_AVATAR_URL
    val students: List<Student> = listOf(
        Student(
            id = 1,
            name = "Becker Yldefonso",
            career = "Diseño y Desarrollo de Software",
            avatarUrl = BECKER_AVATAR_URL, // Misma constante usada en el perfil del usuario
            studentCode = "2024-0001",
            email = "adrien.yldefonso@tecsup.edu.pe",
            faculty = "Ingeniería y Tecnología",
            bio = "Estudiante destacado con interés en desarrollo Android."
        ),
        Student(
            id = 2,
            name = "María García",
            career = "Arquitectura",
            avatarUrl = "https://randomuser.me/api/portraits/women/44.jpg",
            studentCode = "2024-0002",
            email = "maria.garcia@example.com",
            faculty = "Diseño y Arquitectura",
            bio = "Apasionada por el diseño sostenible y la innovación urbana."
        ),
        Student(
            id = 3,
            name = "Carlos Perez",
            career = "Medicina",
            avatarUrl = "https://randomuser.me/api/portraits/men/68.jpg",
            studentCode = "2024-0003",
            email = "carlos.perez@example.com",
            faculty = "Ciencias de la Salud",
            bio = "Interesado en investigación médica y pediatría preventiva."
        ),
        Student(
            id = 4,
            name = "Ana Lopez",
            career = "Derecho",
            avatarUrl = "https://randomuser.me/api/portraits/women/28.jpg",
            studentCode = "2024-0004",
            email = "ana.lopez@example.com",
            faculty = "Derecho y Ciencias Políticas",
            bio = "Enfocada en derecho corporativo y derechos humanos internacional."
        ),
        Student(
            id = 5,
            name = "Luis Ramirez",
            career = "Administración",
            avatarUrl = "https://randomuser.me/api/portraits/men/85.jpg",
            studentCode = "2024-0005",
            email = "luis.ramirez@example.com",
            faculty = "Gestión y Negocios",
            bio = "Especializándose en finanzas corporativas y emprendimiento digital."
        )
    )
}
