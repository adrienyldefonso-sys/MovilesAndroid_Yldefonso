package com.yldefonso.lavacampus.data.local

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.room3.Database
import com.yldefonso.lavacampus.data.local.dao.ServicioDao
import com.yldefonso.lavacampus.data.local.entity.Servicio

@Database(
    entities = [Servicio::class /* TODO (Paso 3): agrega Usuario, Pedido y DetallePedido */],
    version = 1,
    exportSchema = false
)
abstract class LavaDatabase : RoomDatabase() {
    abstract fun servicioDao(): ServicioDao
    // TODO (Paso 3): usuarioDao(), pedidoDao() y detalleDao()

    companion object {
        @Volatile private var INSTANCE: LavaDatabase? = null

        fun obtener(context: Context): LavaDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                LavaDatabase::class.java,
                "lavacampus.db"
            ).build().also { INSTANCE = it }
        }
    }
}
