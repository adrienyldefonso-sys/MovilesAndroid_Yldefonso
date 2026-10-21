package com.yldefonso.lavacampus.ui.administrador

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yldefonso.lavacampus.data.local.entity.Usuario
import com.yldefonso.lavacampus.ui.components.AvatarIniciales
import com.yldefonso.lavacampus.ui.components.FilaSwitch
import com.yldefonso.lavacampus.viewmodel.SesionViewModel

@Composable
fun AjustesScreen(
    sesion: SesionViewModel,
    usuario: Usuario,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit,
    barraInferior: @Composable () -> Unit = {}
) {
    val oscuro by sesion.modoOscuro.collectAsState()
    val notificaciones by sesion.notificaciones.collectAsState()
    val recibir by sesion.recibirPedidos.collectAsState()

    Scaffold(bottomBar = barraInferior) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Ajustes", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                AvatarIniciales(nombre = usuario.nombre, tamano = 64.dp)
                Column {
                    Text(usuario.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(usuario.correo, style = MaterialTheme.typography.bodyMedium)
                    Text("Administrador", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                }
            }

            OutlinedButton(onClick = onCambiarPerfil, modifier = Modifier.fillMaxWidth()) {
                Text("Cambiar de perfil")
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    FilaSwitch(
                        titulo = "Modo oscuro",
                        subtitulo = "Se recuerda al cerrar la app",
                        marcado = oscuro,
                        onCambio = sesion::cambiarModoOscuro
                    )
                    FilaSwitch(
                        titulo = "Notificaciones",
                        subtitulo = "Avisos de nuevos pedidos",
                        marcado = notificaciones,
                        onCambio = sesion::cambiarNotificaciones
                    )
                    FilaSwitch(
                        titulo = "Recibir pedidos",
                        subtitulo = "Si lo apagas, los estudiantes no podrán crear pedidos",
                        marcado = recibir,
                        onCambio = sesion::cambiarRecibirPedidos
                    )
                }
            }

            OutlinedButton(
                onClick = onCerrarSesion,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Cerrar sesión") }
        }
    }
}