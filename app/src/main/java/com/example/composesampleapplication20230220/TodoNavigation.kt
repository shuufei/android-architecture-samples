package com.example.composesampleapplication20230220

// Q: なぜ自身のファイルからimportして利用しているのか
import androidx.navigation.NavHostController
import com.example.composesampleapplication20230220.TodoScreens.TASKS_SCREEN
import com.example.composesampleapplication20230220.TodoScreens.STATISTICS_SCREEN
import com.example.composesampleapplication20230220.TodoScreens.TASK_DETAIL_SCREEN
import com.example.composesampleapplication20230220.TodoScreens.ADD_EDIT_TASK_SCREEN
import com.example.composesampleapplication20230220.TodoDestinationsArgs.USER_MESSAGE_ARG
import com.example.composesampleapplication20230220.TodoDestinationsArgs.TASK_ID_ARG
import com.example.composesampleapplication20230220.TodoDestinationsArgs.TITLE_ARG

private object TodoScreens {
    const val TASKS_SCREEN = "tasks"
    const val STATISTICS_SCREEN = "statistics"
    const val TASK_DETAIL_SCREEN = "task"
    const val ADD_EDIT_TASK_SCREEN = "addEditTask"
}

object TodoDestinationsArgs {
    const val USER_MESSAGE_ARG = "userMessage"
    const val TASK_ID_ARG = "taskId"
    const val TITLE_ARG = "title"
}

object TodoDestinations {
    const val TASKS_ROUTE = "$TASKS_SCREEN?$USER_MESSAGE_ARG={$USER_MESSAGE_ARG}"
    const val STATISTICS_ROUTE = STATISTICS_SCREEN
    const val TASK_DETAIL_ROUTE = "$TASK_DETAIL_SCREEN/{$TASK_ID_ARG}"
    const val ADD_EDIT_TASK_ROUTE = "$ADD_EDIT_TASK_SCREEN/{$TITLE_ARG}?$TASK_ID_ARG={$TASK_ID_ARG}"
}

class TodoNavigationActions(private val navController: NavHostController) {
    fun navigateToTasks(userMessage: Int = 0) {
        val navigationFromDrawer = userMessage == 0
        navController.navigate(
            TASKS_SCREEN.let {
                if (userMessage != 0) "$it?$USER_MESSAGE_ARG=$userMessage" else it
            }
        )
    }

    fun navigateToAddEditTask(title: Int, taskId: String?) {
        navController.navigate(
            // Q: このletの使い方って何？
            "$ADD_EDIT_TASK_SCREEN/$title".let {
                if (taskId != null) "$it?$TASK_ID_ARG=$taskId" else it
            }
        )
    }
}

