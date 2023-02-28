package com.example.composesampleapplication20230220.addedittask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composesampleapplication20230220.data.Task
import com.example.composesampleapplication20230220.data.source.local.ToDoDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddEditTaskUiState(
    val title: String = "",
    val description: String = "",
    val isTaskCompleted: Boolean = false,
    val isLoading: Boolean = false,
    val userMessage: Int? = null,
    val isTaskSaved: Boolean = false
)

class AddEditTaskViewModel(
    private val db: ToDoDatabase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddEditTaskUiState())
    val uiState: StateFlow<AddEditTaskUiState> = _uiState.asStateFlow()

    fun saveTask() {
        createNewTask()
    }

    fun updateTitle(newTitle: String) {
        _uiState.update {
            it.copy(title = newTitle)
        }
    }

    fun updateDescription(newDescription: String) {
        _uiState.update {
            it.copy(description = newDescription)
        }
    }

    private fun createNewTask() = viewModelScope.launch {
        val newTask = Task(uiState.value.title, uiState.value.description)
        db.taskDao().insertTask(newTask)
        _uiState.update {
            it.copy(isTaskSaved = true)
        }
    }
}
