package com.example.levelup.ui.screens.friends

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.levelup.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.example.levelup.model.User

@Composable
fun FriendsScreen(viewModel: AuthViewModel) {
    val friendEmail = remember { mutableStateOf("") }
    val status by viewModel.addFriendStatus.collectAsState()
    val friends by viewModel.friends.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFriends()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = 16.dp, top = 40.dp, end = 16.dp, bottom = 16.dp)

    )

    {
        Text(
            text = "Friends",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = friendEmail.value,
            onValueChange = { friendEmail.value = it },
            label = { Text("Enter friend's email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.addFriendByEmail(friendEmail.value)
                viewModel.loadFriends()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Add Friend")
        }

        Spacer(modifier = Modifier.height(8.dp))

        status?.let {
            Text(
                text = it,
                color = if (it.contains("success", true)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Your Friends:", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(friends) { friend ->
                FriendItem(friend = friend)
            }
        }
    }

    LaunchedEffect(status) {
        if (status != null) {
            delay(3000)
            viewModel.clearAddFriendStatus()
        }
    }
}

@Composable
fun FriendItem(friend: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = friend.displayName.firstOrNull()?.uppercase() ?: "?",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = friend.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = friend.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
