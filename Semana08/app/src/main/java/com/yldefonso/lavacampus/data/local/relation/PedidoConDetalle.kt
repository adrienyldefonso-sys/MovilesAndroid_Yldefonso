package com.yldefonso.lavacampus.data.local.relation

import androidx.room3.Embedded
import androidx.room3.Relation
import com.yldefonso.lavacampus.data.local.entity.DetallePedido
import com.yldefonso.lavacampus.data.local.entity.Pedido
import com.yldefonso.lavacampus.data.local.entity.Usuario

data class PedidoConDetalle(
    @Embedded val pedido: Pedido,

    @Relation(parentColumns = ["usuarioId"], entityColumns = ["id"])
    val usuario: Usuario,

    // Relación uno a muchos anidada: cada detalle trae su Servicio
    @Relation(entity = DetallePedido::class, parentColumns = ["id"], entityColumns = ["pedidoId"])
    val detalles: List<DetalleConServicio>
)