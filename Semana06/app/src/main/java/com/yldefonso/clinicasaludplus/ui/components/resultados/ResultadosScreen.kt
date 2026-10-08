package com.yldefonso.clinicasaludplus.ui.components.resultados

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Paleta de colores unificada
private val AzulPrimario = Color(0xFF2F6BEA)
private val TextoOscuro = Color(0xFF1B2540)
private val TextoGris = Color(0xFF6B7690)
private val FondoPantalla = Color(0xFFF8FAFC)
private val BordeCard = Color(0xFFE2E8F0)
private val FondoIcono = Color(0xFFEAF1FF)
private val VerdeFondo = Color(0xFFF0FDF4)
private val VerdeTexto = Color(0xFF166534)
private val AzulPastelFondo = Color(0xFFEAF1FF)

data class ResultadoMedico(
    val id: Int,
    val examen: String,
    val fecha: String,
    val estado: String,
    val tipo: String = "Análisis Clínico"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultadosScreen(navController: NavController) {
    val listaResultados = listOf(
        ResultadoMedico(1, "Hemograma Completo", "10/10/2026", "Completado", "Laboratorio"),
        ResultadoMedico(2, "Perfil Lipídico", "05/10/2026", "Completado", "Laboratorio"),
        ResultadoMedico(3, "Rayos X de Tórax", "28/09/2026", "Entregado", "Radiología")
    )

    Scaffold(
        containerColor = FondoPantalla,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Resultados Médicos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = TextoOscuro
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = FondoPantalla
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
        ) {
            items(listaResultados) { res ->
                CardResultadoItem(resultado = res)
            }
        }
    }
}

@Composable
private fun CardResultadoItem(resultado: ResultadoMedico) {
    val esCompletado = resultado.estado.equals("Completado", ignoreCase = true)
    val colorFondoBadge = if (esCompletado) VerdeFondo else AzulPastelFondo
    val colorTextoBadge = if (esCompletado) VerdeTexto else AzulPrimario

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BordeCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 1. CABECERA: TIPO DE EXAMEN + INSIGNIA DE ESTADO
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = resultado.tipo,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextoGris
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorFondoBadge)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = resultado.estado,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorTextoBadge
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2. CUERPO: ÍCONO + NOMBRE DEL EXAMEN + FLECHA NAVEGACIÓN
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(FondoIcono),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Assignment,
                        contentDescription = resultado.examen,
                        tint = AzulPrimario,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = resultado.examen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextoOscuro,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Ver resultado",
                    tint = TextoGris,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(thickness = 1.dp, color = BordeCard)
            Spacer(modifier = Modifier.height(12.dp))

            // 3. PIE: FECHA DE EMISIÓN E INDICADOR VISUAL DE DESCARGA PDF
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF1F5F9))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Fecha",
                        tint = AzulPrimario,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Emisión: ${resultado.fecha}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextoOscuro
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FileDownload,
                        contentDescription = "Descargar PDF",
                        tint = AzulPrimario,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "PDF",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulPrimario
                    )
                }
            }
        }
    }
}