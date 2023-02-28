package com.example.composesampleapplication20230220.tasks

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.composesampleapplication20230220.R
import com.example.composesampleapplication20230220.data.Task
import com.example.composesampleapplication20230220.data.source.local.ToDoDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class TasksUiState(
    val items: List<Task> = listOf(Task(
        title = "task title 0001"
    )),
    val isLoading: Boolean = false,
    val filteringUiInfo: FilteringUiInfo = FilteringUiInfo(),
    val userMessage: Int? = null
)

//@HiltViewModel
class TasksViewModel(
    private val db: ToDoDatabase
) : ViewModel() {

//    val db = Room.databaseBuilder(
//        applicationContext,
//        ToDoDatabase::class.java, "Tasks.db"
//    ).build()

    private val _uiState = MutableStateFlow((TasksUiState()))
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    val tasks = db.taskDao().observeTasks()

    fun completeTask(task: Task, completed: Boolean) {
        // TODO: 実装
    }

    fun refresh() {
        // TODO: 実装
    }
}

data class FilteringUiInfo(
    val currentFilteringLabel: Int = R.string.label_all,
    val noTasksLabel: Int = R.string.no_tasks_all,
    val noTaskIconRes: Int = R.drawable.logo_no_fill,
)
