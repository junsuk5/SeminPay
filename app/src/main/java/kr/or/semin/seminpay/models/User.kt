package kr.or.semin.seminpay.models

import com.google.gson.Gson
import java.io.Serializable

data class User(
    var name: String = "",
    var email: String = "",
    var money: Int = 0,
    var uid: String = ""
) : Serializable

fun User.toJson(): String {
    return Gson().toJson(this)
}

fun jsonToUser(json: String): User {
    return Gson().fromJson(json, User::class.java)
}