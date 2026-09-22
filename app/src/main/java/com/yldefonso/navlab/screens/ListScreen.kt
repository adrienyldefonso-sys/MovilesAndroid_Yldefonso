package com.yldefonso.navlab.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.ui.Modifier
import com.yldefonso.navlab.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class) //Para definir el TopAppbar
@Composable
fun ListScreen(navController: NavController) {
    val items = (1..8).map { "Elemento numero $it" } //datos de la pantalla de ejemplo (8)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        // LazyColumn dibuja los items visibles en la pantalla
        LazyColumn(contentPadding = padding) {
            items(items.size) { index ->
                ListItem(
                    headlineContent = { Text(items[index]) },
                    supportingContent = { Text("Toca para ver el detalle") },
                    modifier = Modifier.clickable {
                        // Navega al detalle pasando el id mediante createRoute
                        navController.navigate(
                            Screen.Detail.createRoute(index + 1)
                        )
                    }
                )
                HorizontalDivider()
            }
        }
    }
}