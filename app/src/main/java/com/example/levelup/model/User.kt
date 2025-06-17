package com.example.levelup.model

data class User(
    val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val score: Int = 0,
    val friends: List<String> = emptyList()
)
