package com.yldefonso.clinicasaludplus.ui.components.agendamiento

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yldefonso.clinicasaludplus.model.Especialidad
import com.yldefonso.clinicasaludplus.navigation.Rutas
import com.yldefonso.clinicasaludplus.repository.Repositorio

// Paleta de colores
private val TextoOscuro = Color(0xFF1B2540)
private val TextoGris = Color(0xFF6B7690)
private val FondoPantalla = Color(0xFFF8FAFC)
private val BordeCard = Color(0xFFE2E8F0)
private val AzulPrimario = Color(0xFF2F6BEA)
private val AzulPastel = Color(0xFFEAF1FF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EspecialidadesScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val sedeActiva = Repositorio.sedeSeleccionada
    val especialidadesFiltradas = Repositorio.especialidadesDisponiblesPorSede(sedeActiva?.id, searchQuery)

    Scaffold(
        containerColor = FondoPantalla,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Especialidades",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = TextoOscuro
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = TextoOscuro
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = FondoPantalla
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            // ENCABEZADO INFORMATIVO DE SEDE ACTIVA
            if (sedeActiva != null) {
                Surface(
                    color = AzulPastel,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Sede activa",
                            tint = AzulPrimario,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Mostrando especialidades disponibles en: ${sedeActiva.nombre}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextoOscuro
                        )
                    }
                }
            }

            // BUSCADOR EN CARD BLANCA REDONDEADA
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                placeholder = {
                    Text(
                        text = "Buscar especialidad...",
                        color = TextoGris,
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = TextoGris
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(26.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = BordeCard,
                    focusedBorderColor = AzulPrimario,
                    cursorColor = AzulPrimario
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // LISTA DE CARDS BLANCAS INDEPENDIENTES
            if (especialidadesFiltradas.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay especialidades disponibles para esta búsqueda.",
                        fontSize = 14.sp,
                        color = TextoGris
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {
                    items(especialidadesFiltradas) { especialidad ->
                        CardEspecialidad(
                            especialidad = especialidad,
                            onClick = {
                                Repositorio.especialidadSeleccionada = especialidad
                                navController.navigate(Rutas.Medicos.crearRuta(especialidad.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CardEspecialidad(
    especialidad: Especialidad,
    onClick: () -> Unit
) {
    val estilo = obtenerEstiloEspecialidad(especialidad.nombre)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, BordeCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ÍCONO DENTRO DE CONTENEDOR CIRCULAR PASTEL
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(estilo.colorFondo),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = estilo.icono,
                    contentDescription = especialidad.nombre,
                    tint = estilo.colorIcono,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // TEXTOS
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = especialidad.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextoOscuro
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = especialidad.descripcion,
                    fontSize = 13.sp,
                    color = TextoGris,
                    lineHeight = 17.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // FLECHA INDICADORA (>)
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Ir",
                tint = TextoGris,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

// CONFIGURACIÓN DE ÍCONOS Y COLORES PASTEL
private data class ConfigEstilo(
    val icono: ImageVector,
    val colorIcono: Color,
    val colorFondo: Color
)

private fun obtenerEstiloEspecialidad(nombre: String): ConfigEstilo {
    return when {
        nombre.contains("medicina", ignoreCase = true) -> ConfigEstilo(
            icono = Icons.Default.Person,
            colorIcono = Color(0xFF2F6BEA),
            colorFondo = Color(0xFFEAF1FF)
        )
        nombre.contains("pedia", ignoreCase = true) -> ConfigEstilo(
            icono = Icons.Default.ChildCare,
            colorIcono = Color(0xFFFF8A00),
            colorFondo = Color(0xFFFFF3E0)
        )
        nombre.contains("gineco", ignoreCase = true) -> ConfigEstilo(
            icono = IconoTrompasDeFalopio,
            colorIcono = Color(0xFFD81B60),
            colorFondo = Color(0xFFFCE4EC)
        )
        nombre.contains("cardio", ignoreCase = true) -> ConfigEstilo(
            icono = Icons.Default.Favorite,
            colorIcono = Color(0xFFE53935),
            colorFondo = Color(0xFFFFEBEE)
        )
        nombre.contains("odonto", ignoreCase = true) -> ConfigEstilo(
            icono = Icons.Default.SentimentSatisfied,
            colorIcono = Color(0xFFFFA000),
            colorFondo = Color(0xFFFFF8E1)
        )
        nombre.contains("trauma", ignoreCase = true) -> ConfigEstilo(
            icono = Icons.Default.Accessibility,
            colorIcono = Color(0xFF0288D1),
            colorFondo = Color(0xFFE1F5FE)
        )
        nombre.contains("oftal", ignoreCase = true) -> ConfigEstilo(
            icono = Icons.Default.Visibility,
            colorIcono = Color(0xFF00ACC1),
            colorFondo = Color(0xFFE0F7FA)
        )
        else -> ConfigEstilo(
            icono = Icons.Default.Person,
            colorIcono = Color(0xFF2F6BEA),
            colorFondo = Color(0xFFEAF1FF)
        )
    }
}

// VECTOR PERSONALIZADO CORREGIDO CON curveTo EN LUGAR DE cubicTo
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
