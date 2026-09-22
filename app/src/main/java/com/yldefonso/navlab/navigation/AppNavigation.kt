package com.yldefonso.navlab.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.yldefonso.navlab.screens.DetailScreen
import com.yldefonso.navlab.screens.HomeScreen
import com.yldefonso.navlab.screens.ListScreen
import com.yldefonso.navlab.screens.ProfileScreen

@Composable
fun AppNavigation(){
    //Crea y recuerda el controlador de navegacion LUEGO DE LAS RECOMPOSICIONES
    val navController = rememberNavController()

    NavHost(
        navController= navController,
        //Se designa la pantalla donde se comienza
        startDestination= Screen.Home.route
    ){
        //registra HomeScreen como la ruta "home"
        composable(Screen.Home.route){
            HomeScreen(navController)
        }
        //registra ListScreen bajo la ruta "list"
        composable(Screen.List.route){
            ListScreen(navController)
        }
        //registra ProfileScreen como la ruta "profile"
        composable(Screen.Profile.route){
            ProfileScreen(navController)
        }
        //registrDetailScreen pero este recibe un elemento itemId
        composable(
            route=Screen.Detail.route,
            arguments=listOf(
                navArgument(name ="itemId") {
                    type = NavType.IntType //-> El argumento itemId es INT
                    defaultValue=0
                }
            )
        ){backStackEntry->
            //Extrae el itemId,si no existe,usa 0
            val itemId = backStackEntry.arguments?.getInt("itemId")?:0
            DetailScreen(navController,itemId)
        }
    }
}
