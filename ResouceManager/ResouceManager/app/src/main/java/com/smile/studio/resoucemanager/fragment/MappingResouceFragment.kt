package com.smile.studio.resoucemanager.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.smile.studio.libsmilestudio.recyclerviewer.EndlessRecyclerOnScrollListener
import com.smile.studio.libsmilestudio.recyclerviewer.OnItemClickListenerRecyclerView
import com.smile.studio.libsmilestudio.security.AES128
import com.smile.studio.libsmilestudio.security.MD5
import com.smile.studio.libsmilestudio.utils.AndroidUtils
import com.smile.studio.libsmilestudio.utils.Debug
import com.smile.studio.libsmilestudio.utils.Utils
import com.smile.studio.resoucemanager.R
import com.smile.studio.resoucemanager.activity.MainActivity
import com.smile.studio.resoucemanager.adapter.ResouceMappingAdapter
import com.smile.studio.resoucemanager.model.GlobalApp
import com.smile.studio.resoucemanager.network.face.ResouceManager
import com.smile.studio.resoucemanager.network.model.API
import com.smile.studio.resoucemanager.network.model.Resouce
import com.smile.studio.resoucemanager.network.request.FilterReqeust
import com.smile.studio.resoucemanager.view.FilterSettingDialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_mapping.*
import kotlinx.android.synthetic.main.fragment_resouce_by_owner.img_qrcode
import kotlinx.android.synthetic.main.fragment_resouce_by_owner.tv_qrcode

class MappingResouceFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    var qrCode: String? = null
    val barcodeEncoder = BarcodeEncoder()
    var filterRequest: FilterReqeust? = null
    var adapter: ResouceMappingAdapter? = null
    var layoutManager: LinearLayoutManager? = null
    var endlessRecyclerOnScrollListener: EndlessRecyclerOnScrollListener? = null

    companion object {

        val QRCODE = "qrcode"

        fun newInstance(qrCode: String): MappingResouceFragment {
            val bundle = Bundle()
            bundle.putString(QRCODE, qrCode)
            val fragment = MappingResouceFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mapping, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        qrCode = arguments?.getString(QRCODE)
        val plaintDeCrypt = AES128.decrypt(qrCode, MD5.encrypt(AndroidUtils.getSHA1(activity), 2))
        val bitmap = barcodeEncoder.encodeBitmap(plaintDeCrypt, BarcodeFormat.QR_CODE, Utils.convertDpToPixel(activity, 400), Utils.convertDpToPixel(activity, 400))
        img_qrcode.setImageBitmap(bitmap)
        tv_qrcode.text = plaintDeCrypt
        filterRequest = FilterReqeust()
        layoutManager = LinearLayoutManager(activity!!)
        recyclerView.layoutManager = layoutManager
        adapter = ResouceMappingAdapter(activity!!, ArrayList())
        adapter?.onItemClick = object : OnItemClickListenerRecyclerView {
            override fun onClick(view: View?, position: Int) {
                val alertDialog: AlertDialog.Builder? = AlertDialog.Builder(activity)
                alertDialog?.setMessage(R.string.message_question_qrcode)
                alertDialog?.setNegativeButton(R.string.title_button_mapping) { dialog: DialogInterface, which: Int ->
                    onAtionMapping(adapter?.mData?.get(position)!!)
                }
                alertDialog?.setPositiveButton(R.string.title_button_cancel) { dialog: DialogInterface, which: Int ->
                }
                alertDialog?.show()
            }

            override fun onLongClick(view: View?, position: Int) {
            }

        }
        recyclerView.adapter = adapter!!
        recyclerView.setHasFixedSize(true)
        loadData(filterRequest!!)
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.isRefreshing = false
        endlessRecyclerOnScrollListener = object : EndlessRecyclerOnScrollListener(layoutManager, page) {
            override fun onLoadMore(mPage: Int) {
                page = mPage
                loadData(filterRequest!!)
            }
        }
        recyclerView.addOnScrollListener(endlessRecyclerOnScrollListener!!)
        groups_qrcode.setOnClickListener(this)
    }

    override fun onPause() {
        super.onPause()
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.isRefreshing = false
            swipeRefreshLayout.destroyDrawingCache()
            swipeRefreshLayout.clearAnimation()
        }
    }

    override fun onRefresh() {
        if (swipeRefreshLayout.isRefreshing) {
            page = 1
            endlessRecyclerOnScrollListener?.resetState()
            adapter?.clear()
            loadData(filterRequest!!)
            Handler().postDelayed(Runnable {
                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.isRefreshing = false
            }, TIME_DELAY)
        }
    }

    fun onAtionMapping(resouce: Resouce) {
        val plaintDeCrypt = AES128.decrypt(qrCode, MD5.encrypt(AndroidUtils.getSHA1(activity), 2))
        resouce.qrcode = plaintDeCrypt
        val callResponse = GlobalApp().getInstance().baseURL(API.HOST).create(ResouceManager::class.java).insertUpdateResouce(resouce)
        val subscribe = callResponse.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doFinally {
        }.subscribe({
            if (it?.code == 10) {
                // TODO
                adapter?.removeItem(resouce)
                Debug.showAlert(activity, "Cập nhật thông tin thành công")
            }
        }, {
            Debug.e("--- Lỗi: ${it.message}")
            val pDialog = SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
            pDialog.titleText = "Oops..."
            pDialog.contentText = getString(R.string.message_error_connect_internet)
            pDialog.confirmText = getString(R.string.retry)
            pDialog.setConfirmClickListener { sweetAlertDialog ->
                sweetAlertDialog.dismiss()
                //TODO
                onAtionMapping(resouce)
            }
            pDialog.show()
        })
        compositeDisposable.add(subscribe)
    }

    private fun loadData(filterReqeust: FilterReqeust) {
        (activity as MainActivity).showProgressDialog()
        val callResponse = GlobalApp().getInstance().baseURL(API.HOST).create(ResouceManager::class.java).filterResouce(filterReqeust)
        val subscribe = callResponse.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doFinally {
            (activity as MainActivity).dismissProgressDialog()
        }.subscribe({
            if (it?.code == 10) {
                // TODO
                adapter?.addAll(it.data!!)
            }
        }, {
            Debug.e("--- Lỗi: ${it.message}")
            val pDialog = SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
            pDialog.titleText = "Oops..."
            pDialog.contentText = getString(R.string.message_error_connect_internet)
            pDialog.confirmText = getString(R.string.retry)
            pDialog.setConfirmClickListener { sweetAlertDialog ->
                sweetAlertDialog.dismiss()
                //TODO
                loadData(filterReqeust)
            }
            pDialog.show()
        })
        compositeDisposable.add(subscribe)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.groups_qrcode -> {
                val dialog = FilterSettingDialogFragment.newInstance()
                dialog.iAction = object : FilterSettingDialogFragment.IAction {
                    override fun callback(mFilter: FilterReqeust) {
                        mFilter.page = page
                        mFilter.pageSize = pagesize
                        mFilter.mapping = FilterReqeust.TypeMapping.Mapping.value
                        onActionFilter(mFilter)
                    }

                }
                dialog.show(fragmentManager, FilterSettingDialogFragment::class.java.simpleName)
            }
        }
    }

    private fun onActionFilter(mFilter: FilterReqeust) {
        (activity as MainActivity).showProgressDialog()
        val callResponse = GlobalApp().getInstance().baseURL(API.HOST).create(ResouceManager::class.java).filterResouce(mFilter)
        val subscribe = callResponse.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doFinally {
            (activity as MainActivity).dismissProgressDialog()
        }.subscribe({
            if (it?.code == 10) {
                // TODO
            }
        }, {
            Debug.e("--- Lỗi: ${it.message}")
            val pDialog = SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
            pDialog.titleText = "Oops..."
            pDialog.contentText = getString(R.string.message_error_connect_internet)
            pDialog.confirmText = getString(R.string.retry)
            pDialog.setConfirmClickListener { sweetAlertDialog ->
                sweetAlertDialog.dismiss()
                //TODO
                onActionFilter(mFilter)
            }
            pDialog.show()
        })
        compositeDisposable.add(subscribe)
    }


}