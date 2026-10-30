package com.yldefonso.lavacampus.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val codigo: String,   // código de estudiante; vacío para el administrador
    val correo: String,
    val clave: String,
    val rol: String       // Rol.name: CLIENTE | ADMIN
)