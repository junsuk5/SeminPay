package kr.or.semin.seminpay.ui


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.fragment.findNavController
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import kotlinx.android.synthetic.main.fragment_main.*
import kr.or.semin.seminpay.R
import kr.or.semin.seminpay.models.jsonToUser

class MainFragment : Fragment() {
    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    ////////////////////// 권한 체크 시작
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                zxing_barcode_scanner.decodeContinuous(callback)
            } else {

//                Toast.makeText(this,
//                    "Permissions not granted by the user.",
//                    Toast.LENGTH_SHORT).show()
//                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }
    ////////////////////// 권한 체크 끝


    private lateinit var beepManager: BeepManager
    private var lastText: String? = null

    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null || result.text == lastText) {
                // Prevent duplicate scans
                return
            }


            lastText = result.text


            zxing_barcode_scanner.setStatusText(result.text)

            beepManager.playBeepSoundAndVibrate()


            val user = jsonToUser(result.text)
            val action = MainFragmentDirections.actionMainFragmentToSendMoneyDialogFragment(user)
            findNavController().navigate(action)


            //Added preview of scanned barcode
//            Glide.with(barcode_image).load(
//                result.getBitmapWithResultPoints(Color.YELLOW)
//            ).into(barcode_image)
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val formats = listOf(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)

        zxing_barcode_scanner
            .barcodeView.decoderFactory = DefaultDecoderFactory(formats)

        beepManager = BeepManager(requireActivity())

        if (allPermissionsGranted()) {
            zxing_barcode_scanner.decodeContinuous(callback)
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    override fun onResume() {
        super.onResume()

        zxing_barcode_scanner.resume()
    }

    override fun onPause() {
        super.onPause()

        zxing_barcode_scanner.pause()
    }

    fun pause(view: View) {
        zxing_barcode_scanner.pause()
    }

    fun resume(view: View) {
        zxing_barcode_scanner.resume()
    }

    fun triggerScan(view: View) {
        zxing_barcode_scanner.decodeSingle(callback)
    }

    class ViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return ReceiveMoneyFragment()
        }

        override fun getCount() = 3
    }
}
