package com.suitmediatest1

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.suitmediatest1.ui.navigation.Screen
import com.suitmediatest1.ui.screen.FirstScreen
import com.suitmediatest1.ui.screen.ScreenViewModel
import com.suitmediatest1.ui.screen.SecondScreen
import com.suitmediatest1.ui.screen.ThirdScreen

@Composable
fun SuitmediaTestApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val viewModel: ScreenViewModel by lazy { ScreenViewModel() }

    NavHost(
        navController = navController,
        startDestination = Screen.FirstScreen.route)
    {
        composable(Screen.FirstScreen.route) {
            FirstScreen(
                navController = navController,
                navigateToSecondScreen = { name ->
                    navController.navigate(Screen.SecondScreen.createRoute(name))
                }
            )
        }
        composable(
            route = Screen.SecondScreen.route,
            arguments = listOf(
                navArgument("name") {type = NavType.StringType}
            )
        ) {
            val name = it.arguments?.getString("name") ?: ""
            SecondScreen(navController = navController, name = name, viewModel = viewModel)
        }
        composable(Screen.ThirdScreen.route) {
            ThirdScreen(navController = navController, viewModel = viewModel)
        }
    }
}