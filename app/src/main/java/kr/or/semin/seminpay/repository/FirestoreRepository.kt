package kr.or.semin.seminpay.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kr.or.semin.seminpay.models.User

class FirestoreRepository : Repository {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun getUser(email: String): User {
        val snapshot = db.collection("SeminPay")
            .document(email)
            .get().await()

        return snapshot.toObject(User::class.java)!!
    }

    fun getUserRealtime(email: String, callback: (User) -> Unit) {
        db.collection("SeminPay")
            .document(email)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(User::class.java)?.also {
                        callback.invoke(it)
                    }
                }
            }
    }

    override suspend fun sendMoney(user: User, money: Int): Boolean {
        try {
            db.collection("SeminPay")
                .document(user.email)
                .update("money", user.money + money).await()
            return true
        } catch (e: Exception) {
            return false
        }
    }
}