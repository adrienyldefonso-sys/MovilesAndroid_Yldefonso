package com.yldefonso.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yldefonso.lavacampus.model.Rol
import com.yldefonso.lavacampus.ui.components.IconoServicio
import com.yldefonso.lavacampus.ui.components.LavaButton
import com.yldefonso.lavacampus.ui.components.LavaTextField
import com.yldefonso.lavacampus.ui.theme.LavaTheme
import com.yldefonso.lavacampus.viewmodel.SesionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(vm: SesionViewModel, onLoginExitoso: (Rol) -> Unit) {
    val usuario by vm.usuarioActual.collectAsState()
    val error by vm.error.collectAsState()
    val oscuro by vm.modoOscuro.collectAsState()

    var rol by remember { mutableStateOf(Rol.CLIENTE) }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }

    // Éxito: el ViewModel publica el usuario y la pantalla navega
    LaunchedEffect(usuario) {
        usuario?.let { onLoginExitoso(Rol.valueOf(it.rol)) }
    }

    // El color de la pantalla sigue a la pestaña elegida
    LavaTheme(rol = rol, modoOscuro = oscuro) {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .statusBarsPadding()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconoServicio(icono = "wash", tamano = 72.dp, color = MaterialTheme.colorScheme.onPrimary)
                Text(
                    "LavaCampus",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    "Inicia sesión para continuar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Column(
                Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                    listOf(Rol.CLIENTE to "Estudiante", Rol.ADMIN to "Administrador")
                        .forEachIndexed { i, (r, texto) ->
                            SegmentedButton(
                                selected = rol == r,
                                onClick = { rol = r; vm.limpiarError() },
                                shape = SegmentedButtonDefaults.itemShape(index = i, count = 2)
                            ) { Text(texto) }
                        }
                }

                LavaTextField(
                    valor = correo,
                    onValorChange = { correo = it; vm.limpiarError() },
                    etiqueta = "Correo",
                    teclado = KeyboardType.Email
                )
                LavaTextField(
                    valor = clave,
                    onValorChange = { clave = it; vm.limpiarError() },
                    etiqueta = "Contraseña",
                    esClave = true
                )

                error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                }

                LavaButton(texto = "Ingresar", onClick = { vm.login(correo, clave, rol) })

                Text(
                    "Demo: ana@correo.test / 1234 · admin@lavacampus.test / admin",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}