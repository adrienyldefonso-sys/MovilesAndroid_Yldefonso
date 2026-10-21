package com.yldefonso.lavacampus.ui.estudiante

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yldefonso.lavacampus.data.local.entity.Usuario
import com.yldefonso.lavacampus.ui.components.AvatarIniciales
import com.yldefonso.lavacampus.ui.components.FilaSwitch
import com.yldefonso.lavacampus.ui.components.formatoCantidad
import com.yldefonso.lavacampus.viewmodel.PerfilViewModel
import com.yldefonso.lavacampus.viewmodel.SesionViewModel

@Composable
fun PerfilScreen(
    vm: PerfilViewModel,
    sesion: SesionViewModel,
    usuario: Usuario,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit,
    barraInferior: @Composable () -> Unit = {}
) {
    val activos by vm.pedidosActivos.collectAsState()
    val totales by vm.pedidosTotales.collectAsState()
    val kilos by vm.kilosLavados.collectAsState()
    val oscuro by sesion.modoOscuro.collectAsState()
    val avisos by sesion.notificaciones.collectAsState()

    Scaffold(bottomBar = barraInferior) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Perfil", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                AvatarIniciales(nombre = usuario.nombre, tamano = 64.dp)
                Column {
                    Text(usuario.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Código ${usuario.codigo}", style = MaterialTheme.typography.bodyMedium)
                    Text(usuario.correo, style = MaterialTheme.typography.bodySmall)
                }
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Estadistica("Pedidos activos", activos.toString(), Modifier.weight(1f))
                Estadistica("Pedidos totales", totales.toString(), Modifier.weight(1f))
                Estadistica("Kilos lavados", formatoCantidad(kilos, "KG"), Modifier.weight(1f))
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
                        titulo = "Avisos",
                        subtitulo = "Cuando tu pedido esté listo",
                        marcado = avisos,
                        onCambio = sesion::cambiarNotificaciones
                    )
                }
            }

            OutlinedButton(onClick = onCambiarPerfil, modifier = Modifier.fillMaxWidth()) {
                Text("Cambiar de perfil")
            }
            OutlinedButton(
                onClick = onCerrarSesion,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Cerrar sesión") }
        }
    }
}

@Composable
private fun Estadistica(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier
    ) {
        Column(
            Modifier.fillMaxWidth().padding(vertical = 14.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                valor,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                titulo,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}