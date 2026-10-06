package com.yldefonso.clinicasaludplus.repository

import com.yldefonso.clinicasaludplus.model.Usuario

object Repositorio {

    val usuarios = mutableListOf<Usuario>(
        Usuario(
            id = 1,
            nombre = "Juan Pérez",
            telefono = "987654321",
            correo = "juan@correo.com",
            contrasena = "123456"
        )
    )

    var usuarioActual: Usuario? = usuarios.firstOrNull()

    fun registrarUsuario(usuario: Usuario): Boolean {
        val existeCorreo = usuarios.any { it.correo.equals(usuario.correo.trim(), ignoreCase = true) }
        if (existeCorreo) {
            return false
        }
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
        } else {
            false
        }
    }

    fun cerrarSesion() {
        usuarioActual = null
    }
}