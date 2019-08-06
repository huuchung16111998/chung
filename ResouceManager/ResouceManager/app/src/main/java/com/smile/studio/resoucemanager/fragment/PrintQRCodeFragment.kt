package com.smile.studio.resoucemanager.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smile.studio.libsmilestudio.utils.Debug
import com.smile.studio.resoucemanager.R
import com.smile.studio.resoucemanager.adapter.QRCodeAdapter
import github.nisrulz.screenshott.ScreenShott
import kotlinx.android.synthetic.main.fragment_print_qrcode.*
import java.text.DecimalFormat

class PrintQRCodeFragment : BaseFragment() {

    var adapter: QRCodeAdapter? = null
    val decimalFormat = DecimalFormat("00000")

    companion object {

        val PAGE = "page"
        val PAGESIZE = "pageSize"
        val COLUMNS = "columns"
        val IDENTIFICATION = "identification"

        fun newInstance(page: Int, pageSize: Int, columns: Int, identification: String): PrintQRCodeFragment {
            val bundle = Bundle()
            bundle.putInt(PAGE, page)
            bundle.putInt(PAGESIZE, pageSize)
            bundle.putInt(COLUMNS, columns)
            bundle.putString(IDENTIFICATION, identification)
            val fragment = PrintQRCodeFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_print_qrcode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            page = arguments?.getInt(PAGE, 1)!!
            val pageSize = (page - 1) + arguments?.getInt(PAGESIZE, 88)!!
            val columns = arguments?.getInt(COLUMNS, 8)!!
            val identification = arguments?.getString(IDENTIFICATION, "ST")
            val layoutManager = androidx.recyclerview.widget.GridLayoutManager(activity, columns)
            recyclerView.layoutManager = layoutManager
            val mData = ArrayList<String>()
            for (value: Int in page..pageSize) {
                try {
                    mData.add(identification + decimalFormat.format(value))
                    Debug.e("--- ${identification + decimalFormat.format(value)}")
                } catch (e: Exception) {
                    Debug.e("--- Lỗi: ${e.message}")
                }
            }
            adapter = QRCodeAdapter(activity!!, columns, mData)
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)
            Handler().postDelayed(Runnable {
                try {
                    val bitmap = ScreenShott.getInstance().takeScreenShotOfJustView(recyclerView)
                    val file = ScreenShott.getInstance().saveScreenshotToPicturesFolder(context, bitmap, "QRCode-${System.currentTimeMillis()}")
                    Debug.e("--- Đường dẫn lưu file: ${file.absolutePath}")
                    Debug.showAlert(activity, "Ghi file thành công")
                } catch (e: Exception) {
                    Debug.e("--- Lỗi: ${e.message}")
                    Debug.showAlert(activity, "${e.message}")
                }
            }, 1500)
        }
    }

}