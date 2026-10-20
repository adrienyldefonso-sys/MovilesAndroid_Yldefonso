package com.yldefonso.lavacampus.data.local.entity

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey
import com.yldefonso.lavacampus.model.EstadoPedido

@Entity(
    tableName = "pedidos",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("usuarioId"), Index("estado")]
)
data class Pedido(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioId: Int,
    val fechaRegistro: String,                       // aaaa-MM-dd
    val fechaEntrega: String,                        // aaaa-MM-dd
    val estado: String = EstadoPedido.RECIBIDO.name,
    val total: Double,
    val casillero: String? = null,                   // nulo hasta estar LISTO
    val pagado: Boolean = false,
    val nota: String = "",
    val fechaCreacion: Long = System.currentTimeMillis()
)