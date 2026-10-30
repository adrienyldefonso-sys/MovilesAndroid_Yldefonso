package com.yldefonso.lavacampus.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.yldefonso.lavacampus.data.local.entity.DetallePedido
import com.yldefonso.lavacampus.model.IngresoServicio
import kotlinx.coroutines.flow.Flow

@Dao
interface DetalleDao {

    @Insert
    suspend fun insertarTodos(lista: List<DetallePedido>)

    @Query("SELECT COUNT(*) FROM detalles_pedido WHERE servicioId = :servicioId")
    suspend fun contarPorServicio(servicioId: Int): Int

    @Query(
        """
        SELECT s.nombre AS nombre, SUM(d.subtotal) AS total
        FROM detalles_pedido d
        INNER JOIN servicios s ON d.servicioId = s.id
        INNER JOIN pedidos p ON d.pedidoId = p.id
        WHERE p.pagado = 1 AND p.fechaRegistro >= :fechaInicio
        GROUP BY s.id
        """
    )
    fun observarIngresosPorServicio(fechaInicio: String): Flow<List<IngresoServicio>>
}