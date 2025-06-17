package com.example.levelup.ui.screens.leaderboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levelup.model.User
import com.example.levelup.viewmodel.AuthViewModel

@Composable
fun LeaderboardScreen(viewModel: AuthViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val friends by viewModel.friends.collectAsState()

    // Combine current user and friends into one list
    val allUsers = remember(currentUser, friends) {
        val combined = mutableListOf<User>()
        currentUser?.let { combined.add(it) }
        combined.addAll(friends)
        combined.sortedByDescending { it.score }
    }

    LaunchedEffect(Unit) {
        viewModel.loadFriends()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = 16.dp, top = 40.dp, end = 16.dp, bottom = 16.dp)

    ) {
        Text(
            text = "Leaderboard",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (allUsers.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No users to display")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(allUsers) { user ->
                    UserRankItem(
                        user = user,
                        isCurrentUser = user.uid == currentUser?.uid
                    )
                }
            }
        }
    }
}

@Composable
fun UserRankItem(user: User, isCurrentUser: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentUser) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = user.displayName,
                    fontWeight = if (isCurrentUser) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 18.sp
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text(
                text = "${user.score} pts",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}