package com.example.composesampleapplication20230220.taskdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composesampleapplication20230220.R
import com.example.composesampleapplication20230220.TodoDestinations
import com.example.composesampleapplication20230220.TodoDestinationsArgs
import com.example.composesampleapplication20230220.data.Task
import com.example.composesampleapplication20230220.data.Result
import com.example.composesampleapplication20230220.data.source.TasksRepository
import com.example.composesampleapplication20230220.tasks.TasksUiState
import com.example.composesampleapplication20230220.util.Async
import com.example.composesampleapplication20230220.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskDetailUiState(
    val task: Task? = null,
    val isLoading: Boolean = false,
    val userMessage: Int? = null,
    val isTaskDeleted: Boolean = false
)

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val tasksRepository: TasksRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val taskId: String = savedStateHandle[TodoDestinationsArgs.TASK_ID_ARG]!!

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(false)
    private val _isTaskDeleted = MutableStateFlow(false)
    private val _taskAsync = tasksRepository.getTaskStream(taskId)
        .map { handleResult(it) }
        .onStart { emit(Async.Loading) }

    val uiState: StateFlow<TaskDetailUiState> = combine(
        _userMessage, _isLoading, _isTaskDeleted, _taskAsync
    ) { userMessage, isLoading, isTaskDeleted, taskAsync ->
        when (taskAsync) {
            Async.Loading -> {
                TaskDetailUiState(isLoading = true)
            }
            is Async.Success -> {
                TaskDetailUiState(
                    task = taskAsync.data,
                    isLoading = isLoading,
                    userMessage = userMessage,
                    isTaskDeleted = isTaskDeleted
                )
            }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = TaskDetailUiState(isLoading = true)
        )

    fun deleteTask() = viewModelScope.launch {
        tasksRepository.deleteTask(taskId)
        _isTaskDeleted.value = true
    }

    fun setCompleted(completed: Boolean) = viewModelScope.launch {
        val task = uiState.value.task ?: return@launch
        if (completed) {
            tasksRepository.completeTask(task)
            showSnackbarMessage(R.string.task_marked_complete)
        } else {
            tasksRepository.activateTask(task)
            showSnackbarMessage(R.string.task_marked_active)
        }
    }

    fun snackbarMessageShown() {
        _userMessage.value = null
    }

    private fun handleResult(tasksResult: Result<Task>): Async<Task?> {
        return if (tasksResult is Result.Success) {
            Async.Success(tasksResult.data)
        } else {
            Async.Success(null)
        }
    }

    private fun showSnackbarMessage(message: Int) {
        _userMessage.value = message
    }
}