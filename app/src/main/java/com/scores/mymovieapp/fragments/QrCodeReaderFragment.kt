package com.scores.mymovieapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.scores.mymovieapp.R
import com.scores.mymovieapp.utilities.Utils

class QrCodeReaderFragment : Fragment(), View.OnClickListener  {

    private var codeScanner: CodeScanner? = null
    private lateinit var backIv: ImageView

    override fun onClick(v: View?) {
        try {
            if(v?.id == R.id.details_back_iv){
                activity?.onBackPressed()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val DATA_TAG: String = "movie_data"

        fun newInstance(): QrCodeReaderFragment {
            val fragment = QrCodeReaderFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.qr_code_reader_layout, container, false)
        backIv = view.findViewById(R.id.details_back_iv)!!
        backIv.setOnClickListener(this)
        if(Utils.checkAndroidVersionAndPermissionIsGranted()){

            val scannerView = view!!.findViewById<CodeScannerView>(R.id.scanner_view)

            codeScanner = CodeScanner(Utils.getContext(), scannerView)

            // Parameters (default values)
            codeScanner?.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
            codeScanner?.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
            // ex. listOf(BarcodeFormat.QR_CODE)
            codeScanner?.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
            codeScanner?.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
            codeScanner?.isAutoFocusEnabled = true // Whether to enable auto focus or not
            codeScanner?.isFlashEnabled = false // Whether to enable flash or not

            // Callbacks
            codeScanner?.decodeCallback = DecodeCallback {
                activity?.runOnUiThread {
                    try {
                        if(activity?.intent == null){
                            activity?.intent = Intent()
                        }
                        activity?.intent?.putExtra(Utils.DATA_FROM_SCAN, it.text)
                        activity?.onBackPressed()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            codeScanner?.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
                activity?.runOnUiThread {
                    Toast.makeText(Utils.getContext(), "Camera initialization error: ${it.message}",
                        Toast.LENGTH_LONG).show()
                    activity?.onBackPressed()
                }
            }

            scannerView.setOnClickListener {
                codeScanner?.startPreview()
            }
        }
        else {
            activity?.onBackPressed()
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        codeScanner?.startPreview()
    }

    override fun onPause() {
        super.onPause()
        codeScanner?.releaseResources()
    }
}