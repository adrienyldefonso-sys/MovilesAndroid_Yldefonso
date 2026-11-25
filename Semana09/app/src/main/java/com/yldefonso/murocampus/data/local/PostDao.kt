package com.yldefonso.murocampus.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// DAO: las consultas a la base local.
@Dao
interface PostDao {

    // REPLACE: si el id ya existe, reemplaza la fila en lugar de fallar.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(posts: List<PostEntity>)

    // Una página de la copia local, en el mismo orden y con la misma paginación que la red.
    @Query("SELECT * FROM posts ORDER BY id LIMIT :limite OFFSET :inicio")
    suspend fun paginaDesde(inicio: Int, limite: Int): List<PostEntity>
}