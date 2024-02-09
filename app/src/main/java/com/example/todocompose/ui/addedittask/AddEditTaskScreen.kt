package com.example.todocompose.ui.addedittask

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.todocompose.ui.theme.Grey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    navController: NavController,
    addEditViewModel: AddEditViewModel = hiltViewModel()
    ) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier.shadow(elevation = 2.dp),
                colors = topAppBarColors(
                    containerColor = Grey
                ),
                title = {
                    Text(text = addEditViewModel.topBarTitle)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
            LaunchedEffect(Unit) {
                addEditViewModel.addEditTaskEvent.collect { event ->
                    when(event) {
                        is AddEditViewModel.AddEditTaskEvent.ShowInvalidInputMessage -> {
                            snackbarHostState.showSnackbar(event.msg)
                        }
                        is AddEditViewModel.AddEditTaskEvent.NavigateBackWithResult -> {
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("add_edit_result", event.result)
                            navController.popBackStack()
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    addEditViewModel.onSaveClick()
                }
            ) {
                Icon(imageVector = Icons.Filled.Check, contentDescription = "Add/edit task")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            TextField(
                value = addEditViewModel.taskName,
                onValueChange = { newText ->
                    addEditViewModel.taskName = newText
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Task name")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = addEditViewModel.taskImportance,
                    onCheckedChange = {
                        addEditViewModel.taskImportance = it
                    }
                )
                Text(text = "Important task")
            }
            Text(
                text = if (addEditViewModel.topBarTitle == "Add task") ""
                else "Created: ${addEditViewModel.createdDateFormatted}")
        }
    }
}