package com.yldefonso.lavacampus.ui.navigation

object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val INICIO = "inicio"
    const val PANEL = "panel"
    const val NUEVO_PEDIDO = "nuevo_pedido"
    const val SERVICIOS = "servicios"

    const val DETALLE = "detalle/{pedidoId}"
    fun detalle(pedidoId: Int) = "detalle/$pedidoId"

    const val PERFIL = "perfil"

    const val MIS_PEDIDOS = "mis_pedidos"
    const val TARIFAS = "tarifas"
    // TODO (Pasos 8 al 13): NUEVO_PEDIDO, DETALLE, MIS_PEDIDOS, TARIFAS, PERFIL,
    //                       BANDEJA, GESTION, FORMULARIO_SERVICIO, CLIENTES, AJUSTES
}