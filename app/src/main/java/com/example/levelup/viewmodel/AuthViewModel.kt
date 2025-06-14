package com.example.levelup.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.firebase.FirestoreUserService
import com.example.levelup.data.firebase.FirebaseAuthService
import com.example.levelup.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val authService = FirebaseAuthService()
    private val userService = FirestoreUserService()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError

    private val _registrationSuccess = MutableStateFlow<String?>(null)
    val registrationSuccess: StateFlow<String?> = _registrationSuccess

    init {
        val firebaseUser = authService.currentUser
        if (firebaseUser != null) {
            viewModelScope.launch {
                loadUser(firebaseUser.uid)
            }
        }
    }

    fun register(email: String, password: String, displayName: String) {
        viewModelScope.launch {
            try {
                clearAuthError()
                val firebaseUser = authService.register(email, password)
                firebaseUser?.let {
                    val user = User(uid = it.uid, displayName = displayName, email = email)
                    try {
                        userService.saveUser(user)
                        _currentUser.value = user
                    } catch (e: Exception) {
                        _authError.value = "Error saving an user"
                    }
                    _currentUser.value = user
                }
            } catch (e: Exception) {
                _authError.value = e.localizedMessage ?: "Registration failed"
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                clearAuthError()
                val firebaseUser = authService.login(email, password)
                if (firebaseUser != null) {
                    loadUser(firebaseUser.uid)
                } else {
                    _authError.value = "Incorrect email or password"
                }
            } catch (e: Exception) {
                _authError.value = "Incorrect email or password"
            }
        }
    }

    fun setAuthError(message: String) {
        _authError.value = message
    }

    fun clearAuthError() {
        _authError.value = null
    }

    fun clearRegistrationSuccess() {
        _registrationSuccess.value = null
    }

    private suspend fun loadUser(uid: String) {
        try {
            val user = userService.getUser(uid)
            _currentUser.value = user
        } catch (e: Exception) {
            _authError.value = "Failed to load user data"
        }
    }

    fun logout() {
        authService.logout()
        _currentUser.value = null
    }
}