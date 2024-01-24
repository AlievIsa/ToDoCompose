package com.example.todocompose.ui.tasks

import android.content.res.Resources
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todocompose.R
import com.example.todocompose.ui.navigation.Screen
import com.example.todocompose.ui.theme.Grey
import com.example.todocompose.ui.theme.PurpleGrey40
import com.example.todocompose.ui.theme.PurpleGrey80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(navController: NavController) {
    var optionsMenuExpanded by remember {
        mutableStateOf(false)
    }
    var sortedMenuExpanded by remember {
        mutableStateOf(false)
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold (
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                modifier = Modifier.shadow(elevation = 2.dp),
                colors = TopAppBarDefaults.smallTopAppBarColors(
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
                            painter = painterResource(id = R.drawable.baseline_sort_24)
                            , contentDescription = "Sort"
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
                        DropDownOptionsMenu(expanded = optionsMenuExpanded) {
                            optionsMenuExpanded = false
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddEditTask.route) })
            {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "")
            }
        }
    ) { innerPadding ->
        LazyColumn (
            modifier = Modifier.padding(innerPadding)
        ) {

        }
    }
}

@Composable
fun DropDownOptionsMenu(expanded: Boolean, onDismissRequest: () -> Unit ) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        offset = DpOffset(0.dp, (-40).dp
    )) {
        DropdownMenuItem(
            text = {
                Row (verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Hide completed")
                    Checkbox(checked = false, onCheckedChange = {})
                }
            },
            onClick = { /*TODO*/ })
        DropdownMenuItem(
            text = { Text(text = "Delete all completed") },
            onClick = { /*TODO*/ })
    }
}

@Composable
fun DropDownSortedMenu(expanded: Boolean, onDismissRequest: () -> Unit ) {
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