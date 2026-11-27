// Pantalla rediseñada (commit 4): misma lógica, nueva presentación con tema oscuro y componentes propios.
package com.yldefonso.ui

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.yldefonso.model.PermisoUi
import com.yldefonso.notificacion.Notificador
import com.yldefonso.receiver.ModoAvionReceiver
import com.yldefonso.ui.theme.Cian
import com.yldefonso.ui.theme.Coral
import com.yldefonso.ui.theme.Esmeralda
import com.yldefonso.ui.theme.Fondo
import com.yldefonso.ui.theme.Superficie
import com.yldefonso.ui.theme.Texto
import com.yldefonso.ui.theme.TextoSuave
import com.yldefonso.viewmodel.AvisosViewModel

@Composable
fun PantallaAvisos(vm: AvisosViewModel) {
    val contexto = LocalContext.current
    val permiso by vm.permiso.collectAsState()
    val faltan by vm.faltan.collectAsState()
    val escuchando by vm.escuchandoAvion.collectAsState()
    val avisos by vm.avisos.collectAsState()
    val mensaje by vm.mensaje.collectAsState()

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        vm.permisoRespondido(concedido)
    }

    LaunchedEffect(Unit) {
        if (Notificador.tienePermiso(contexto)) {
            vm.permisoRespondido(true)
        }
    }

    LaunchedEffect(Unit) {
        vm.eventos.collect { aviso ->
            val mostrado = Notificador.mostrar(contexto, aviso)
            if (!mostrado) {
                vm.noSePudoMostrar()
            }
        }
    }

    DisposableEffect(escuchando) {
        if (escuchando) {
            val receptor = ModoAvionReceiver { activo -> vm.alCambiarModoAvion(activo) }
            ContextCompat.registerReceiver(
                contexto,
                receptor,
                IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            onDispose { contexto.unregisterReceiver(receptor) }
        } else {
            onDispose { }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Cabecera()

        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GlassCard(acento = BrilloPrimario) {
                SeccionTitulo(
                    titulo = "Permiso de notificaciones",
                    descripcion = "Android 13 o superior pide tu autorización para mostrar avisos."
                )

                val (textoEstado, colorEstado) = when (permiso) {
                    PermisoUi.SinPedir -> "Sin pedir" to TextoSuave
                    PermisoUi.Concedido -> "Permitido" to Esmeralda
                    PermisoUi.Denegado -> "Denegado" to Coral
                }
                StatusPill(texto = textoEstado, color = colorEstado)

                GradientButton(
                    texto = "Permitir notificaciones",
                    enabled = true,
                    brush = BrilloPrimario,
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            lanzador.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            vm.permisoRespondido(true)
                        }
                    }
                )

                mensaje?.let { mensajeError ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.errorContainer)
                            .padding(14.dp)
                    ) {
                        Text(
                            text = mensajeError,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    SecondaryButton(
                        texto = "Abrir ajustes",
                        enabled = true,
                        onClick = {
                            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                putExtra(Settings.EXTRA_APP_PACKAGE, contexto.packageName)
                            }
                            contexto.startActivity(intent)
                        }
                    )
                }
            }

            GlassCard(acento = BrilloPrimario) {
                SeccionTitulo(
                    titulo = "Avisos",
                    descripcion = "Genera la notificación Tarea lista y la guarda en el historial."
                )
                GradientButton(
                    texto = "Mostrar aviso",
                    enabled = true,
                    brush = BrilloPrimario,
                    onClick = { vm.avisarAhora() }
                )
            }

            GlassCard(acento = BrilloAlerta) {
                SeccionTitulo(
                    titulo = "Recordatorio",
                    descripcion = "Cuenta regresiva de 10 segundos. Llega como urgente."
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    faltan?.let { segundos ->
                        Box(
                            modifier = Modifier.size(64.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // progress va de 1.0 (10 s) a 0.1 (1 s), así el anillo se vacía con el tiempo.
                            CircularProgressIndicator(
                                progress = { segundos / 10f },
                                modifier = Modifier.fillMaxSize(),
                                color = Cian,
                                trackColor = Superficie,
                                strokeWidth = 5.dp
                            )
                            Text(
                                text = "$segundos",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = Texto
                            )
                        }
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        GradientButton(
                            texto = "Recordar en 10 s",
                            enabled = faltan == null,
                            brush = BrilloAlerta,
                            onClick = { vm.recordarEn(10) }
                        )
                        SecondaryButton(
                            texto = "Cancelar",
                            enabled = faltan != null,
                            onClick = { vm.cancelarRecordatorio() }
                        )
                    }
                }
            }

            GlassCard(acento = Brush.horizontalGradient(listOf(Cian, Esmeralda))) {
                SeccionTitulo(
                    titulo = "Receptor de anuncios",
                    descripcion = "Avisa cuando activas o desactivas el modo avión."
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "Escuchar el modo avión",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Texto
                        )
                        Text(
                            text = if (escuchando) "Activo" else "Apagado",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextoSuave
                        )
                    }
                    Switch(
                        checked = escuchando,
                        onCheckedChange = { vm.cambiarEscucha(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Fondo,
                            checkedTrackColor = Cian,
                            uncheckedThumbColor = TextoSuave,
                            uncheckedTrackColor = Superficie
                        )
                    )
                }
            }

            HistorialAvisos(avisos)
        }
    }
}

// Cabecera con degradado: de índigo oscuro al fondo de la app.
@Composable
private fun Cabecera() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(Color(0xFF312E81), Fondo)))
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "AvisosCampus",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Texto
            )
            Text(
                text = "Tus avisos inteligentes del campus",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSuave
            )
        }
    }
}