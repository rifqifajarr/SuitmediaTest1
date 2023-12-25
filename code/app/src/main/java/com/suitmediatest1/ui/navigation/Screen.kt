package com.suitmediatest1.ui.navigation

sealed class Screen (val route: String) {
    object FirstScreen : Screen("first_screen")
    object SecondScreen : Screen("second_screen/{name}") {
        fun createRoute(name: String) = "second_screen/$name"
    }
    object ThirdScreen : Screen("third_screen")
}