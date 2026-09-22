package com.yldefonso.navlab.screens

import android.R.attr.onClick
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yldefonso.navlab.navigation.Screen

@Composable
fun HomeScreen(navController: NavController){
    Column(
        modifier= Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = ("Pantall Tecsup"),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(32.dp))
        //Boton relleno que permitira navegar la lista
        Button(
            onClick = { navController.navigate(Screen.List.route) },//Se tiene que esocoger el navigate de NAVDORECTIONS
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver lista con elementos")
        }
        Spacer(modifier = Modifier.height(12.dp))
        //Aca se añade el boton con borde que navega AL PERFIL
        OutlinedButton(
            onClick = { navController.navigate(Screen.Profile.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mi perfil")
        }
    }
    }