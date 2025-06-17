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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import com.example.levelup.R

fun calculateLevel(exp: Int): Pair<Int, Int> {
    val level = exp / 100 + 1
    val expToNextLevel = 100 - (exp % 100)
    return Pair(level, expToNextLevel)
}

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
    onNavigateToLeaderboard: () -> Unit,
    onNavigateToMonth: () -> Unit
) {
    val currentLevel = score / 100 + 1
    val expInCurrentLevel = score % 100
    val progress = expInCurrentLevel / 100f

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
                modifier = Modifier.padding(bottom = 4.dp)
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

                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { onThemeChange(it) },
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Nowy wiersz z informacją o poziomie
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Level $currentLevel",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    Text(
                        text = "$expInCurrentLevel/100",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .padding(top = 4.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
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

        //Monthly View
        NavigationDrawerItem(
            label = { Text("Monthly") },
            icon = { Icon(Icons.Filled.CalendarMonth, contentDescription = "Month") },
            selected = false,
            onClick = {
                scope.launch { drawerState.close() }
                onNavigateToMonth()
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