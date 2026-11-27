// [E] ENTREGADO: no modificar.
package com.yldefonso

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.yldefonso.data.AvisosRepositoryMemoria
import com.yldefonso.ui.PantallaAvisos
import com.yldefonso.ui.theme.AvisosTheme
import com.yldefonso.viewmodel.AvisosViewModel
import com.yldefonso.viewmodel.AvisosViewModelFactory

class MainActivity : ComponentActivity() {

    // El ViewModel (y su repositorio en memoria) sobreviven a la rotación de pantalla.
    private val vm: AvisosViewModel by viewModels {
        AvisosViewModelFactory(AvisosRepositoryMemoria())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContent: la UI se dibuja con Compose; la pantalla recibe solo el ViewModel
        setContent {
            AvisosTheme {
                PantallaAvisos(vm)
            }
        }
    }
}
