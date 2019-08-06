package com.smile.studio.resoucemanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.smile.studio.libsmilestudio.security.AES128
import com.smile.studio.libsmilestudio.security.MD5
import com.smile.studio.libsmilestudio.utils.AndroidUtils
import com.smile.studio.libsmilestudio.utils.Debug
import com.smile.studio.resoucemanager.R

class QRCodeAdapter(val mContext: Context, val columns: Int, val mData: ArrayList<String>) : RecyclerView.Adapter<QRCodeAdapter.ViewHolder>() {

    val barcodeEncoder = BarcodeEncoder()

    fun addAll(mData: ArrayList<String>) {
        this.mData.addAll(mData)
        notifyDataSetChanged()
    }

    fun clear() {
        this.mData.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(container: ViewGroup, typeView: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.custom_item_print_qrcode, container, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val plaintEndCrypt = AES128.encrypt(mData.get(position), MD5.encrypt(AndroidUtils.getSHA1(mContext), 2))
            Debug.e("--- plaintEndCrypt: $plaintEndCrypt")
            val bitmap = barcodeEncoder.encodeBitmap(plaintEndCrypt, BarcodeFormat.QR_CODE, 400, 400)
            holder.thumb.setImageBitmap(bitmap)
            holder.tv_title.text = mData.get(position)
        } catch (e: Exception) {

        }
    }

    inner class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val thumb = view.findViewById<ImageView>(R.id.thumb)
        val tv_title = view.findViewById<TextView>(R.id.tv_title)
    }
}