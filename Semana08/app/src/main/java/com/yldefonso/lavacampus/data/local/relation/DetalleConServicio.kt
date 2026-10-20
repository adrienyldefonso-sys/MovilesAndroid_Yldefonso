package com.yldefonso.lavacampus.data.local.relation

import androidx.room3.Embedded
import androidx.room3.Relation
import com.yldefonso.lavacampus.data.local.entity.DetallePedido
import com.yldefonso.lavacampus.data.local.entity.Servicio

data class DetalleConServicio(
    @Embedded val detalle: DetallePedido,
    @Relation(parentColumns = ["servicioId"], entityColumns = ["id"])
    val servicio: Servicio
)