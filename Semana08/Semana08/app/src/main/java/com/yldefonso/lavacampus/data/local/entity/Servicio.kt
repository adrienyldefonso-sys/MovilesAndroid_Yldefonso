package com.yldefonso.lavacampus.data.local.entity
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "servicios")
data class Servicio(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val unidad: String,        // KG, PRENDA, UNIDAD
    val precio: Double,
    val horasEntrega: Int,     // 24, 48, 72
    val activo: Boolean = true,
    val icono: String          // wash, iron, shirt
)
