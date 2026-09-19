package com.yldefonso.lab04_carrito_yldefonso

import android.R.attr.title
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yldefonso.lab04_carrito_yldefonso.ui.theme.Lab04carritoyldefonsoTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab04carritoyldefonsoTheme {
                PantallaCarrito()
                }
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCarrito(){
    var nombre by remember { mutableStateOf("") }
    var precio by remember {mutableStateOf("")}
    var cantidad by remember {mutableStateOf("")}
    // -- lista observable de los productos agregados
    // gracias al mutableStateListOf(),se establece recomposicion
    val productos = remember {
        mutableStateListOf<Producto>() }

    Scaffold(
        topBar= {
            TopAppBar(
                title = {Text("Mi carrito TECSUP")},
                colors= TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6C5CA5),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier= Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ){
            TextField(
                value=nombre,
                onValueChange = {nombre=it},
                label={Text ("Nombre del producto:")}
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value=precio,
                    onValueChange = {precio=it},
                    label= {Text("Precio (S/):")}
                )
                TextField(
                    value=cantidad,
                    onValueChange = {cantidad=it},
                    label= {Text("Cantidad: ")}
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick={
                    val precioNum=precio.toDoubleOrNull()?:0.0
                    val cantidadNum=cantidad.toIntOrNull()?:0
                    if (nombre.isNotBlank()&&precioNum>0 && cantidadNum>0){
                        productos.add(Producto(nombre,precioNum,cantidadNum))
                        nombre=""
                        precio=""
                        cantidad=""
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors= ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CA5))
            ){
                Text("AGREGAR")}
            Spacer (modifier = Modifier.height(16.dp))
            Text("Productos: ${productos.size}")
            }
    }

}
