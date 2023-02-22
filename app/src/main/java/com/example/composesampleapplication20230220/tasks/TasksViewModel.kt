package com.example.composesampleapplication20230220.tasks

import androidx.lifecycle.ViewModel
import com.example.composesampleapplication20230220.R
import com.example.composesampleapplication20230220.data.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class TasksUiState(
    val items: List<Task> = listOf(Task(
        title = "task title 0001"
    )),
    val isLoading: Boolean = false,
    val filteringUiInfo: FilteringUiInfo = FilteringUiInfo(),
    val userMessage: Int? = null
)

class TasksViewModel: ViewModel() {
    private val _uiState = MutableStateFlow((TasksUiState()))
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

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
