package kr.or.semin.seminpay.extensions

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(text: String) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}

fun Fragment.logd(text: String) {
    Log.d(this::class.java.simpleName, text)
}