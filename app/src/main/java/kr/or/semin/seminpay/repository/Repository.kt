package kr.or.semin.seminpay.repository

import kr.or.semin.seminpay.models.User

interface Repository {
    suspend fun getUser(email: String): User
    suspend fun sendMoney(user: User, money: Int): Boolean
}