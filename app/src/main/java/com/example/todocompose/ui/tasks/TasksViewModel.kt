package com.example.todocompose.ui.tasks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todocompose.data.PreferencesManager
import com.example.todocompose.data.Task
import com.example.todocompose.data.TaskDao
import com.example.todocompose.ui.addedittask.ADD_TASK_RESULT_OK
import com.example.todocompose.ui.addedittask.EDIT_TASK_RESULT_OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager,
    private val state: SavedStateHandle
) : ViewModel() {

    val searchQuery = state.getLiveData("searchQuery", "")

    val preferencesFlow = preferencesManager.preferencesFlow

//    @OptIn(ExperimentalCoroutinesApi::class)
//    private val taskFlow = combine(
//        searchQuery.asFlow(), preferencesFlow
//    ) { searchQuery, filterPreferences ->
//        Pair(searchQuery, filterPreferences)
//    }.flatMapLatest { (query, filterPreferences) ->
//        taskDao.getTasks(query, filterPreferences.sortOrder, filterPreferences.hideCompleted)
//    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val taskFlow = preferencesFlow.flatMapLatest { taskDao.getTasks(it.hideCompleted) }


    val tasks = taskFlow.asLiveData()

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

    fun onHideCompletedClick(hideCompleted: Boolean) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

    private fun showTaskSaveConfirmationMessage(message: String) = viewModelScope.launch {
        taskEventChannel.send(TasksEvent.ShowTaskSavedConfirmationMessage(message))
    }

    sealed class TasksEvent {

        data class ShowTaskSavedConfirmationMessage(val msg: String): TasksEvent()

        data class ShowUndoDeleteTaskMessage(val task: Task) : TasksEvent()
    }
}