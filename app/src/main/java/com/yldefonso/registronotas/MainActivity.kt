package com.yldefonso.registronotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yldefonso.registronotas.ui.theme.RegistroNotasTheme
import kotlin.math.roundToInt
val MoradoPrincipal = Color(0xFF6A4C93)
val MoradoClaroFondo = Color(0xFFF3EEFB)
val MoradoBadge = Color(0xFFD8CDEE)
val GrisTextoSecundario = Color(0xFF6B6B6B)
val VerdeClaroBg = Color(0xFFE3F3E6)
val VerdeTexto = Color(0xFF2E7D32)
val AmbarClaroBg = Color(0xFFFFF1DA)
val AmbarTexto = Color(0xFFB25E00)
val RojoClaroBg = Color(0xFFFCE4E4)
val RojoTexto = Color(0xFFC62828)
val VerdeOscuroBg = Color(0xFFDCEEE0)
val VerdeOscuroTexto = Color(0xFF1B5E20)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistroNotasTheme {
                RegistroNotasScreen()
            }
        }
    }
}

data class Curso(val nombre: String, val peso: Float)

val cursos = listOf(
    Curso("Fundamentos de Programación", 0.20f),
    Curso("Programación Orientada a Objetos", 0.25f),
    Curso("Programación en Móviles", 0.30f),
    Curso("Base de Datos", 0.25f)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroNotasScreen() {
    var nota1 by remember { mutableStateOf(0f) }
    var nota2 by remember { mutableStateOf(0f) }
    var nota3 by remember { mutableStateOf(0f) }
    var nota4 by remember { mutableStateOf(0f) }
    var redondear by remember { mutableStateOf(false) }
    var confirmado by remember { mutableStateOf(false) }
    var mostrarResultado by remember { mutableStateOf(false) }
    var promedioPonderado by remember { mutableStateOf(0.0) }
    var promedioFinal by remember { mutableStateOf(0.0) }
    var observacion by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Registro de Notas",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MoradoPrincipal,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(MoradoClaroFondo, Color.White)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Notas del ciclo",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF2B2140)
                )
                Text(
                    text = "Desliza para asignar cada nota (0 a 20)",
                    fontSize = 13.sp,
                    color = GrisTextoSecundario
                )

                Spacer(modifier = Modifier.height(16.dp))
                FilaCurso(cursos[0], nota1) { nota1 = it }
                Spacer(modifier = Modifier.height(14.dp))
                FilaCurso(cursos[1], nota2) { nota2 = it }
                Spacer(modifier = Modifier.height(14.dp))
                FilaCurso(cursos[2], nota3) { nota3 = it }
                Spacer(modifier = Modifier.height(14.dp))
                FilaCurso(cursos[3], nota4) { nota4 = it }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Redondear promedio final", color = Color(0xFF2B2140))
                    Switch(
                        checked = redondear,
                        onCheckedChange = { redondear = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MoradoPrincipal
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = confirmado,
                        onCheckedChange = { confirmado = it },
                        colors = CheckboxDefaults.colors(checkedColor = MoradoPrincipal)
                    )
                    Text("Confirmo que las notas son correctas", color = Color(0xFF2B2140))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val ponderado =
                            nota1 * 0.20 + nota2 * 0.25 + nota3 * 0.30 + nota4 * 0.25
                        promedioPonderado = ponderado

                        promedioFinal = if (redondear) {
                            ponderado.roundToInt().toDouble()
                        } else {
                            ponderado
                        }

                        observacion = when {
                            promedioFinal >= 17.0 -> "EXCELENTE"
                            promedioFinal >= 13.0 -> "APROBADO"
                            promedioFinal >= 10.0 -> "EN RECUPERACIÓN"
                            else -> "DESAPROBADO"
                        }

                        mostrarResultado = true
                    },
                    enabled = confirmado,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MoradoPrincipal,
                        disabledContainerColor = Color(0xFFCFC7DD)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("CALCULAR PROMEDIO", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))
                if (mostrarResultado) {
                    TarjetaResultado(
                        promedioPonderado = promedioPonderado,
                        promedioFinal = promedioFinal,
                        redondear = redondear,
                        observacion = observacion
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Promedio calculado correctamente",
                        color = VerdeTexto,
                        fontSize = 13.sp
                    )
                } else {
                    Text(
                        text = "Asigna las notas y confirma para calcular",
                        color = GrisTextoSecundario,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Desarrollado por: Becker Yldefonso",
                    fontSize = 12.sp,
                    color = GrisTextoSecundario,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun FilaCurso(curso: Curso, nota: Float, onNotaChange: (Float) -> Unit) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.weight(1f)) {
                Text(
                    text = curso.nombre,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2B2140)
                )
                Text(
                    text = "  (${(curso.peso * 100).toInt()}%)",
                    color = GrisTextoSecundario
                )
            }
            Text(
                text = "${nota.toInt()}",
                fontWeight = FontWeight.Bold,
                color = MoradoBadge.let { MoradoPrincipal } // texto en morado
            )
        }
        Slider(
            value = nota,
            onValueChange = onNotaChange,
            valueRange = 0f..20f,
            steps = 19,
            colors = SliderDefaults.colors(
                thumbColor = MoradoPrincipal,
                activeTrackColor = MoradoPrincipal,
                inactiveTrackColor = Color(0xFFE0D6F0)
            )
        )
    }
}

@Composable
fun TarjetaResultado(
    promedioPonderado: Double,
    promedioFinal: Double,
    redondear: Boolean,
    observacion: String
) {
    val (bgChip, textoChip) = when (observacion) {
        "EXCELENTE" -> VerdeOscuroBg to VerdeOscuroTexto
        "APROBADO" -> VerdeClaroBg to VerdeTexto
        "EN RECUPERACIÓN" -> AmbarClaroBg to AmbarTexto
        else -> RojoClaroBg to RojoTexto
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE0D6F0), RoundedCornerShape(12.dp))
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Promedio ponderado: ${"%.2f".format(promedioPonderado)}",
            color = Color(0xFF2B2140)
        )
        Text(
            text = "Promedio final: ${
                if (redondear) "%.0f".format(promedioFinal) else "%.2f".format(promedioFinal)
            }",
            fontWeight = FontWeight.Bold,
            color = MoradoPrincipal
        )
        if (redondear) {
            Text(
                text = "(redondeado)",
                fontSize = 12.sp,
                color = GrisTextoSecundario
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .background(bgChip, RoundedCornerShape(20.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Text(
                text = observacion,
                color = textoChip,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
    }
}