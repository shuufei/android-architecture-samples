package com.example.composesampleapplication20230220

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
import com.example.composesampleapplication20230220.TodoDestinationsArgs.TASK_ID_ARG
import com.example.composesampleapplication20230220.TodoDestinationsArgs.TITLE_ARG
import com.example.composesampleapplication20230220.TodoDestinationsArgs.USER_MESSAGE_ARG
import com.example.composesampleapplication20230220.tasks.TasksScreen
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
                } }
            )
        }

        composable(
            TodoDestinations.ADD_EDIT_TASK_ROUTE,
            arguments = listOf(
                navArgument(TITLE_ARG) { type = NavType.IntType },
                navArgument(TASK_ID_ARG) { type = NavType.StringType; nullable = true },
            )
        ) { entry ->
                AddEditTaskScreenTmp()
            }
    }
}

@Composable
fun AddEditTaskScreenTmp() {
    Text(text = "add edit task screen")
}