package com.example.composesampleapplication20230220.data.source


import com.example.composesampleapplication20230220.data.Result
import com.example.composesampleapplication20230220.data.Task
import kotlinx.coroutines.flow.Flow

interface TasksDataSource {
    fun getTasksStream(): Flow<Result<List<Task>>>

    suspend fun getTasks(): Result<List<Task>>

    suspend fun refreshTasks()

    fun getTaskStream(taskId: String): Flow<Result<Task>>

    suspend fun getTask(taskId: String): Result<Task>

    suspend fun refreshTask(taskId: String)

    suspend fun saveTask(task: Task)

    suspend fun completeTask(task: Task)

    suspend fun completeTask(taskId: String)

    suspend fun activateTask(task: Task)

    suspend fun activateTask(taskId: String)

    suspend fun clearCompletedTasks()

    suspend fun deleteAllTasks()

    suspend fun deleteTask(taskId: String)
}