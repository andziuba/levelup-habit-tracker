package com.example.levelup.data.firebase

import com.example.levelup.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreUserService {

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    suspend fun saveUser(user: User) {
        usersCollection.document(user.uid).set(user).await()
    }

    suspend fun getUser(uid: String): User? {
        val doc = usersCollection.document(uid).get().await()
        return if (doc.exists()) doc.toObject(User::class.java) else null
    }
}
