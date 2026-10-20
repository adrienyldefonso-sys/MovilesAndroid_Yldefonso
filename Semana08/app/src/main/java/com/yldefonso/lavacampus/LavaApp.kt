package com.yldefonso.lavacampus

import android.app.Application
import com.yldefonso.lavacampus.data.local.DatosIniciales
import com.yldefonso.lavacampus.data.local.LavaDatabase
import com.yldefonso.lavacampus.data.prefs.PreferenciasStore
import com.yldefonso.lavacampus.data.repository.LavaRepository
import com.yldefonso.lavacampus.data.repository.LavaRepositoryRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LavaApp : Application() {
    val database: LavaDatabase by lazy { LavaDatabase.obtener(this) }
    val repository: LavaRepository by lazy { LavaRepositoryRoom(database) }
    val preferencias: PreferenciasStore by lazy { PreferenciasStore(this) }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            DatosIniciales.sembrar(database)
        }
    }
}