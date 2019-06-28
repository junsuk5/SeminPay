package kr.or.semin.seminpay

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import kr.or.semin.seminpay.models.User
import kr.or.semin.seminpay.models.toJson
import kr.or.semin.seminpay.repository.FirestoreRepository


class MainViewModel : ViewModel() {
    private val barcodeEncoder = BarcodeEncoder()
    private val repository = FirestoreRepository()

    val myUserLiveData = MutableLiveData<User>()

    val user = User("오준석","a811219@gmail.com", 10000)
    val myQrJson = MutableLiveData<String>()

    init {
        myQrJson.value = user.toJson()
    }

    fun encodeQr(content: String): Bitmap {
        return barcodeEncoder.encodeBitmap(
            content,
            BarcodeFormat.QR_CODE, 350, 350
        )
    }

    suspend fun getUser(email: String): User {
        return repository.getUser(email)
    }

    suspend fun sendMoney(user: User, money: Int): Boolean {
        return repository.sendMoney(user, money)
    }

    fun loadMyInfo() {
        repository.getUserRealtime("a811219@gmail.com") {
            myUserLiveData.value = it
        }
    }

}