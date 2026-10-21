package com.yldefonso.lavacampus.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.yldefonso.lavacampus.data.local.entity.Usuario
import com.yldefonso.lavacampus.model.ClienteResumen
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuarios WHERE correo = :correo AND clave = :clave LIMIT 1")
    suspend fun login(correo: String, clave: String): Usuario?

    @Insert
    suspend fun insertarTodos(lista: List<Usuario>)

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int

    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun obtener(id: Int): Usuario?

    @Query(
        """
        SELECT u.id AS id,
               u.nombre AS nombre,
               u.codigo AS codigo,
               COUNT(p.id) AS cantidadPedidos,
               COALESCE(SUM(CASE WHEN p.pagado = 1 THEN p.total ELSE 0 END), 0.0) AS totalGastado
        FROM usuarios u
        LEFT JOIN pedidos p ON p.usuarioId = u.id AND p.estado != 'CANCELADO'
        WHERE u.rol = 'CLIENTE'
        GROUP BY u.id
        ORDER BY u.nombre
        """
    )
    fun observarClientes(): Flow<List<ClienteResumen>>

    @Query("SELECT * FROM usuarios WHERE rol = :rol ORDER BY id LIMIT 1")
    suspend fun obtenerPorRol(rol: String): Usuario?

}