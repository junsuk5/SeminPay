package kr.or.semin.seminpay.extensions

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.logd(text: String) {
    Log.d(this::class.java.simpleName, text)
}

inline fun <reified T> Context.startActivity() {
    startActivity(Intent(this, T::class.java))
}