package com.yldefonso.lavacampus.model

import androidx.compose.ui.graphics.Color

enum class EstadoPedido(val etiqueta: String, val color: Color) {
    RECIBIDO("Recibido", Color(0xFFD97706)),
    EN_PROCESO("En proceso", Color(0xFF2563EB)),
    LISTO("Listo", Color(0xFF16A34A)),
    ENTREGADO("Entregado", Color(0xFF475569)),
    CANCELADO("Cancelado", Color(0xFFDC2626));

    /** Siguiente paso de la máquina de estados (null si es final). */
    fun siguiente(): EstadoPedido? = when (this) {
        RECIBIDO -> EN_PROCESO
        EN_PROCESO -> LISTO
        LISTO -> ENTREGADO
        else -> null
    }

    val esActivo: Boolean get() = this == RECIBIDO || this == EN_PROCESO || this == LISTO
}
