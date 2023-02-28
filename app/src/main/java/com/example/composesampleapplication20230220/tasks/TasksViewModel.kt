package com.example.composesampleapplication20230220.tasks

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
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

//    val db = Room.databaseBuilder(
//        applicationContext,
//        ToDoDatabase::class.java, "Tasks.db"
//    ).build()

    private val _uiState = MutableStateFlow((TasksUiState()))
//    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    private val _filteredTasksAsync = combine(tasksRepository.getTasksStream()) { (tasks) ->
        filterTasks(tasks)
    }
        .map { Async.Success(it) }
        .onStart<Async<List<Task>>> { emit(Async.Loading) }

    val uiState: StateFlow<TasksUiState> = combine(
        _filteredTasksAsync
    ) { (tasksAsync) ->
        when (tasksAsync) {
            Async.Loading -> {
                TasksUiState(isLoading = true)
            }
            is Async.Success -> {
                TasksUiState(
                    items = tasksAsync.data
                )
            }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = TasksUiState(isLoading = true)
        )

    fun completeTask(task: Task, completed: Boolean) {
        // TODO: 実装
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
