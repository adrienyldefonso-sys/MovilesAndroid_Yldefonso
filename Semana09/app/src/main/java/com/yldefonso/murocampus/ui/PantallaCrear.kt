package com.yldefonso.murocampus.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yldefonso.murocampus.ui.theme.VerdeFondo
import com.yldefonso.murocampus.ui.theme.VerdeTexto
import com.yldefonso.murocampus.viewmodel.PostsViewModel
import com.yldefonso.murocampus.viewmodel.UiState

@Composable
fun PantallaCrear(vm: PostsViewModel, onVolver: () -> Unit) {
    val estado by vm.crear.collectAsStateWithLifecycle()

    // rememberSaveable: lo escrito sobrevive a la rotación y a un error de red.
    var titulo by rememberSaveable { mutableStateOf("") }
    var texto by rememberSaveable { mutableStateOf("") }

    // Publicar solo se habilita si ambos campos tienen texto real.
    val puedePublicar = titulo.isNotBlank() && texto.isNotBlank()

    BackHandler {
        vm.limpiarCrear()
        onVolver()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = texto,
            onValueChange = { texto = it },
            label = { Text("Contenido") },
            shape = RoundedCornerShape(12.dp),
            minLines = 4,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { vm.publicar(titulo, texto) },
            enabled = puedePublicar && estado !is UiState.Cargando
        ) {
            Text("Publicar", fontWeight = FontWeight.Bold)
        }

        // Estado del envío. null = todavía no se envió nada.
        when (val s = estado) {
            null -> Unit
            is UiState.Cargando -> CircularProgressIndicator()
            is UiState.Error -> Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Error: muestra el mensaje y el formulario conserva lo escrito para reintentar.
                Text(
                    text = s.mensaje,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                )
            }
            is UiState.Exito -> {
                // Éxito: recuadro verde con borde sólido.
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = VerdeFondo),
                    border = BorderStroke(1.dp, VerdeTexto),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Publicado con id ${s.datos.id}",
                        color = VerdeTexto,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    )
                }
                // LaunchedEffect(s): limpia el formulario solo cuando llega el éxito.
                LaunchedEffect(s) {
                    titulo = ""
                    texto = ""
                }
            }
        }
    }
}