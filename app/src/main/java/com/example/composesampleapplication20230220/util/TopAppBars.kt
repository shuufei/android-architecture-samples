package com.example.composesampleapplication20230220.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.composesampleapplication20230220.R
import com.example.composesampleapplication20230220.ui.theme.ComposeSampleApplication20230220Theme

@Composable
fun TasksTopAppBar(
    openDrawer: () -> Unit,
    onFilterAllTasks: () -> Unit,
    onFilterActiveTasks: () -> Unit,
    onFilterCompletedTasks: () -> Unit,
    onClearCompletedTasks: () -> Unit,
    onRefresh: () -> Unit
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon( Icons.Filled.Menu, contentDescription = stringResource(id = R.string.open_drawer))
            }
        },
        actions = {
            FilterTasksMenu(
                onFilterAllTasks = onFilterAllTasks,
                onFilterActiveTasks = onFilterActiveTasks,
                onFilterCompletedTasks = onFilterActiveTasks
            )
            MoreTasksMenu(
                onClearCompletedTasks = onClearCompletedTasks,
                onRefresh = onRefresh
            )
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun FilterTasksMenu(
    onFilterAllTasks: () -> Unit,
    onFilterActiveTasks: () -> Unit,
    onFilterCompletedTasks: () -> Unit
) {
    TopAppBarDropdownMenu(iconContent = {
        Icon(
            painter = painterResource(id = R.drawable.ic_filter_list),
            contentDescription = stringResource(id = R.string.menu_filter)
        )
    }) { closeMenu ->
        DropdownMenuItem(onClick = { onFilterAllTasks(); closeMenu() }) {
            Text(text = stringResource(id = R.string.nav_all))
        }
        DropdownMenuItem(onClick = { onFilterActiveTasks(); closeMenu() }) {
            Text(text = stringResource(id = R.string.nav_active))
        }
        DropdownMenuItem(onClick = { onFilterCompletedTasks(); closeMenu() }) {
            Text(text = stringResource(id = R.string.nav_completed))
        }
    }
}

@Composable
private fun MoreTasksMenu(
    onClearCompletedTasks: () -> Unit,
    onRefresh: () -> Unit
) {
    TopAppBarDropdownMenu(
        iconContent = {
            Icon(Icons.Filled.MoreVert, stringResource(id = R.string.menu_more))
        }
    ) { closeMenu ->
        DropdownMenuItem(onClick = { onClearCompletedTasks(); closeMenu() }) {
            Text(text = stringResource(id = R.string.menu_clear))
        }
        DropdownMenuItem(onClick = { onRefresh(); closeMenu() }) {
            Text(text = stringResource(id = R.string.refresh))
        }
    }
}

@Composable
private fun TopAppBarDropdownMenu(
    iconContent: @Composable () -> Unit,
    // Q: この構文何
    content: @Composable ColumnScope.(() -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
        IconButton(onClick = { expanded = !expanded }) {
            iconContent()
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentSize((Alignment.TopEnd))
        ) {
            // Q: この構文何
            content { expanded = !expanded }
        }
    }
}


@Preview
@Composable
private fun TasksTopAppBarPreview() {
    ComposeSampleApplication20230220Theme() {
        Surface {
            TasksTopAppBar({}, {}, {}, {}, {}, {})
        }
    }
}