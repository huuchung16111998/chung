package com.smile.studio.resoucemanager.view

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy.ALL
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.smile.studio.resoucemanager.R
import kotlinx.android.synthetic.main.dialog_fragment_profile.thumb
import kotlinx.android.synthetic.main.dialog_fragment_setting_paint.*


class SettingPaintDialogFragment : BottomSheetDialogFragment(), View.OnClickListener {

    var iAction: IAction? = null

    companion object {

        fun newInstance(): SettingPaintDialogFragment {
            val fragment = SettingPaintDialogFragment()
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_fragment_setting_paint, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val placeholder = BitmapFactory.decodeResource(view.resources, R.drawable.ic_printer)
        val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(view.resources!!, placeholder)
        circularBitmapDrawable.isCircular = true
        Glide.with(this).load(R.drawable.ic_printer).apply(RequestOptions.diskCacheStrategyOf(ALL).format(DecodeFormat.PREFER_ARGB_8888)).error(circularBitmapDrawable).placeholder(circularBitmapDrawable).circleCrop().thumbnail(0.5f).into(thumb)
        btn_print.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        var page = 1
        if (!TextUtils.isEmpty(edt_start_index.text.toString())) {
            page = edt_start_index.text.toString().toInt()
            page = if (page < 1) 1 else page
        }
        var pageSize = 50
        if (!TextUtils.isEmpty(edt_total.text.toString())) {
            pageSize = edt_total.text.toString().toInt()
            pageSize = if (pageSize < 50) 50 else pageSize
        }
        var columns = 7
        if (!TextUtils.isEmpty(edt_columns.text.toString())) {
            columns = edt_columns.text.toString().toInt()
            columns = if (pageSize < 7) 7 else columns
        }
        var identification = "TS"
        if (!TextUtils.isEmpty(edt_identification.text.toString())) {
            identification = edt_identification.text.toString()
            identification = if (!TextUtils.isEmpty(identification)) identification else "TS"
        }
        iAction?.callBack(page, pageSize, columns, identification)
    }

    interface IAction {
        fun callBack(page: Int, pageSize: Int, columns: Int, identification: String)
    }

}