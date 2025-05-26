// AddHabitScreen.kt
package com.example.levelup.ui.screens.dailyhabit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.levelup.R
import com.example.levelup.model.Duration
import com.example.levelup.model.Frequency
import com.example.levelup.model.Habit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(
    navController: NavController,
    onHabitAdded: (Habit) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedFrequency by remember { mutableStateOf(Frequency.DAILY) }
    var hours by remember { mutableStateOf(0) }
    var minutes by remember { mutableStateOf(30) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.add_new_habit)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.habit_name)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.frequency),
                    style = MaterialTheme.typography.titleMedium
                )

                Frequency.values().forEach { frequency ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(frequency.name.lowercase().replaceFirstChar { it.uppercase() })
                        RadioButton(
                            selected = frequency == selectedFrequency,
                            onClick = { selectedFrequency = frequency }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.duration),
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    NumberPicker(
                        label = stringResource(R.string.hours),
                        value = hours,
                        onValueChange = { hours = it },
                        range = 0..23
                    )

                    NumberPicker(
                        label = stringResource(R.string.minutes),
                        value = minutes,
                        onValueChange = { minutes = it },
                        range = 0..59
                    )
                }
            }
        },
        bottomBar = {
            BottomAppBar {
                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            onHabitAdded(
                                Habit(
                                    name = name,
                                    frequency = selectedFrequency,
                                    duration = Duration(hours, minutes)
                                )
                            )
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    enabled = name.isNotBlank()
                ) {
                    Text(stringResource(R.string.save))
                }
            }
        }
    )
}

@Composable
fun NumberPicker(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    if (value > range.first) {
                        onValueChange(value - 1)
                    }
                }
            ) {
                Text("-")
            }
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            IconButton(
                onClick = {
                    if (value < range.last) {
                        onValueChange(value + 1)
                    }
                }
            ) {
                Text("+")
            }
        }
    }
}