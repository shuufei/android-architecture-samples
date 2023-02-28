package com.example.composesampleapplication20230220.data.source

import com.example.composesampleapplication20230220.data.Result
import com.example.composesampleapplication20230220.data.Task
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class DefaultTasksRepository(
    private val tasksLocalDataSource: TasksDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TasksRepository {
    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> {
        return tasksLocalDataSource.getTasks()
    }

    override suspend fun refreshTasks() {
        return tasksLocalDataSource.refreshTasks()
    }

    override fun getTasksStream(): Flow<Result<List<Task>>> {
        return tasksLocalDataSource.getTasksStream()
    }

    override suspend fun refreshTask(taskId: String) {
        return tasksLocalDataSource.refreshTask(taskId)
    }

    override fun getTaskStream(taskId: String): Flow<Result<Task>> {
        return tasksLocalDataSource.getTaskStream(taskId)
    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Result<Task> {
        return tasksLocalDataSource.getTask(taskId)
    }

    override suspend fun saveTask(task: Task) {
        coroutineScope {
            launch { tasksLocalDataSource.saveTask(task) }
        }
    }

    override suspend fun completeTask(task: Task) {
        coroutineScope {
            launch { tasksLocalDataSource.completeTask(task) }
        }
    }

    override suspend fun completeTask(taskId: String) {
        coroutineScope {
            launch { tasksLocalDataSource.completeTask(taskId) }
        }
    }

    override suspend fun activateTask(task: Task) {
        coroutineScope {
            launch { tasksLocalDataSource.activateTask(task) }
        }
    }

    override suspend fun activateTask(taskId: String) {
        withContext(ioDispatcher) {
            (getTaskWithId(taskId) as? Result.Success)?.let { it ->
                activateTask(it.data)
            }
        }
    }

    override suspend fun clearCompletedTasks() {
        coroutineScope {
            launch { tasksLocalDataSource.clearCompletedTasks() }
        }
    }

    override suspend fun deleteAllTasks() {
        withContext(ioDispatcher) {
            coroutineScope {
                launch { tasksLocalDataSource.deleteAllTasks() }
            }
        }
    }

    override suspend fun deleteTask(taskId: String) {
        coroutineScope {
            launch { tasksLocalDataSource.deleteTask(taskId) }
        }
    }

    private suspend fun getTaskWithId(id: String): Result<Task> {
        return tasksLocalDataSource.getTask(id)
    }
}