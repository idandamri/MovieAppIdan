package com.scores.mymovieapp.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.budiyev.android.codescanner.*
import com.scores.mymovieapp.R
import com.scores.mymovieapp.utilities.Utils

class QrCodeReaderActivity : AppCompatActivity() {

    private var codeScanner: CodeScanner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qr_code_reader_layout)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        if(Utils.checkAndroidVersionAndPermissionIsGranted()){

            val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

            codeScanner = CodeScanner(this, scannerView)

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
                runOnUiThread {
                    try {
                        val resultIntent = Intent()
                        resultIntent.putExtra(Utils.DATA_FROM_SCAN, it.text)
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            codeScanner?.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
                runOnUiThread {
                    Toast.makeText(this, "Camera initialization error: ${it.message}",
                        Toast.LENGTH_LONG).show()
                }
            }

            scannerView.setOnClickListener {
                codeScanner?.startPreview()
            }
        }
        else {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner?.startPreview()
    }

    override fun onPause() {
        codeScanner?.releaseResources()
        super.onPause()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}