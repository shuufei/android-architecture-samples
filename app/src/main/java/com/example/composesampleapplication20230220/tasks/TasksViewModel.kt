package com.example.composesampleapplication20230220.tasks

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.composesampleapplication20230220.ADD_EDIT_RESULT_OK
import com.example.composesampleapplication20230220.DELETE_RESULT_OK
import com.example.composesampleapplication20230220.EDIT_RESULT_OK
import com.example.composesampleapplication20230220.R
import com.example.composesampleapplication20230220.data.Task
import com.example.composesampleapplication20230220.data.source.TasksRepository
import com.example.composesampleapplication20230220.data.source.local.ToDoDatabase
import com.example.composesampleapplication20230220.util.Async
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import com.example.composesampleapplication20230220.data.Result
import com.example.composesampleapplication20230220.util.WhileUiSubscribed
import kotlinx.coroutines.launch

data class TasksUiState(
    val items: List<Task> = listOf(Task(
        title = "task title 0001"
    )),
    val isLoading: Boolean = false,
    val filteringUiInfo: FilteringUiInfo = FilteringUiInfo(),
    val userMessage: Int? = null
)

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val tasksRepository: TasksRepository,
    private val db: ToDoDatabase
) : ViewModel() {

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val _filteredTasksAsync = combine(tasksRepository.getTasksStream()) { (tasks) ->
        filterTasks(tasks)
    }
        .map { Async.Success(it) }
        .onStart<Async<List<Task>>> { emit(Async.Loading) }

    val uiState: StateFlow<TasksUiState> = combine(
        _isLoading, _userMessage, _filteredTasksAsync
    ) { isLoading, userMessage,tasksAsync ->
        when (tasksAsync) {
            Async.Loading -> {
                TasksUiState(isLoading = true)
            }
            is Async.Success -> {
                TasksUiState(
                    items = tasksAsync.data,
                    isLoading = isLoading,
                    userMessage = userMessage
                )
            }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = TasksUiState(isLoading = true)
        )

    fun  clearCompletedTasks() {
        viewModelScope.launch {
            tasksRepository.clearCompletedTasks()
            showEditResultMessage(R.string.completed_tasks_cleared)
            refresh()
        }
    }

    fun completeTask(task: Task, completed: Boolean) = viewModelScope.launch {
        if (completed) {
            tasksRepository.completeTask(task)
            showSnackbarMessage(R.string.task_marked_complete)
        } else {
            tasksRepository.activateTask(task)
            showSnackbarMessage(R.string.task_marked_active)
        }
    }

    fun showEditResultMessage(result: Int) {
        when (result) {
            EDIT_RESULT_OK -> showSnackbarMessage(R.string.successfully_saved_task_message)
            ADD_EDIT_RESULT_OK -> showSnackbarMessage(R.string.successfully_added_task_message)
            DELETE_RESULT_OK -> showSnackbarMessage(R.string.successfully_deleted_task_message)
        }
    }

    fun snackbarMessageShown() {
        _userMessage.value = null
    }

    fun showSnackbarMessage(message: Int) {
        _userMessage.value = message
    }

    fun refresh() {
        // TODO: 実装
    }

    private fun filterTasks(
        tasksResult: Result<List<Task>>
    ): List<Task> = if (tasksResult is Result.Success) {
        filterItem(tasksResult.data)
    } else {
        emptyList()
    }

    private fun filterItem(tasks: List<Task>): List<Task> {
        val tasksToShow = ArrayList<Task>()
        for (task in tasks) {
//          // TODO: filter実装
            tasksToShow.add(task)
        }
        return tasksToShow
    }
}

data class FilteringUiInfo(
    val currentFilteringLabel: Int = R.string.label_all,
    val noTasksLabel: Int = R.string.no_tasks_all,
    val noTaskIconRes: Int = R.drawable.logo_no_fill,
)
