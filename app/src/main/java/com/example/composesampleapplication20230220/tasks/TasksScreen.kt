package com.example.composesampleapplication20230220.tasks

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composesampleapplication20230220.R
import com.example.composesampleapplication20230220.data.Task
import com.example.composesampleapplication20230220.ui.theme.ComposeSampleApplication20230220Theme
import com.example.composesampleapplication20230220.util.LoadingContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composesampleapplication20230220.util.TasksTopAppBar

@Composable
fun TasksScreen(
    @StringRes userMessage: Int,
    onTaskClick: (Task) -> Unit,
    onAddTask: () -> Unit,
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TasksViewModel = viewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    Scaffold(
        topBar = {
                 TasksTopAppBar(
                     openDrawer = openDrawer,
                     onFilterAllTasks = { /*TODO*/ },
                     onFilterActiveTasks = { /*TODO*/ },
                     onFilterCompletedTasks = { /*TODO*/ },
                     onClearCompletedTasks = { /*TODO*/ },
                     onRefresh = { /*TODO*/ }
                 )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTask) {
                Icon(Icons.Filled.Add, stringResource(id = R.string.add_task))
            }
        },
        modifier = modifier.fillMaxSize()
    ) {paddingValues ->
        val uiState by viewModel.uiState.collectAsState()

        TasksContent(
            loading = uiState.isLoading,
            tasks = uiState.items,
            currentFilteringLabel = uiState.filteringUiInfo.currentFilteringLabel,
            noTasksLabel = uiState.filteringUiInfo.noTasksLabel,
            noTasksIconRes = uiState.filteringUiInfo.noTaskIconRes,
            onRefresh = viewModel::refresh,
            onTaskClick = onTaskClick,
            onTaskCheckedChange = viewModel::completeTask,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun TasksContent(
    loading: Boolean,
    tasks: List<Task>,
    @StringRes currentFilteringLabel: Int,
    @StringRes noTasksLabel: Int,
    @DrawableRes noTasksIconRes: Int,
    onRefresh: () -> Unit,
    onTaskClick: (Task) -> Unit,
    onTaskCheckedChange: (Task, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LoadingContent(
        loading = loading,
        empty = tasks.isEmpty() && !loading,
        emptyContent = { TasksEmptyContent(noTasksLabel = noTasksLabel, noTasksIconRes = noTasksIconRes) },
        onRefresh = onRefresh
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.horizontal_margin))
        ) {
            Text(
                text = stringResource(currentFilteringLabel),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.list_item_padding),
                    vertical = dimensionResource(id = R.dimen.vertical_margin)
                ),
                style = MaterialTheme.typography.h6
            )
            LazyColumn {
                items(tasks) {task ->
                    TaskItem(
                        task = task,
                        onCheckedChange = {
                            onTaskCheckedChange(task, it)
                        },
                        onTaskClick = onTaskClick
                    )
                }
            }
        }    
    }
}

@Composable
private fun TaskItem(
    task: Task,
    onCheckedChange: (Boolean) -> Unit,
    onTaskClick: (Task) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.horizontal_margin),
                vertical = dimensionResource(id = R.dimen.list_item_padding)
            )
            .clickable { onTaskClick(task) }
    ) {
        Checkbox(checked = task.isCompleted, onCheckedChange = {checked ->
            onCheckedChange(checked)
        })
        Text(
            text = task.titleForList,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(
                start = dimensionResource(id = R.dimen.horizontal_margin)
            ),
            textDecoration = if (task.isCompleted) {
                TextDecoration.LineThrough
            } else {
                null
            }
        )
    }
}

@Composable
private fun TasksEmptyContent(
    @StringRes noTasksLabel: Int,
    @DrawableRes noTasksIconRes: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = noTasksIconRes),
            contentDescription = stringResource(id = R.string.no_tasks_image_content_description),
            modifier = Modifier.size(96.dp)
        )
        Text(text = stringResource(id = noTasksLabel))
    }
}

@Preview
@Composable
private fun TasksContentPreview() {
    ComposeSampleApplication20230220Theme {
        Surface {
            TasksContent(
                loading = false,
                tasks = listOf(
                    Task("Title 1", "Description 1"),
                    Task("Title 2", "Description 2", true),
                    Task("Title 3", "Description 3", true),
                    Task("Title 4", "Description 4"),
                    Task("Title 5", "Description 5", true)
                ),
                currentFilteringLabel = R.string.label_all,
                noTasksLabel = R.string.no_tasks_all,
                noTasksIconRes = R.drawable.logo_no_fill,
                onRefresh = { },
                onTaskClick = { },
                onTaskCheckedChange = { _, _ -> },
            )
        }
    }
}

@Preview
@Composable
private fun TasksContentEmptyPreview() {
    ComposeSampleApplication20230220Theme {
        Surface {
            TasksContent(
                loading = false,
                tasks = emptyList(),
                currentFilteringLabel = R.string.label_all,
                noTasksLabel = R.string.no_tasks_all,
                noTasksIconRes = R.drawable.logo_no_fill,
                onRefresh = { },
                onTaskClick = { },
                onTaskCheckedChange = { _, _ -> },
            )
        }
    }
}


@Preview
@Composable
private fun TaskItemPreview() {
    ComposeSampleApplication20230220Theme() {
        Surface {
            TaskItem(task = Task(
                title = "Task Title",
                isCompleted = true
            ), onTaskClick = {}, onCheckedChange = {})
        }
    }
}

@Preview
@Composable
private fun TaskEmptyContentPreview() {
    ComposeSampleApplication20230220Theme() {
        Surface {
            TasksEmptyContent(
                noTasksLabel = R.string.no_tasks_all,
                noTasksIconRes = R.drawable.logo_no_fill
            )
        }
    }
}