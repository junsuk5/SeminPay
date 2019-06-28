package kr.or.semin.seminpay.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kr.or.semin.seminpay.models.User

class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getUser(email: String): User {
        val snapshot = db.collection("SeminPay")
            .document(email)
            .get().await()

        return snapshot.toObject(User::class.java)!!
    }

    suspend fun getMyInfo(callback: (User) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser

        user?.let {
            db.collection("SeminPay")
                .document(user.email!!)
                .get().await().exists()
                .let {
                    if (!it) {
                        db.collection("SeminPay")
                            .document(user.email!!)
                            .set(
                                User(
                                    user.displayName!!,
                                    user.email!!,
                                    5000
                                )
                            ).await()
                    }
                }


            db.collection("SeminPay")
                .document(user.email!!)
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
    }

    suspend fun sendMoney(me: User, you: User, money: Int): Boolean {
        if (me.money - money < 0) {
            return false
        }

        try {
            val result = db.runTransaction {
                db.collection("SeminPay")
                    .document(you.email)
                    .update("money", you.money + money)

                db.collection("SeminPay")
                    .document(me.email)
                    .update("money", me.money - money)
            }.await()

            return result.isComplete

        } catch (e: Exception) {
            return false
        }
    }


}