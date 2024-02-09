package com.example.todocompose.ui.tasks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.todocompose.R
import com.example.todocompose.data.Task
import kotlinx.coroutines.Job

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    task: Task,
    onItemDeleted: (task: Task) -> Job,
    onItemChecked: (task: Task) -> Job
) {
    val dismissState = rememberDismissState(
        confirmValueChange = {
            if (it == DismissValue.DismissedToStart) {
                onItemDeleted(task)
                true
            } else
                false
        }
    )
    LaunchedEffect(task) {
        dismissState.reset()
    }
    SwipeToDismiss(
        state = dismissState,
        background = {
            Box (modifier = Modifier.fillMaxSize()){
                if (dismissState.dismissDirection == DismissDirection.EndToStart) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 16.dp)
                    )
                }
            }
        },
        dismissContent = {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopStart),
                verticalAlignment = Alignment.CenterVertically)
            {
                Checkbox(
                    checked = task.completed,
                    onCheckedChange = {
                        onItemChecked(task)
                    })
                Text(
                    text = task.name,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1f),
                    color = Color.Black,
                    textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
                if (task.important) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_priority_high_24),
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }
    )
}