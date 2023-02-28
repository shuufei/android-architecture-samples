package com.example.composesampleapplication20230220

import android.app.Activity
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.Text
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.composesampleapplication20230220.TodoDestinationsArgs.TASK_ID_ARG
import com.example.composesampleapplication20230220.TodoDestinationsArgs.TITLE_ARG
import com.example.composesampleapplication20230220.TodoDestinationsArgs.USER_MESSAGE_ARG
import com.example.composesampleapplication20230220.addedittask.AddEditTaskScreen
import com.example.composesampleapplication20230220.addedittask.AddEditTaskViewModel
import com.example.composesampleapplication20230220.data.source.local.ToDoDatabase
import com.example.composesampleapplication20230220.tasks.TasksScreen
import com.example.composesampleapplication20230220.tasks.TasksViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun TodoNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    startDestination: String = TodoDestinations.TASKS_ROUTE,
    navActions: TodoNavigationActions = remember(navController) {
        TodoNavigationActions(navController)
    }
) {
    val db = Room.databaseBuilder(
        TodoApplication.getInstance(),
        ToDoDatabase::class.java, "Tasks.db"
    ).build()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            TodoDestinations.TASKS_ROUTE,
            arguments = listOf(
                navArgument(USER_MESSAGE_ARG) { type = NavType.IntType; defaultValue = 0 }
            )
        ) { entry ->
            TasksScreen(
                userMessage = entry.arguments?.getInt(USER_MESSAGE_ARG)!!,
                onTaskClick = {task ->
                    println("clicked task ${task.title}")
                },
                onAddTask = { navActions.navigateToAddEditTask(R.string.add_task, null) },
                openDrawer = { coroutineScope.launch {
                    drawerState.open()
                } },
//                viewModel = TasksViewModel(db)
            )
        }

        composable(
            TodoDestinations.ADD_EDIT_TASK_ROUTE,
            arguments = listOf(
                navArgument(TITLE_ARG) { type = NavType.IntType },
                navArgument(TASK_ID_ARG) { type = NavType.StringType; nullable = true },
            )
        ) { entry ->
            val taskId = entry.arguments?.getString(TASK_ID_ARG)
            AddEditTaskScreen(
                topBarTitle = entry.arguments?.getInt(TITLE_ARG)!!,
                onTaskUpdate = {
                    navActions.navigateToTasks(
                        if (taskId == null) ADD_EDIT_RESULT_OK else EDIT_RESULT_OK
                    ) },
                onBack = { navController.popBackStack() },
//                viewModel = AddEditTaskViewModel(db)
            )
        }
    }
}

const val ADD_EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val DELETE_RESULT_OK = Activity.RESULT_FIRST_USER + 2
const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 3