package com.example.todocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todocompose.navigation.Screen
import com.example.todocompose.ui.addedittask.AddEditTaskScreen
import com.example.todocompose.ui.tasks.TasksScreen
import com.example.todocompose.ui.theme.ToDoComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Tasks.route
                    ) {
                        composable(Screen.Tasks.route) {
                            TasksScreen(navController)
                        }
                        composable(Screen.AddEditTask.route + "/{taskId}",
                            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
                        ) {
                            AddEditTaskScreen(navController)
                        }
                    }
                }
            }
        }
    }
}