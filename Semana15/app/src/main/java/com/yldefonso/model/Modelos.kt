// [E] ENTREGADO: no modificar.
package com.yldefonso.model

// Origen del aviso: de dónde vino (botón, recordatorio o modo avión)
enum class Origen(val texto: String) {
    BOTON("Botón"),
    RECORDATORIO("Recordatorio"),
    MODO_AVION("Modo avión")
}

// Un aviso del historial. "id" distingue cada notificación (se usa como id de notify).
data class Aviso(
    val id: Int,
    val titulo: String,
    val texto: String,
    val origen: Origen,
    val hora: String
)

// Estado del permiso que se muestra en la tarjeta "Permiso de notificaciones".
sealed interface PermisoUi {
    data object SinPedir : PermisoUi
    data object Concedido : PermisoUi
    data object Denegado : PermisoUi
}
