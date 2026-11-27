// [E] con cambio de la mejora 2: se agrega el campo urgente.
package com.yldefonso.model

enum class Origen(val texto: String) {
    BOTON("Botón"),
    RECORDATORIO("Recordatorio"),
    MODO_AVION("Modo avión")
}

data class Aviso(
    val id: Int,
    val titulo: String,
    val texto: String,
    val origen: Origen,
    val hora: String,
    // Mejora 2: si es true, la notificación usa el canal Urgentes (IMPORTANCE_HIGH).
    // Tiene valor por defecto, así el Repository sigue creando avisos igual que antes.
    val urgente: Boolean = false
)

sealed interface PermisoUi {
    data object SinPedir : PermisoUi
    data object Concedido : PermisoUi
    data object Denegado : PermisoUi
}