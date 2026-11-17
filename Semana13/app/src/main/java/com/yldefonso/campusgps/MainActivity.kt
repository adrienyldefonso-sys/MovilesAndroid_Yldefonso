package com.yldefonso.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yldefonso.campusgps.data.LugaresRepositoryLocal
import com.yldefonso.campusgps.data.UbicacionRepositoryGoogle
import com.yldefonso.campusgps.ui.PantallaMapa
import com.yldefonso.campusgps.ui.theme.CampusGPSTheme
import com.yldefonso.campusgps.viewmodel.CampusViewModelFactory
import com.yldefonso.campusgps.viewmodel.MapaViewModel

/**
 * Punto de entrada. Aquí se "ensamblan" las piezas (el único lugar que conoce el Context):
 * Repositories -> Factory -> ViewModel -> Pantalla.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fabrica = CampusViewModelFactory(
            ubicacionRepo = UbicacionRepositoryGoogle(applicationContext),
            lugaresRepo = LugaresRepositoryLocal()
        )

        setContent {
            CampusGPSTheme {
                // viewModel(): al rotar la pantalla devuelve el MISMO ViewModel (no se pierde el estado).
                val vm: MapaViewModel = viewModel(factory = fabrica)
                PantallaMapa(vm)
            }
        }
    }
}
