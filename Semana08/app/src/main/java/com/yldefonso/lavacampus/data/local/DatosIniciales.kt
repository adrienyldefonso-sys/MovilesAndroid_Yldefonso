package com.yldefonso.lavacampus.data.local

import com.yldefonso.lavacampus.data.local.entity.Servicio

object DatosIniciales {
    /**
     * Base: solo siembra los servicios.
     * TODO (Paso 3): validar con usuarioDao().contar() == 0 y sembrar también
     * usuarios, pedidos y detalles (sección 5 del laboratorio).
     */
    suspend fun sembrar(db: LavaDatabase) {
        val servicioDao = db.servicioDao()
        if (servicioDao.contar() == 0) {
            servicioDao.insertarTodos(
                listOf(
                    Servicio(nombre = "Lavado y secado", descripcion = "Lavado y secado por kilo",
                        unidad = "KG", precio = 8.0, horasEntrega = 24, icono = "wash"),
                    Servicio(nombre = "Planchado", descripcion = "Planchado por prenda",
                        unidad = "PRENDA", precio = 2.5, horasEntrega = 48, icono = "iron"),
                    Servicio(nombre = "Lavado en seco", descripcion = "Para prendas delicadas",
                        unidad = "PRENDA", precio = 12.0, horasEntrega = 48, icono = "shirt"),
                    Servicio(nombre = "Edredón / frazada", descripcion = "Lavado de ropa de cama",
                        unidad = "UNIDAD", precio = 20.0, horasEntrega = 48, activo = false, icono = "wash")
                )
            )
        }
    }
}
