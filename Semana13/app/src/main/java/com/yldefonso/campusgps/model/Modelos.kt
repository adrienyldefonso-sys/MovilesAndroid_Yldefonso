package com.yldefonso.campusgps.model

/** Una coordenada geográfica (la posición del usuario). */
data class Posicion(val latitud: Double, val longitud: Double)

/** Un lugar ficticio del campus (datos de la sección 5 del laboratorio). */
data class Lugar(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val latitud: Double,
    val longitud: Double
)

// metros = null mientras todavía no se conoce la ubicación del usuario.
// Es un tipo "para mostrar": junta el Lugar con su distancia ya calculada.
data class LugarConDistancia(val lugar: Lugar, val metros: Float?)

/**
 * Estados posibles de la pantalla. La View solo DIBUJA según cuál sea el estado actual.
 * "sealed" obliga a tratar todos los casos en un `when` (el compilador avisa si falta uno).
 */
sealed interface UbicacionUi {
    data object Inicial : UbicacionUi                          // aún no se pulsó el botón
    data object Buscando : UbicacionUi                         // esperando respuesta del GPS
    data class Lista(val posicion: Posicion) : UbicacionUi     // ubicación obtenida con éxito
    data class Error(val mensaje: String) : UbicacionUi        // algo salió mal (Paso 8 añadirá el tipo)
}
