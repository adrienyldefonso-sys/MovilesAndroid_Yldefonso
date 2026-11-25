package com.yldefonso.murocampus.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yldefonso.murocampus.ui.theme.*
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
        modifier = Modifier
            .fillMaxSize()
            .padding(Medidas.L),
        verticalArrangement = Arrangement.spacedBy(Medidas.M)
    ) {
        Text("Título", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Medium, color = IndigoProfundo)
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            singleLine = true,
            shape = MuroShapes.small,
            modifier = Modifier.fillMaxWidth()
        )

        Text("Contenido", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Medium, color = IndigoProfundo)
        OutlinedTextField(
            value = texto,
            onValueChange = { texto = it },
            minLines = 4,
            shape = MuroShapes.small,
            modifier = Modifier.fillMaxWidth()
        )

        // Botón principal en ámbar, ancho completo y con forma de píldora.
        Button(
            onClick = { vm.publicar(titulo, texto) },
            enabled = puedePublicar && estado !is UiState.Cargando,
            shape = FormaPildora,
            colors = ButtonDefaults.buttonColors(containerColor = Ambar, contentColor = AmbarOscuro),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Publicar", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
        }

        // Estado del envío. null = todavía no se envió nada.
        when (val s = estado) {
            null -> Unit
            is UiState.Cargando -> CircularProgressIndicator(color = IndigoProfundo)
            is UiState.Error -> Card(
                shape = MuroShapes.small,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Error: muestra el mensaje y el formulario conserva lo escrito.
                Text(
                    text = s.mensaje,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Medidas.L)
                )
            }
            is UiState.Exito -> {
                // Éxito: recuadro verde con borde sólido.
                Card(
                    shape = MuroShapes.small,
                    colors = CardDefaults.cardColors(containerColor = VerdeFondo),
                    border = BorderStroke(1.dp, VerdeTexto),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Publicado con id ${s.datos.id}",
                        color = VerdeTexto,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Medidas.L)
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