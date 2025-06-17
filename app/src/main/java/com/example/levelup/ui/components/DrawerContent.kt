package com.example.levelup.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import com.example.levelup.R

@Composable
fun DrawerContent(
    onLogoutClicked: () -> Unit,
    drawerState: DrawerState,
    scope: CoroutineScope,
    displayName: String,
    score: Int,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onNavigateToDailyHabits: () -> Unit,
    onNavigateToAddHabit: () -> Unit,
    onNavigateToFriends: () -> Unit,
    onNavigateToLeaderboard: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.fillMaxHeight(),
        drawerContainerColor = MaterialTheme.colorScheme.surface,
        drawerContentColor = MaterialTheme.colorScheme.onSurface,
        drawerTonalElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .padding(end = 8.dp),
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "$score points",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Theme switch in top right corner
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { onThemeChange(it) },
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        // Divider
        HorizontalDivider(
            color = MaterialTheme.colorScheme.surfaceVariant,
            thickness = 1.dp
        )

        // Navigation items
        NavigationDrawerItem(
            label = { Text("Today's Habits") },
            icon = { Icon(Icons.Filled.Today, contentDescription = "Daily Habits") },
            selected = false,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigateToDailyHabits()
            },
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            label = { Text("Add Habit") },
            icon = { Icon(Icons.Filled.Add, contentDescription = "Add Habit") },
            selected = false,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigateToAddHabit()
            },
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            label = { Text("Friends") },
            icon = { Icon(Icons.Filled.Group, contentDescription = "Friends") },
            selected = false,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigateToFriends()
            },
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            label = { Text("Leaderboard") },
            icon = { Icon(Icons.Filled.Leaderboard, contentDescription = "Leaderboard") },
            selected = false,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigateToLeaderboard()
            },
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Logout button
        NavigationDrawerItem(
            label = { Text("Logout") },
            icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout") },
            selected = false,
            onClick = {
                scope.launch { drawerState.close() }
                onLogoutClicked()
            },
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}