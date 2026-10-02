package com.yldefonso.lab04_carrito_yldefonso.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yldefonso.lab04_carrito_yldefonso.Producto
import com.yldefonso.lab04_carrito_yldefonso.ui.components.DrawerContenido
import com.yldefonso.lab04_carrito_yldefonso.ui.components.PanelTotales
import com.yldefonso.lab04_carrito_yldefonso.ui.components.TarjetaProducto
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCarrito() {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    val productos = remember { mutableStateListOf<Producto>() }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var pantallaActual by remember { mutableStateOf("Inicio") }

    val destinos = listOf("Inicio", "Mis pedidos", "Favoritos", "Perfil", "Cerrar sesion")

    val totalFavoritos = productos.count { it.favorito }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContenido(
                destinos = destinos,
                pantallaActual = pantallaActual,
                cantidadFavoritos = totalFavoritos,
                onDestinoSeleccionado = { destino ->
                    pantallaActual = destino
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Mi Carrito TECSUP") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menú",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF6C5CA5),
                        titleContentColor = Color.White
                    )
                )
            }
        ) { padding ->
            if (pantallaActual != "Inicio") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Pantalla \"$pantallaActual\" en construcción",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        TextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre del producto") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TextField(
                                value = precio,
                                onValueChange = { precio = it },
                                label = { Text("Precio (S/)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f)
                            )
                            TextField(
                                value = cantidad,
                                onValueChange = { cantidad = it },
                                label = { Text("Cantidad") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                val precioNum = precio.toDoubleOrNull() ?: 0.0
                                val cantidadNum = cantidad.toIntOrNull() ?: 0
                                if (nombre.isNotBlank() && precioNum > 0 && cantidadNum > 0) {
                                    productos.add(Producto(nombre, precioNum, cantidadNum))
                                    nombre = ""
                                    precio = ""
                                    cantidad = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CA5))
                        ) {
                            Text("AGREGAR")
                        }
                    }

                    if (productos.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Tu carrito está vacío",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "Agrega tu primer producto",
                                    color = Color.Gray
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(productos) { producto ->
                                TarjetaProducto(
                                    producto = producto,
                                    onFavorito = {
                                        val index = productos.indexOf(producto)
                                        if (index != -1) {
                                            productos[index] = producto.copy(favorito = !producto.favorito)
                                        }
                                    }
                                )
                            }
                        }
                    }

                    PanelTotales(productos = productos)
                }
            }
        }
    }
}