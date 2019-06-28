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

    val myQrJson = MutableLiveData<String>()

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
        val me = myUserLiveData.value
        return if (me != null) {
            repository.sendMoney(me, user, money)
        } else {
            false
        }
    }

    suspend fun loadMyInfo() {
        // 내 정보 조회를 했는데 없으면 DB에 작성

        // 실시간 모니터링
        repository.getMyInfo {
            myUserLiveData.value = it
            myQrJson.value = it.toJson()
        }
    }

}