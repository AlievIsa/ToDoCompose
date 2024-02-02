package com.example.todocompose.ui.tasks

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.todocompose.R
import com.example.todocompose.data.Task

@Composable
fun TaskItem(task: Task) {
    val checked by remember {
        mutableStateOf(false)
    }
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopStart),
        verticalAlignment = Alignment.CenterVertically)
    {
        Checkbox(
            checked = task.completed,
            onCheckedChange = {

            })
        Text(
            text = task.name,
            modifier = Modifier
                .padding(end = 8.dp)
                .weight(1f),
            color = Color.Black,
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