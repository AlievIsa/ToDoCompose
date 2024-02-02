package com.example.todocompose.ui.addedittask

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todocompose.data.Task
import com.example.todocompose.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val state: SavedStateHandle
) : ViewModel() {

    private val taskId: Int = checkNotNull(state["taskId"])
    private val _task = MutableStateFlow<Task?>(null)
    private val task: StateFlow<Task?> = _task
    var taskName by mutableStateOf("")
    var taskImportance by mutableStateOf(false)
    var createdDateFormatted by mutableStateOf("")
    var topBarTitle by mutableStateOf("Add task")

    init {
        viewModelScope.launch {
            if (taskId != -1) {
                taskDao.getTaskById(taskId).collect { task ->
                    _task.value = task
                    taskName = task.name
                    taskImportance = task.important
                    createdDateFormatted = task.createdDateFormatted
                    topBarTitle = "Edit task"
                }
            }
        }
    }

    fun onSaveClick() {
        if (taskName.isBlank()) {
            return
        }
        if (taskId != -1) {
            val updatedTask = task.value!!.copy(name = taskName, important = taskImportance)
            updateTask(updatedTask)
        } else {
            val newTask = Task(name = taskName, important = taskImportance)
            createTask(newTask)
        }
    }

    private fun createTask(task: Task) = viewModelScope.launch {
        taskDao.upsert(task)
    }

    private fun updateTask(task: Task) = viewModelScope.launch {
        taskDao.upsert(task)
    }
}