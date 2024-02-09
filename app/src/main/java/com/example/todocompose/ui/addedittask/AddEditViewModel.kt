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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val ADD_TASK_RESULT_OK = 1
const val EDIT_TASK_RESULT_OK = 2

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val state: SavedStateHandle
) : ViewModel() {

    private val taskId: Int = checkNotNull(state["taskId"])
    private val task = MutableStateFlow<Task?>(null)
    var taskName by mutableStateOf("")
    var taskImportance by mutableStateOf(false)
    var createdDateFormatted by mutableStateOf("")
    var topBarTitle by mutableStateOf("Add task")

    init {
        viewModelScope.launch {
            if (taskId != -1) {
                taskDao.getTaskById(taskId).collect {
                    task.value = it
                    taskName = it.name
                    taskImportance = it.important
                    createdDateFormatted = it.createdDateFormatted
                    topBarTitle = "Edit task"
                }
            }
        }
    }

    private val addEditTaskChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskChannel.receiveAsFlow()

    fun onSaveClick() {
        if (taskName.isBlank()) {
            showInvalidInputMessage("Name cannot be empty")
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
        addEditTaskChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))
    }

    private fun updateTask(task: Task) = viewModelScope.launch {
        taskDao.upsert(task)
        addEditTaskChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_TASK_RESULT_OK))
    }

    private fun showInvalidInputMessage(msg: String) = viewModelScope.launch {
        addEditTaskChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(msg))
    }

    sealed class AddEditTaskEvent {
        data class ShowInvalidInputMessage(val msg: String): AddEditTaskEvent()
        data class NavigateBackWithResult(val result: Int): AddEditTaskEvent()
    }
}