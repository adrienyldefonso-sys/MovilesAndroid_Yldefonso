package com.yldefonso.clinicasaludplus.ui.components.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yldefonso.clinicasaludplus.navigation.Rutas
import com.yldefonso.clinicasaludplus.repository.Repositorio

// ============================================================
// PALETA DE COLORES Y ESTILOS
// ============================================================

private val AzulPrimario = Color(0xFF2F6BEA)
private val TextoOscuro = Color(0xFF18284A)
private val TextoGris = Color(0xFF6F7B91)
private val BordeCardBlanco = Color(0xFFEBF0F9)

private val FondoAgendar = Color(0xFFE4EEFF)
private val IconoAgendar = Color(0xFF2F6BEA)

private val FondoCitas = Color(0xFFE4F5EC)
private val IconoCitas = Color(0xFF20A66A)

private val FondoPerfil = Color(0xFFF0E5FF)
private val IconoPerfil = Color(0xFF8B50D9)

private val FondoResultados = Color(0xFFFFEDE0)
private val IconoResultados = Color(0xFFF18432)

// ============================================================
// HOME SCREEN
// ============================================================

@Composable
fun HomeScreen(navController: NavController) {

    val usuario = Repositorio.usuarioActual
    val destacadas = Repositorio.especialidadesDestacadas()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = 20.dp,
                vertical = 16.dp
            )
    ) {

        // ENCABEZADO DE BIENVENIDA CON CAMPANA Y BADGE DE NOTIFICACIONES
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "¡Hola, ${usuario?.nombre?.split(" ")?.firstOrNull() ?: "Becker"}!",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoOscuro
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "¿Qué deseas hacer hoy?",
                    fontSize = 18.sp,
                    color = TextoGris
                )
            }

            IconButton(
                onClick = {
                    Repositorio.limpiarNotificacionesNoLeidas()
                    navController.navigate(Rutas.Notificaciones.ruta)
                },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF1F5F9))
            ) {
                BadgedBox(
                    badge = {
                        if (Repositorio.notificacionesNoLeidas > 0) {
                            Badge(
                                containerColor = Color(0xFFE53935),
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = Repositorio.notificacionesNoLeidas.toString(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notificaciones",
                        tint = TextoOscuro,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // PRIMERA FILA DE ACCESOS RÁPIDOS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            TarjetaAccesoRapido(
                modifier = Modifier.weight(1f),
                titulo = "Agendar cita",
                icono = Icons.Default.CalendarMonth,
                fondoColor = FondoAgendar,
                iconoColor = IconoAgendar,
                onClick = { navController.navigate(Rutas.Especialidades.ruta) }
            )

            TarjetaAccesoRapido(
                modifier = Modifier.weight(1f),
                titulo = "Mis citas",
                icono = Icons.Default.EventNote,
                fondoColor = FondoCitas,
                iconoColor = IconoCitas,
                onClick = { navController.navigate(Rutas.MisCitas.ruta) }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // SEGUNDA FILA DE ACCESOS RÁPIDOS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            TarjetaAccesoRapido(
                modifier = Modifier.weight(1f),
                titulo = "Mi perfil",
                icono = Icons.Default.Person,
                fondoColor = FondoPerfil,
                iconoColor = IconoPerfil,
                onClick = { navController.navigate(Rutas.Perfil.ruta) }
            )

            TarjetaAccesoRapido(
                modifier = Modifier.weight(1f),
                titulo = "Resultados",
                icono = Icons.Default.Assignment,
                fondoColor = FondoResultados,
                iconoColor = IconoResultados,
                onClick = { navController.navigate(Rutas.Resultados.ruta) }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // SECCIÓN ESPECIALIDADES DESTACADAS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Especialidades destacadas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextoOscuro
            )

            Text(
                text = "Ver todas",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = AzulPrimario,
                modifier = Modifier.clickable {
                    navController.navigate(Rutas.Especialidades.ruta)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // CARDS DE ESPECIALIDADES DESTACADAS (CORREGIDA ALINEACIÓN DE ÍCONOS)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(destacadas) { esp ->
                val estilo = obtenerEstiloEspecialidad(esp.nombre)

                Card(
                    modifier = Modifier
                        .width(128.dp)
                        .height(165.dp)
                        .clickable {
                            Repositorio.especialidadSeleccionada = esp
                            navController.navigate(Rutas.Medicos.crearRuta(esp.id))
                        },
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    border = BorderStroke(1.dp, BordeCardBlanco),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp, vertical = 14.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(4.dp))

                        // ÍCONO FIJO EN LA PARTE SUPERIOR DE LA TARJETA
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(estilo.colorFondo),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = estilo.icono,
                                contentDescription = esp.nombre,
                                tint = estilo.colorIcono,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // CONTENEDOR FLEXIBLE PARA EL TEXTO CENTRADO
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = esp.nombre,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextoOscuro,
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

// ============================================================
// BOX DE ACCESO RÁPIDO
// ============================================================

@Composable
private fun TarjetaAccesoRapido(
    modifier: Modifier = Modifier,
    titulo: String,
    icono: ImageVector,
    fondoColor: Color,
    iconoColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = fondoColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icono,
                contentDescription = titulo,
                tint = iconoColor,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = titulo,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextoOscuro,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ============================================================
// MAPEADO DE ESTILOS POR ESPECIALIDAD
// ============================================================

private data class EstiloEspecialidad(
    val icono: ImageVector,
    val colorIcono: Color,
    val colorFondo: Color
)

private fun obtenerEstiloEspecialidad(nombre: String): EstiloEspecialidad {
    return when {
        nombre.contains("medicina", ignoreCase = true) -> EstiloEspecialidad(
            icono = Icons.Default.Person,
            colorIcono = Color(0xFF2F6BEA),
            colorFondo = Color(0xFFEAF1FF)
        )
        nombre.contains("pedi", ignoreCase = true) -> EstiloEspecialidad(
            icono = Icons.Default.ChildCare,
            colorIcono = Color(0xFFFF8A00),
            colorFondo = Color(0xFFFFF3E0)
        )
        nombre.contains("cardio", ignoreCase = true) -> EstiloEspecialidad(
            icono = Icons.Default.Favorite,
            colorIcono = Color(0xFFE53935),
            colorFondo = Color(0xFFFFEBEE)
        )
        nombre.contains("gine", ignoreCase = true) -> EstiloEspecialidad(
            icono = IconoTrompasDeFalopio,
            colorIcono = Color(0xFFD81B60),
            colorFondo = Color(0xFFFCE4EC)
        )
        nombre.contains("oftal", ignoreCase = true) -> EstiloEspecialidad(
            icono = Icons.Default.Visibility,
            colorIcono = Color(0xFF00ACC1),
            colorFondo = Color(0xFFE0F7FA)
        )
        else -> EstiloEspecialidad(
            icono = Icons.Default.Person,
            colorIcono = Color(0xFF2F6BEA),
            colorFondo = Color(0xFFEAF1FF)
        )
    }
}

// VECTOR PERSONALIZADO: TROMPAS DE FALOPIO / ÚTERO (GINECOLOGÍA)
private var _iconoTrompasDeFalopio: ImageVector? = null
private val IconoTrompasDeFalopio: ImageVector
    get() {
        if (_iconoTrompasDeFalopio != null) {
            return _iconoTrompasDeFalopio!!
        }
        _iconoTrompasDeFalopio = ImageVector.Builder(
            name = "IconoTrompasDeFalopio",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
            pathFillType = PathFillType.NonZero
        ) {
            // Ovario Izquierdo
            moveTo(3.5f, 10.5f)
            curveTo(2.67f, 10.5f, 2f, 9.83f, 2f, 9f)
            curveTo(2f, 8.17f, 2.67f, 7.5f, 3.5f, 7.5f)
            curveTo(4.33f, 7.5f, 5f, 8.17f, 5f, 9f)
            curveTo(5f, 9.83f, 4.33f, 10.5f, 3.5f, 10.5f)
            close()

            // Ovario Derecho
            moveTo(20.5f, 10.5f)
            curveTo(19.67f, 10.5f, 19f, 9.83f, 19f, 9f)
            curveTo(19f, 8.17f, 19.67f, 7.5f, 20.5f, 7.5f)
            curveTo(21.33f, 7.5f, 22f, 8.17f, 22f, 9f)
            curveTo(22f, 9.83f, 21.33f, 10.5f, 20.5f, 10.5f)
            close()

            // Cuerpo del Útero y Trompas de Falopio
            moveTo(12f, 19.5f)
            curveTo(10.5f, 19.5f, 9f, 16.5f, 8.2f, 13f)
            curveTo(7f, 13.5f, 5.5f, 13.8f, 4.5f, 13.2f)
            curveTo(3.7f, 12.7f, 3.5f, 11.7f, 4f, 11f)
            curveTo(4.5f, 10.3f, 5.5f, 10.1f, 6.2f, 10.5f)
            curveTo(7f, 10.9f, 8f, 10.6f, 9f, 10.1f)
            curveTo(9.5f, 8.9f, 10.3f, 8f, 12f, 8f)
            curveTo(13.7f, 8f, 14.5f, 8.9f, 15f, 10.1f)
            curveTo(16f, 10.6f, 17f, 10.9f, 17.8f, 10.5f)
            curveTo(18.5f, 10.1f, 19.5f, 10.3f, 20f, 11f)
            curveTo(20.5f, 11.7f, 20.3f, 12.7f, 19.5f, 13.2f)
            curveTo(18.5f, 13.8f, 17f, 13.5f, 15.8f, 13f)
            curveTo(15f, 16.5f, 13.5f, 19.5f, 12f, 19.5f)
            close()
        }.build()
        return _iconoTrompasDeFalopio!!
    }