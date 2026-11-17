package com.yldefonso.campusgps.data

import com.yldefonso.campusgps.model.Lugar

/** Contrato para obtener los lugares del campus. */
interface LugaresRepository {
    fun obtenerLugares(): List<Lugar>
}
