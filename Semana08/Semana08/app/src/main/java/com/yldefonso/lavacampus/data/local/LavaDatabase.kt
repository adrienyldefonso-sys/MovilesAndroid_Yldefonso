package com.yldefonso.lavacampus.data.local

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.yldefonso.lavacampus.data.local.dao.DetalleDao
import com.yldefonso.lavacampus.data.local.dao.PedidoDao
import com.yldefonso.lavacampus.data.local.dao.ServicioDao
import com.yldefonso.lavacampus.data.local.dao.UsuarioDao
import com.yldefonso.lavacampus.data.local.entity.DetallePedido
import com.yldefonso.lavacampus.data.local.entity.Pedido
import com.yldefonso.lavacampus.data.local.entity.Servicio
import com.yldefonso.lavacampus.data.local.entity.Usuario

@Database(
    entities = [Servicio::class, Usuario::class, Pedido::class, DetallePedido::class],
    version = 2,
    exportSchema = false
)
abstract class LavaDatabase : RoomDatabase() {
    abstract fun servicioDao(): ServicioDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun pedidoDao(): PedidoDao
    abstract fun detalleDao(): DetalleDao

    companion object {
        @Volatile private var INSTANCE: LavaDatabase? = null

        /** Una sola instancia para toda la app (singleton). */
        fun obtener(context: Context): LavaDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: construir(context.applicationContext).also { INSTANCE = it }
        }

        private fun construir(context: Context): LavaDatabase =
            Room.databaseBuilder<LavaDatabase>(
                context = context,
                name = context.getDatabasePath("lavacampus.db").absolutePath
            )
                .setDriver(BundledSQLiteDriver())
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
    }
}