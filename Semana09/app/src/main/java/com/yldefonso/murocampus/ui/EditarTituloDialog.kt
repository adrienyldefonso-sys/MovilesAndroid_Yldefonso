package com.yldefonso.murocampus.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yldefonso.murocampus.viewmodel.PostsViewModel
import com.yldefonso.murocampus.viewmodel.UiState

// Diálogo para cambiar el título con PATCH. Muestra la respuesta del servidor al terminar.
@Composable
fun EditarTituloDialog(vm: PostsViewModel, idPost: Int, tituloActual: String, onCerrar: () -> Unit) {
    val estado by vm.editar.collectAsStateWithLifecycle()
    // remember: el texto que escribes se mantiene mientras el diálogo está abierto.
    var texto by remember { mutableStateOf(tituloActual) }

    fun cerrar() {
        vm.limpiarEditar()
        onCerrar()
    }

    AlertDialog(
        onDismissRequest = { cerrar() },
        title = { Text("Editar título") },
        text = {
            Column {
                OutlinedTextField(
                    value = texto,
                    onValueChange = { texto = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                when (val s = estado) {
                    null -> Unit
                    is UiState.Cargando -> CircularProgressIndicator()
                    is UiState.Error -> Text(s.mensaje, color = MaterialTheme.colorScheme.error)
                    // Respuesta del servidor: el título que devolvió el PATCH.
                    is UiState.Exito -> Text(
                        "Respuesta del servidor: ${s.datos.title}",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        confirmButton = {
            // Deshabilitado si está vacío o mientras se envía.
            Button(
                onClick = { vm.editarTitulo(idPost, texto) },
                enabled = texto.isNotBlank() && estado !is UiState.Cargando
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = { cerrar() }) { Text("Cancelar") }
        }
    )
}