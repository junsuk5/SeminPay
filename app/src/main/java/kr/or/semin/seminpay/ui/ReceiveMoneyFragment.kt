package kr.or.semin.seminpay.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_send_money.*
import kr.or.semin.seminpay.MainViewModel
import kr.or.semin.seminpay.R

class ReceiveMoneyFragment : Fragment() {
    val viewModel: MainViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_send_money, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // QR 이미지
        viewModel.myQrJson.observe(this, Observer {
            val bitmap = viewModel.encodeQr(it)
            Glide.with(this).load(bitmap).into(qr_image)
        })

        // 내 잔액
        viewModel.myUserLiveData.observe(this, Observer {
            val amountText = String.format(requireContext().getString(R.string.amount), it.money)
            amount_text.text = amountText
        })

        viewModel.loadMyInfo()
    }


}
