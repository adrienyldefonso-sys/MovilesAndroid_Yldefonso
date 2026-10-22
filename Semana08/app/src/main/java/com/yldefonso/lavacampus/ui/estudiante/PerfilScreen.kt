package com.yldefonso.lavacampus.ui.estudiante

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.yldefonso.lavacampus.data.local.entity.Usuario
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

    val primario = MaterialTheme.colorScheme.primary

    Scaffold(bottomBar = barraInferior) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ---- Título ----
            Text("Perfil", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

            // ---- Tarjeta del Usuario ----
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                color = primario.copy(alpha = 0.15f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                            tint = primario,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = usuario.nombre,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = usuario.correo,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = primario.copy(alpha = 0.15f),
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Text(
                                text = "Estudiante",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = primario,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // ---- 3 Estadísticas ----
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Estadistica("Pedidos activos", activos.toString(), Modifier.weight(1f))
                Estadistica("Pedidos totales", totales.toString(), Modifier.weight(1f))
                Estadistica("Lavados", "${formatoCantidad(kilos, "KG")} kg", Modifier.weight(1f))
            }

            // ---- Banner: Cambiar de perfil ----
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = primario),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onCambiarPerfil)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.SwapHoriz,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Cambiar de perfil",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "Modo práctica: ir a vista Administrador",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                        )
                    }
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            // ---- Preferencias ----
            Text("Preferencias", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    FilaSwitch(
                        titulo = "Modo oscuro",
                        subtitulo = "Cambia el tema de la app",
                        marcado = oscuro,
                        onCambio = sesion::cambiarModoOscuro
                    )
                    FilaSwitch(
                        titulo = "Avisos de pedido listo",
                        subtitulo = "Notificar cuando puedas recoger",
                        marcado = avisos,
                        onCambio = sesion::cambiarNotificaciones
                    )
                }
            }

            // ---- Pie de página DataStore ----
            Text(
                text = buildAnnotatedString {
                    append("Se guardan en el dispositivo ")
                    withStyle(SpanStyle(color = primario, fontWeight = FontWeight.Bold)) {
                        append("con DataStore")
                    }
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.padding(start = 4.dp)
            )

            Spacer(Modifier.height(4.dp))

            // ---- Botón Cerrar sesión ----
            OutlinedButton(
                onClick = onCerrarSesion,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun Estadistica(titulo: String, valor: String, modifier: Modifier = Modifier) {
    val colorFondo = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
    val colorTexto = MaterialTheme.colorScheme.primary

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorFondo),
        modifier = modifier
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = valor,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = colorTexto
            )
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                color = colorTexto
            )
        }
    }
}