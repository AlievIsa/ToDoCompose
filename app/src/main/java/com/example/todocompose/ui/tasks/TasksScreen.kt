package com.example.todocompose.ui.tasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.todocompose.R
import com.example.todocompose.navigation.Screen
import com.example.todocompose.ui.theme.Grey
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    navController: NavController,
    tasksViewModel: TasksViewModel = hiltViewModel()
) {
    navController.currentBackStackEntry
        ?.savedStateHandle?.apply {
            get<Int?>("add_edit_result")?.let {
                tasksViewModel.onAddEditResult(it)
            }
            remove<Int?>("add_edit_result")
        }

    var optionsMenuExpanded by remember {
        mutableStateOf(false)
    }
    var sortedMenuExpanded by remember {
        mutableStateOf(false)
    }
    val tasks by tasksViewModel.tasks.observeAsState(emptyList())

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                modifier = Modifier.shadow(elevation = 2.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Grey
                ),
                title = {
                    Text(text = "Tasks")
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    }
                    IconButton(onClick = { sortedMenuExpanded = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_sort_24),
                            contentDescription = "Sort"
                        )
                        DropDownSortedMenu(expanded = sortedMenuExpanded) {
                            sortedMenuExpanded = false
                        }
                    }
                    IconButton(onClick = { optionsMenuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Options"
                        )
                        DropDownOptionsMenu(expanded = optionsMenuExpanded, tasksViewModel) {
                            optionsMenuExpanded = false
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("${Screen.AddEditTask.route}/-1") })
            {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(items = tasks, key = { item -> item.hashCode() } ) { item ->
                Box (modifier = Modifier.clickable {
                    navController.navigate("${Screen.AddEditTask.route}/${item.id}")
                }
                ) {
                    TaskItem(task = item, tasksViewModel::onTaskSwipedToDelete, tasksViewModel::changeCompletedState)
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        tasksViewModel.tasksEvent.collect { event ->
            when(event) {
                is TasksViewModel.TasksEvent.ShowTaskSavedConfirmationMessage -> {
                    snackbarHostState.showSnackbar(message = event.msg, duration = SnackbarDuration.Short)
                }
                is TasksViewModel.TasksEvent.ShowUndoDeleteTaskMessage -> {
                    val result = snackbarHostState.showSnackbar(
                        message = "Task deleted",
                        actionLabel = "UNDO",
                        duration = SnackbarDuration.Short)
                    when(result) {
                        SnackbarResult.ActionPerformed -> {
                            tasksViewModel.onUndoDeleteClick(event.task)
                        }
                        SnackbarResult.Dismissed -> {

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DropDownOptionsMenu(
    expanded: Boolean,
    tasksViewModel: TasksViewModel,
    onDismissRequest: () -> Unit) {
    var hideCompletedState by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        hideCompletedState = tasksViewModel.preferencesFlow
            .first().hideCompleted
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        offset = DpOffset(
            0.dp, (-40).dp
        )
    ) {
        DropdownMenuItem(
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Hide completed")
                    Checkbox(
                        checked = hideCompletedState,
                        onCheckedChange = {
                            hideCompletedState = it
                            tasksViewModel.onHideCompletedClick(it)
                        })
                }
            },
            onClick = {
                hideCompletedState = !hideCompletedState
                tasksViewModel.onHideCompletedClick(hideCompletedState)
            })
        DropdownMenuItem(
            text = { Text(text = "Delete all completed") },
            onClick = { /*TODO*/ })
    }
}

@Composable
fun DropDownSortedMenu(expanded: Boolean, onDismissRequest: () -> Unit) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        offset = DpOffset(0.dp, (-40).dp)
    ) {
        DropdownMenuItem(
            text = { Text(text = "Sort by name") },
            onClick = { /*TODO*/ })
        DropdownMenuItem(
            text = { Text(text = "Sort by date created") },
            onClick = { /*TODO*/ })
    }
}