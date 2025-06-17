package com.example.levelup.data.firebase

import com.example.levelup.model.User
import com.google.firebase.firestore.FieldValue
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

    suspend fun getUserByEmail(email: String): User? {
        val snapshot = usersCollection
            .whereEqualTo("email", email)
            .limit(1)
            .get()
            .await()

        return if (!snapshot.isEmpty) {
            snapshot.documents[0].toObject(User::class.java)
        } else {
            null
        }
    }

    suspend fun addFriend(currentUserId: String, friendId: String) {
        val userRef = usersCollection.document(currentUserId)
        userRef.update("friends", FieldValue.arrayUnion(friendId)).await()
    }

    suspend fun getFriends(uid: String): List<User> {
        val userDoc = usersCollection.document(uid).get().await()
        val friendIds = userDoc.get("friends") as? List<String> ?: emptyList()

        if (friendIds.isEmpty()) return emptyList()

        val tasks = friendIds.map { usersCollection.document(it).get() }
        val docs = tasks.map { it.await() }
        return docs.mapNotNull { it.toObject(User::class.java) }
    }

    suspend fun updateUserScore(uid: String, pointsToAdd: Int) {
        usersCollection.document(uid).update("score", FieldValue.increment(pointsToAdd.toLong())).await()
    }
}
