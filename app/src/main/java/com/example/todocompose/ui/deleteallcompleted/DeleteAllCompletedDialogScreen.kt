package com.example.todocompose.ui.deleteallcompleted

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DeleteAllCompletedDialogScreen(
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        title = {
            Text(text = "Confirm deletion")
        },
        text = {
            Text(text = "Do you really want to delete all completed tasks?")
        },
        onDismissRequest = { onDismissRequest() },
        confirmButton = { /*TODO*/ })
}