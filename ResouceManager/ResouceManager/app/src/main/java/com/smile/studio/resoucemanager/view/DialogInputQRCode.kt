package com.smile.studio.resoucemanager.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.smile.studio.resoucemanager.R

class DialogInputQRCode : BottomSheetDialogFragment() {

    companion object {

        fun newInstance(): DialogInputQRCode {
            val fragment = DialogInputQRCode()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_fragment_input_qrcode, container, false)
    }

}