package com.yldefonso.lavacampus.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yldefonso.lavacampus.model.Rol
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Declarada UNA sola vez, a nivel de archivo
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ajustes")

class PreferenciasStore(private val context: Context) {

    private object Claves {
        val PERFIL_ACTIVO = stringPreferencesKey("perfilActivo")      // CLIENTE | ADMIN | vacío
        val USUARIO_ID = intPreferencesKey("usuarioId")
        val MODO_OSCURO = booleanPreferencesKey("modoOscuro")
        val NOTIFICACIONES = booleanPreferencesKey("notificaciones")
        val RECIBIR_PEDIDOS = booleanPreferencesKey("recibirPedidos")
    }

    // ---- Lectura: un Flow por cada preferencia ----
    val perfilActivo: Flow<String> =
        context.dataStore.data.map { it[Claves.PERFIL_ACTIVO] ?: "" }

    val usuarioId: Flow<Int> =
        context.dataStore.data.map { it[Claves.USUARIO_ID] ?: -1 }

    val modoOscuro: Flow<Boolean> =
        context.dataStore.data.map { it[Claves.MODO_OSCURO] ?: false }

    val notificaciones: Flow<Boolean> =
        context.dataStore.data.map { it[Claves.NOTIFICACIONES] ?: true }

    val recibirPedidos: Flow<Boolean> =
        context.dataStore.data.map { it[Claves.RECIBIR_PEDIDOS] ?: true }

    // ---- Escritura: funciones suspend ----
    suspend fun guardarSesion(rol: Rol, usuarioId: Int) {
        context.dataStore.edit {
            it[Claves.PERFIL_ACTIVO] = rol.name
            it[Claves.USUARIO_ID] = usuarioId
        }
    }

    suspend fun guardarModoOscuro(valor: Boolean) {
        context.dataStore.edit { it[Claves.MODO_OSCURO] = valor }
    }

    suspend fun guardarNotificaciones(valor: Boolean) {
        context.dataStore.edit { it[Claves.NOTIFICACIONES] = valor }
    }

    suspend fun guardarRecibirPedidos(valor: Boolean) {
        context.dataStore.edit { it[Claves.RECIBIR_PEDIDOS] = valor }
    }

    /** Borra el perfil y el usuario; conserva modo oscuro y demás ajustes. */
    suspend fun cerrarSesion() {
        context.dataStore.edit {
            it.remove(Claves.PERFIL_ACTIVO)
            it.remove(Claves.USUARIO_ID)
        }
    }
}