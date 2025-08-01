package com.example.levelup.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.firebase.FirestoreUserService
import com.example.levelup.data.firebase.FirebaseAuthService
import com.example.levelup.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _addFriendStatus = MutableStateFlow<String?>(null)
    val addFriendStatus: StateFlow<String?> = _addFriendStatus

    private val _friends = MutableStateFlow<List<User>>(emptyList())
    val friends: StateFlow<List<User>> = _friends

    init {
        val firebaseUser = authService.currentUser
        if (firebaseUser != null) {
            viewModelScope.launch {
                loadUser(firebaseUser.uid)
            }
        }
    }

    fun loadFriends() {
        val currentUid = _currentUser.value?.uid ?: return
        viewModelScope.launch {
            try {
                _friends.value = userService.getFriends(currentUid)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Failed to load friends", e)
            }
        }
    }

    fun addFriendByEmail(email: String) {
        val currentUid = _currentUser.value?.uid ?: return

        viewModelScope.launch {
            try {
                val userToAdd = userService.getUserByEmail(email)
                if (userToAdd == null) {
                    _addFriendStatus.value = "User with this email doesn't exist"
                    return@launch
                }

                val currentUser = _currentUser.value
                if (currentUser == null) {
                    _addFriendStatus.value = "Current user not loaded"
                    return@launch
                }

                // Check if friend is already added
                val friendIds = currentUser.friends ?: emptyList()
                if (userToAdd.uid in friendIds) {
                    _addFriendStatus.value = "User is already your friend"
                    return@launch
                }

                // Add friend
                userService.addFriend(currentUid, userToAdd.uid)
                _addFriendStatus.value = "Friend added successfully"

                // Reload friends list to update UI
                loadFriends()

                // Also update currentUser with new friends list (optional)
                val updatedFriends = friendIds + userToAdd.uid
                _currentUser.value = currentUser.copy(friends = updatedFriends)

            } catch (e: Exception) {
                _addFriendStatus.value = "Failed to add friend"
            }
        }
    }


    fun clearAddFriendStatus() {
        _addFriendStatus.value = null
    }


    fun signInWithGoogle(idToken: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                clearAuthError()
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val firebaseUser = authService.signInWithCredential(credential)
                firebaseUser?.let { user ->
                    if (user.displayName != null) {
                        // Sprawdź czy użytkownik już istnieje w Firestore
                        val existingUser = try {
                            userService.getUser(user.uid)
                        } catch (e: Exception) {
                            null
                        }

                        if (existingUser == null) {
                            // Jeśli użytkownik nie istnieje, utwórz nowy
                            val newUser = User(
                                uid = user.uid,
                                displayName = user.displayName ?: "",
                                email = user.email ?: ""
                            )
                            userService.saveUser(newUser)
                            _currentUser.value = newUser
                        } else {
                            _currentUser.value = existingUser
                        }
                    }
                }
            } catch (e: Exception) {
                _authError.value = e.localizedMessage ?: "Google sign in failed"
            } finally {
                _isLoading.value = false
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

//    fun addToUserScore(points: Int) {
//        viewModelScope.launch {
//            currentUser.value?.let { user ->
//                val newScore = user.score + points
//                // Update in Firebase or your backend
//                updateUserScore(newScore)
//            }
//        }
//    }
//

    fun updateLocalUserScore(pointsToAdd: Int) {
        viewModelScope.launch {
            _currentUser.update { currentUser ->
                currentUser?.copy(score = (currentUser.score ?: 0) + pointsToAdd)
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