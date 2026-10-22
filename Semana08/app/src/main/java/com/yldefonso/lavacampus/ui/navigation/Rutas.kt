package com.yldefonso.lavacampus.ui.navigation

object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val INICIO = "inicio"
    const val PANEL = "panel"
    const val NUEVO_PEDIDO = "nuevo_pedido"
    const val NUEVO_PEDIDO_RUTA = "nuevo_pedido?repetir={repetir}"
    fun nuevoPedido(repetir: Int) = "nuevo_pedido?repetir=$repetir"
    const val SERVICIOS = "servicios"

    const val DETALLE = "detalle/{pedidoId}"
    fun detalle(pedidoId: Int) = "detalle/$pedidoId"

    const val PERFIL = "perfil"

    const val MIS_PEDIDOS = "mis_pedidos"

    const val BANDEJA = "bandeja"
    const val CLIENTES = "clientes"
    const val AJUSTES = "ajustes"
    const val TARIFAS = "tarifas"

    const val GESTION = "gestion/{pedidoId}"
    fun gestion(pedidoId: Int) = "gestion/$pedidoId"

    const val FORMULARIO = "formulario/{servicioId}"
    fun formulario(servicioId: Int) = "formulario/$servicioId"

}