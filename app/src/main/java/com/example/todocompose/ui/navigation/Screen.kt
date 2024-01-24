package com.example.todocompose.ui.navigation

sealed class Screen(val route: String) {
    data object Tasks : Screen("tasks")
    data object AddEditTask : Screen("addEditTask")
}
