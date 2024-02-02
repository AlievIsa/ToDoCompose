package com.example.todocompose.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todocompose.data.Task
import com.example.todocompose.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskDao: TaskDao
) : ViewModel() {

    val tasks = taskDao.getTasks().asLiveData()

    fun onTaskSwipedToDelete(task: Task) = viewModelScope.launch {
        taskDao.delete(task)
    }
}