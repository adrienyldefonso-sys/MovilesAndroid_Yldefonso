package com.yldefonso.lavacampus.ui.administrador

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Autorenew
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yldefonso.lavacampus.model.EstadoPedido
import com.yldefonso.lavacampus.ui.components.CampanaNotificaciones
import com.yldefonso.lavacampus.ui.components.fechaPanel
import com.yldefonso.lavacampus.ui.components.graficos.GraficoBarras
import com.yldefonso.lavacampus.ui.components.graficos.GraficoDona
import com.yldefonso.lavacampus.ui.components.soles
import com.yldefonso.lavacampus.viewmodel.NotificacionesViewModel
import com.yldefonso.lavacampus.viewmodel.PanelViewModel

@Composable
fun PanelScreen(
    vm: PanelViewModel,
    notificaciones: NotificacionesViewModel,
    onAbrirPedido: (Int) -> Unit,
    onVerListos: () -> Unit,
    barraInferior: @Composable () -> Unit = {}
) {
    val hoy by vm.pedidosHoy.collectAsState()
    val enProceso by vm.enProceso.collectAsState()
    val porRecoger by vm.porRecoger.collectAsState()
    val atrasados by vm.atrasados.collectAsState()
    val ingresos by vm.ingresosSemana.collectAsState()
    val porEstado by vm.porEstado.collectAsState()
    val porServicio by vm.porServicio.collectAsState()

    val totalPedidos = porEstado.sumOf { it.valor.toInt() }

    Scaffold(bottomBar = barraInferior) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // ---- 1. Encabezado ----
            EncabezadoPanel(
                fecha = fechaPanel(),
                notificaciones = notificaciones,
                onAbrirPedido = onAbrirPedido
            )

            // ---- 2. Tres contadores ----
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TarjetaContador(
                    titulo = "Pedidos hoy",
                    valor = hoy,
                    color = MaterialTheme.colorScheme.primary,
                    icono = Icons.AutoMirrored.Outlined.ReceiptLong,
                    modifier = Modifier.weight(1f)
                )
                TarjetaContador(
                    titulo = "En proceso",
                    valor = enProceso,
                    color = EstadoPedido.EN_PROCESO.color,
                    icono = Icons.Outlined.Autorenew,
                    modifier = Modifier.weight(1f)
                )
                TarjetaContador(
                    titulo = "Por recoger",
                    valor = porRecoger,
                    color = EstadoPedido.LISTO.color,
                    icono = Icons.Outlined.ShoppingBag,
                    modifier = Modifier.weight(1f)
                )
            }

            // ---- 3. Tarjeta Hero de Ingresos de la semana ----
            TarjetaIngresosHero(
                monto = ingresos,
                totalPedidos = totalPedidos
            )

            // ---- 4. Sección "Pedidos por estado" ----
            SeccionTarjeta(
                titulo = "Pedidos por estado",
                icono = Icons.Outlined.PieChart
            ) {
                GraficoDona(datos = porEstado)
            }

            // ---- 5. Sección "Ingresos por servicio (S/)" ----
            SeccionTarjeta(
                titulo = "Ingresos por servicio (S/)",
                icono = Icons.Outlined.BarChart
            ) {
                if (porServicio.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Aún no hay pedidos cobrados esta semana.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    GraficoBarras(datos = porServicio)
                }
            }

            // ---- 6. Alerta: DEBAJO del gráfico de barras ----
            AnimatedVisibility(
                visible = porRecoger > 0,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                TarjetaAlertaRecojo(
                    porRecoger = porRecoger,
                    onVerListos = onVerListos
                )
            }

            // ---- 7. Tarjeta de pedidos atrasados ----
            AnimatedVisibility(
                visible = atrasados > 0,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                TarjetaAlertaAtrasados(atrasados = atrasados)
            }
        }
    }
}

@Composable
private fun EncabezadoPanel(
    fecha: String,
    notificaciones: NotificacionesViewModel,
    onAbrirPedido: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "Panel de la lavandería",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Hoy · $fecha",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        CampanaNotificaciones(vm = notificaciones, onAbrirPedido = onAbrirPedido)
    }
}

@Composable
private fun TarjetaContador(
    titulo: String,
    valor: Int,
    color: Color,
    icono: ImageVector,
    modifier: Modifier = Modifier
) {
    val animValor by animateFloatAsState(
        targetValue = valor.toFloat(),
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "animContador"
    )

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.13f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = color.copy(alpha = 0.25f)
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(color.copy(alpha = 0.22f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
            }

            Text(
                text = animValor.toInt().toString(),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )

            Text(
                text = titulo,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun TarjetaIngresosHero(
    monto: Double,
    totalPedidos: Int
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            primaryColor,
            primaryColor.copy(
                red = (primaryColor.red * 0.8f).coerceIn(0f, 1f),
                blue = (primaryColor.blue * 1.15f).coerceIn(0f, 1f)
            ),
            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.92f)
        )
    )

    Card(
        shape = RoundedCornerShape(26.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradientBrush)
                .clip(RoundedCornerShape(26.dp))
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                drawCircle(
                    color = Color.White.copy(alpha = 0.09f),
                    radius = canvasHeight * 0.95f,
                    center = Offset(canvasWidth * 0.95f, canvasHeight * 0.05f)
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.05f),
                    radius = canvasHeight * 1.4f,
                    center = Offset(canvasWidth * 0.82f, canvasHeight * 0.25f)
                )
            }

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.TrendingUp,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Text(
                            text = "Ingresos de la semana",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color.White.copy(alpha = 0.22f)
                    ) {
                        Text(
                            text = "$totalPedidos pedidos",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                Text(
                    text = soles(monto),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    letterSpacing = (-0.5).sp
                )
            }
        }
    }
}

@Composable
private fun SeccionTarjeta(
    titulo: String,
    icono: ImageVector,
    contenido: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            ),
            border = androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(Modifier.padding(18.dp)) {
                contenido()
            }
        }
    }
}

@Composable
private fun TarjetaAlertaRecojo(
    porRecoger: Int,
    onVerListos: () -> Unit
) {
    val verde = EstadoPedido.LISTO.color

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = verde.copy(alpha = 0.12f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = verde.copy(alpha = 0.32f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(verde.copy(alpha = 0.22f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.CreditCard,
                    contentDescription = null,
                    tint = verde,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (porRecoger == 1) "1 pedido espera recojo"
                    else "$porRecoger pedidos esperan recojo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "avisa a los estudiantes",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(
                onClick = onVerListos,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = verde,
                    contentColor = Color.White
                )
            ) {
                Text("Ver", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun TarjetaAlertaAtrasados(atrasados: Int) {
    val rojo = MaterialTheme.colorScheme.error

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = rojo.copy(alpha = 0.12f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = rojo.copy(alpha = 0.32f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(rojo.copy(alpha = 0.22f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = null,
                    tint = rojo,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (atrasados == 1) "1 pedido atrasado" else "$atrasados pedidos atrasados",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "requiere atención inmediata",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
