package kr.or.semin.seminpay.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_send_money_dialog.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.or.semin.seminpay.MainViewModel
import kr.or.semin.seminpay.R
import kr.or.semin.seminpay.extensions.toast
import kr.or.semin.seminpay.models.User


class SendMoneyDialogFragment : DialogFragment() {
    val viewModel: MainViewModel by activityViewModels()
    val args by navArgs<SendMoneyDialogFragmentArgs>()

    val userLiveData = MutableLiveData<User>()

    lateinit var moneyEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_send_money_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameTextView = view.findViewById<TextView>(R.id.name_text)
        moneyEditText = view.findViewById<EditText>(R.id.money_edit)
        userLiveData.observe(this, Observer {
            nameTextView.text =
                String.format(requireContext().getString(R.string.receive_name), it.name)
            moneyEditText.isEnabled = true
        })

        lifecycleScope.launch {
            val user = viewModel.getUser(args.user.email)
            userLiveData.postValue(user)
        }

        send_button.setOnClickListener {
            val money = moneyEditText.text.toString().toInt()

            lifecycleScope.launch {
                val result = viewModel.sendMoney(userLiveData.value!!, money)

                launch(Dispatchers.Main) {
                    if (result) {
                        toast("송금 완료")
                    } else {
                        toast("송금 실패")
                    }
                    dismiss()
                }
            }
        }

    }

}
