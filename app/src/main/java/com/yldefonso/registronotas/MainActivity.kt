package com.yldefonso.registronotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yldefonso.registronotas.ui.theme.RegistroNotasTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistroNotasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegistroNotasScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

data class Curso(val nombre: String, val peso: Float)
val cursos = listOf(
    Curso("Fundamentos de Programación",0.20f),
    Curso("Programación Orientada a Objetos",0.25f),
    Curso("Programación en Móviles",0.30f),
    Curso("Base de Datos", 0.25f)
)

@Composable
fun RegistroNotasScreen(modifier: Modifier = Modifier) {
    var nota1 by remember { mutableStateOf(0f) }
    var nota2 by remember { mutableStateOf(0f) }
    var nota3 by remember { mutableStateOf(0f) }
    var nota4 by remember { mutableStateOf(0f) }
    var redondear by remember { mutableStateOf(false) }
    var confirmado by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Registro de Notas",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        FilaCurso(cursos[0], nota1) { nota1 = it }
        Spacer(modifier = Modifier.height(8.dp))
        FilaCurso(cursos[1], nota2) { nota2 = it }
        Spacer(modifier = Modifier.height(8.dp))
        FilaCurso(cursos[2], nota3) { nota3 = it }
        Spacer(modifier = Modifier.height(8.dp))
        FilaCurso(cursos[3], nota4) { nota4 = it }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Redondear promedio finak")
            Switch(checked = redondear, onCheckedChange = { redondear = it })
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(checked = confirmado, onCheckedChange = { confirmado = it })
            Text("COnfirmo que las notas son correctas")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Asigna las notas y confirma para contunuar",
            color = Color.Gray
        )
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Desarrollado por: (Becker Yldefonso)",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

    }
}
@Composable
fun FilaCurso(curso: Curso,nota:Float,onNotaChange: (Float) -> Unit){
    Card(modifier = Modifier.fillMaxWidth()){
        Column (modifier= Modifier.padding(12.dp)){
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                Text (
                    text = "${curso.nombre} (${(curso.peso * 100).toInt()}%)",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text="Nota: ${nota.toInt()}",
                    fontWeight = FontWeight.Bold,
                    color= MaterialTheme.colorScheme.primary
                )
            }
            Slider(
                value =nota,
                onValueChange=onNotaChange,
                valueRange = 0f..20f,
                steps = 19
            )
        }

    }
}