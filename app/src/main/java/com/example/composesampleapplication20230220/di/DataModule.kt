package com.example.composesampleapplication20230220.di

import android.content.Context
import androidx.room.Room
import com.example.composesampleapplication20230220.data.source.DefaultTasksRepository
import com.example.composesampleapplication20230220.data.source.TasksDataSource
import com.example.composesampleapplication20230220.data.source.TasksRepository
import com.example.composesampleapplication20230220.data.source.local.TasksLocalDataSource
import com.example.composesampleapplication20230220.data.source.local.ToDoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LocalTasksDataSource

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideTasksRepository(
        @LocalTasksDataSource localDataSource: TasksDataSource,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): TasksRepository {
        return DefaultTasksRepository(localDataSource, ioDispatcher)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Singleton
    @LocalTasksDataSource
    @Provides
    fun provideTasksLocalDataSource(
        database: ToDoDatabase,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): TasksDataSource {
        return TasksLocalDataSource(database.taskDao(), ioDispatcher)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): ToDoDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ToDoDatabase::class.java,
            "Tasks.db"
        ).build()
    }
}

