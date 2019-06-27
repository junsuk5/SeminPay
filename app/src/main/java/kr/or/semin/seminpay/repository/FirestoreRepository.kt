package kr.or.semin.seminpay.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kr.or.semin.seminpay.models.User

class FirestoreRepository: Repository {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun getUser(email: String): User {
        val snapshot = db.collection("SeminPay")
            .whereEqualTo("email", email)
            .limit(1)
            .get().await()

        return snapshot.toObjects(User::class.java)[0]
    }

    override suspend fun sendMoney(user: User, money: Int): Boolean {
        try {
            db.collection("SeminPay")
                .document(user.uid)
                .update("money", user.money + money).await()
            return true
        } catch (e: Exception) {
            return false
        }
    }
}