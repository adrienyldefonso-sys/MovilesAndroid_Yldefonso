package com.yldefonso.murocampus.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Base de datos de la app. version = 1: si cambias la tabla, sube el número.
@Database(entities = [PostEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao

    companion object {
        // Crea la base una sola vez y guarda el archivo "murocampus.db" en el teléfono.
        fun crear(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "murocampus.db").build()
    }
}