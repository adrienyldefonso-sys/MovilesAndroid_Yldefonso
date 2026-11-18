package com.yldefonso.campusgps.data

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.yldefonso.campusgps.model.Posicion
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

/**
 * Implementación con Google Play Services. Es la ÚNICA clase que conoce a
 * FusedLocationProviderClient (por eso vive en el Repository y no en el ViewModel).
 */
class UbicacionRepositoryGoogle(context: Context) : UbicacionRepository {

    // Usamos applicationContext para no retener una Activity (evita fugas de memoria).
    private val appContext = context.applicationContext
    private val cliente = LocationServices.getFusedLocationProviderClient(appContext)

    // Paso 8: pregunta a Google si Play Services está instalado y funcionando en este dispositivo.
    override fun playServicesDisponible(): Boolean =
        GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(appContext) ==
                ConnectionResult.SUCCESS

    // La verificación del permiso la hace la View; si falta, SecurityException se captura abajo.
    @SuppressLint("MissingPermission")
    override suspend fun obtenerPosicion(): Result<Posicion> = try {
        // getCurrentLocation pide UNA ubicación fresca. .await() convierte el Task en corrutina.
        val ubicacion = cliente
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
            .await()
        if (ubicacion != null) {
            Result.success(Posicion(ubicacion.latitude, ubicacion.longitude))
        } else {
            // Devuelve null cuando no puede determinar la ubicación (p. ej. GPS apagado).
            Result.failure(UbicacionNoDisponibleException())
        }
    } catch (e: CancellationException) {
        throw e                       // nunca tragarse la cancelación de una corrutina
    } catch (e: Exception) {
        Result.failure(e)             // SecurityException, ApiException, etc. -> fallo controlado
    }
}