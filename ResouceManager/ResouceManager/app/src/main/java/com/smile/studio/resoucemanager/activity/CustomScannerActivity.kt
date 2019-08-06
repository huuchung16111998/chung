package com.smile.studio.resoucemanager.activity

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.smile.studio.libsmilestudio.utils.Utils
import com.smile.studio.resoucemanager.R
import com.smile.studio.resoucemanager.view.DialogInputQRCode
import kotlinx.android.synthetic.main.activity_custom_scanner.*
import kotlinx.android.synthetic.main.custom_barcode_scanner.*

class CustomScannerActivity : BaseActivity(), View.OnClickListener, DecoratedBarcodeView.TorchListener {

    var capture: CaptureManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_custom_scanner)
        tv_not_scanner.text = Utils.fromHtml(getString(R.string.title_message_not_scanner_qrcode))
        capture = CaptureManager(this, zxing_barcode_scanner)
        capture?.initializeFromIntent(intent, savedInstanceState)
        capture?.decode()
        btn_back.setOnClickListener(this)
        tv_not_scanner.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        capture?.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        capture?.onSaveInstanceState(outState)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return zxing_barcode_scanner.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_back -> {
                onBackPressed()
            }
            R.id.tv_not_scanner -> {
                val dialog = DialogInputQRCode.newInstance()
                dialog.show(supportFragmentManager, DialogInputQRCode::class.java.simpleName)

            }
        }
    }

    override fun onTorchOn() {

    }

    override fun onTorchOff() {
    }
}