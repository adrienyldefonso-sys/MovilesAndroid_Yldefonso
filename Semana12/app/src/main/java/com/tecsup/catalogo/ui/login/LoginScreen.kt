package com.tecsup.catalogo.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.catalogo.domain.model.Sesion
import com.tecsup.catalogo.ui.UiState

@Composable
fun LoginScreen(
    factory: ViewModelProvider.Factory,
    onSesionIniciada: (Sesion) -> Unit
) {
    val vm: LoginViewModel = viewModel(factory = factory)
    val estado by vm.estado.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { vm.restaurarSesion() }

    // Cuando el estado trae una sesión, se navega al catálogo.
    LaunchedEffect(estado) {
        val e = estado
        if (e is UiState.Exito && e.datos != null) onSesionIniciada(e.datos)
    }

    LoginContent(
        estado = estado,
        onIngresar = { correo, clave -> vm.iniciarSesion(correo, clave) }
    )
}

@Composable
fun LoginContent(
    estado: UiState<Sesion?>,
    onIngresar: (String, String) -> Unit
) {
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    val cargando = estado is UiState.Cargando

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Catálogo Smartphones", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo") },
                singleLine = true,
                enabled = !cargando,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = clave,
                onValueChange = { clave = it },
                label = { Text("Contraseña") },
                singleLine = true,
                enabled = !cargando,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { onIngresar(correo, clave) }),
                modifier = Modifier.fillMaxWidth()
            )

            if (estado is UiState.Error) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = estado.mensaje,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(20.dp))
            Button(
                onClick = { onIngresar(correo, clave) },
                enabled = !cargando,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (cargando) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                else Text("Ingresar")
            }
        }
    }
}