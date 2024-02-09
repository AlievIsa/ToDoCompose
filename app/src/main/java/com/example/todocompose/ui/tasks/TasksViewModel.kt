package com.example.todocompose.ui.tasks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todocompose.data.Task
import com.example.todocompose.data.TaskDao
import com.example.todocompose.ui.addedittask.ADD_TASK_RESULT_OK
import com.example.todocompose.ui.addedittask.EDIT_TASK_RESULT_OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val state: SavedStateHandle
) : ViewModel() {

    val tasks = taskDao.getTasks().asLiveData()

    private val taskEventChannel = Channel<TasksEvent>()
    val tasksEvent = taskEventChannel.receiveAsFlow()

    fun onTaskSwipedToDelete(task: Task) = viewModelScope.launch {
        taskDao.delete(task)
        taskEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
    }

    fun changeCompletedState(task: Task) = viewModelScope.launch {
        taskDao.upsert(task.copy(completed = !task.completed))
    }

    fun onUndoDeleteClick(task: Task) = viewModelScope.launch {
        taskDao.upsert(task)
    }

    fun onAddEditResult(addEditResult: Int) {
        when(addEditResult) {
            ADD_TASK_RESULT_OK -> showTaskSaveConfirmationMessage("Task added")
            EDIT_TASK_RESULT_OK -> showTaskSaveConfirmationMessage("Task updated")
        }
    }

    private fun showTaskSaveConfirmationMessage(message: String) = viewModelScope.launch {
        taskEventChannel.send(TasksEvent.ShowTaskSavedConfirmationMessage(message))
    }

    sealed class TasksEvent {

        data class ShowTaskSavedConfirmationMessage(val msg: String): TasksEvent()

        data class ShowUndoDeleteTaskMessage(val task: Task) : TasksEvent()
    }
}