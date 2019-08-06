package com.smile.studio.resoucemanager.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.smile.studio.libsmilestudio.security.AES128
import com.smile.studio.libsmilestudio.security.MD5
import com.smile.studio.libsmilestudio.utils.AndroidUtils
import com.smile.studio.libsmilestudio.utils.Debug
import com.smile.studio.libsmilestudio.utils.Utils
import com.smile.studio.resoucemanager.R
import com.smile.studio.resoucemanager.activity.MainActivity
import com.smile.studio.resoucemanager.adapter.CompanyAdapter
import com.smile.studio.resoucemanager.adapter.ResouceTypeAdapter
import com.smile.studio.resoucemanager.adapter.StatusAdapter
import com.smile.studio.resoucemanager.model.GlobalApp
import com.smile.studio.resoucemanager.network.face.ResouceManager
import com.smile.studio.resoucemanager.network.model.API
import com.smile.studio.resoucemanager.network.model.Resouce
import com.smile.studio.resoucemanager.view.ChoiceProfileDialogFragment
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_resouce_by_owner.img_qrcode
import kotlinx.android.synthetic.main.fragment_resouce_by_owner.spinner_resouce_status
import kotlinx.android.synthetic.main.fragment_resouce_by_owner.spinner_resouce_type
import kotlinx.android.synthetic.main.fragment_resouce_by_owner.tv_qrcode
import kotlinx.android.synthetic.main.fragment_resouce_edit.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * A placeholder fragment containing a simple view.
 */
class ResouceEditFragment : BaseFragment(), View.OnClickListener {

    var qrCode: String? = null
    val barcodeEncoder = BarcodeEncoder()
    var resouce: Resouce? = null
    var companyAdapter: CompanyAdapter? = null
    var resouceTypeTypeAdapter: ResouceTypeAdapter? = null
    var statusAdapter: StatusAdapter? = null
    var uid = 0
    var id_type_asset = 0
    var status = 0
    var idCompany = 0
    var buy_date = ""
    var delivery_date = ""

    companion object {

        val QRCODE = "qrcode"

        fun newInstance(qrCode: String): ResouceEditFragment {
            val bundle = Bundle()
            bundle.putString(QRCODE, qrCode)
            val fragment = ResouceEditFragment()
            fragment.arguments = bundle
            return fragment
        }

        fun newInstance(resouce: Resouce): ResouceEditFragment {
            val bundle = Bundle()
            bundle.putParcelable(Resouce::class.simpleName, resouce)
            val fragment = ResouceEditFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_resouce_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        companyAdapter = CompanyAdapter(activity!!, ArrayList())
        companyAdapter?.addAll(GlobalApp().getInstance().mDataCompany)
        spinner_company.adapter = companyAdapter
        spinner_company.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                idCompany = companyAdapter?.mData?.get(position)?.id!!
            }

        }
        resouceTypeTypeAdapter = ResouceTypeAdapter(activity!!, ArrayList())
        resouceTypeTypeAdapter?.addAll(GlobalApp().getInstance().mDataResouceType)
        spinner_resouce_type.adapter = resouceTypeTypeAdapter
        spinner_resouce_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                id_type_asset = resouceTypeTypeAdapter?.mData?.get(position)?.value!!
            }

        }

        statusAdapter = StatusAdapter(activity!!, ArrayList())
        statusAdapter?.addAll(GlobalApp().getInstance().mDataStatus)
        spinner_resouce_status.adapter = statusAdapter
        spinner_resouce_status.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                status = statusAdapter?.mData?.get(position)?.value!!
            }

        }

        if (arguments != null) {
            qrCode = arguments?.getString(QRCODE)
            if (!TextUtils.isEmpty(qrCode)) {
                val bitmap = barcodeEncoder.encodeBitmap(qrCode, BarcodeFormat.QR_CODE, Utils.convertDpToPixel(activity, 400), Utils.convertDpToPixel(activity, 400))
                img_qrcode.setImageBitmap(bitmap)
                val plaintDeCrypt = AES128.decrypt(qrCode, MD5.encrypt(AndroidUtils.getSHA1(activity), 2))
                tv_qrcode.text = plaintDeCrypt
                Debug.e("--- Thêm mới tài sản")

            } else {
                resouce = arguments?.getParcelable<Resouce>(Resouce::class.java.simpleName)
                val bitmap = barcodeEncoder.encodeBitmap(resouce?.qrcode, BarcodeFormat.QR_CODE, Utils.convertDpToPixel(activity, 400), Utils.convertDpToPixel(activity, 400))
                img_qrcode.setImageBitmap(bitmap)
                tv_qrcode.text = resouce?.qrcode
                Debug.e("--- Chỉnh sửa thông tin tài sản")
            }
        }
        tv_profile.setOnClickListener(this)
        tv_purchase_date.setOnClickListener(this)
        tv_delivery_date.setOnClickListener(this)
        btn_save.setOnClickListener(this)
        btn_cancel.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_profile -> {
                val dialog = ChoiceProfileDialogFragment.newInstance()
                dialog.iAction = object : ChoiceProfileDialogFragment.IAction {
                    override fun perform(poision: Int) {
                        dialog.dismiss()
                        uid = dialog.adapter?.mDataFiltered?.get(poision)?.id!!
                        tv_profile.text = dialog.adapter?.mDataFiltered?.get(poision)?.name
                    }
                }
                dialog.show(fragmentManager, ChoiceProfileDialogFragment::class.java.simpleName)
            }
            R.id.tv_purchase_date -> {
                val calendar = Calendar.getInstance()
                if (!TextUtils.isEmpty(buy_date)) {
                    calendar.time = GlobalApp().getInstance().dateFormat2.parse(buy_date)
                } else {
                    calendar.time = GlobalApp().getInstance().dateFormat1.parse("01/06/1990")
                }
                val dpd = DatePickerDialog.newInstance({ view, year, monthOfYear, dayOfMonth ->
                    calendar.set(year, monthOfYear, dayOfMonth)
                    buy_date = GlobalApp().getInstance().dateFormat2.format(calendar.time.time)
                    tv_purchase_date.text = GlobalApp().getInstance().dateFormat1.format(calendar.time.time)
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                dpd.show(activity?.fragmentManager, DatePickerDialog::class.java.simpleName)
            }
            R.id.tv_delivery_date -> {
                val calendar = Calendar.getInstance()
                if (!TextUtils.isEmpty(delivery_date)) {
                    calendar.time = GlobalApp().getInstance().dateFormat2.parse(delivery_date)
                } else {
                    calendar.time = GlobalApp().getInstance().dateFormat1.parse("01/06/1990")
                }
                val dpd = DatePickerDialog.newInstance({ view, year, monthOfYear, dayOfMonth ->
                    calendar.set(year, monthOfYear, dayOfMonth)
                    delivery_date = GlobalApp().getInstance().dateFormat2.format(calendar.time.time)
                    tv_delivery_date.text = GlobalApp().getInstance().dateFormat1.format(calendar.time.time)
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                dpd.show(activity?.fragmentManager, DatePickerDialog::class.java.simpleName)
            }
            else -> {
                var flag = false
                val name = edt_name.text.toString()
                if (TextUtils.isEmpty(name)) {
                    edt_name.error = getString(R.string.message_error_input_name)
                    flag = true
                }
                val detail = edt_detail.text.toString()
                val serial = edt_serial.text.toString()
                val tech = edt_tech_value.text.toString()
                val price = edt_price.text.toString()
                val madein = edt_madein.text.toString()
                buy_date = tv_purchase_date.text.toString()
                if (!TextUtils.isEmpty(buy_date)) {
                    try {
                        buy_date = GlobalApp().getInstance().dateFormat2.format(GlobalApp().getInstance().dateFormat1.parse(buy_date))
                    } catch (e: Exception) {
                        buy_date = ""
                    }
                }
                delivery_date = tv_delivery_date.text.toString()
                if (!TextUtils.isEmpty(delivery_date)) {
                    try {
                        delivery_date = GlobalApp().getInstance().dateFormat2.format(GlobalApp().getInstance().dateFormat1.parse(delivery_date))
                    } catch (e: Exception) {
                        delivery_date = ""
                    }
                }
                if (!flag) {
                    when (v.id) {
                        R.id.btn_save -> {
                            if (!TextUtils.isEmpty(qrCode)) {
                                qrCode = AES128.decrypt(qrCode, MD5.encrypt(AndroidUtils.getSHA1(activity), 2))
                                resouce = Resouce(id = 0, company = idCompany, name = name, code = "", uid = uid, detail = detail, serial = serial, tech_value = tech, purchase_price = price.toLong(), made_by = madein, purchase_date = buy_date, delivery_date = delivery_date, id_type_asset = id_type_asset, qrcode = qrCode!!, status = status)
                                onActionUpdateInsertResouce(resouce!!, false)
                            } else if (resouce != null) {
                                resouce?.code = ""
                                resouce?.name = name
                                resouce?.detail = detail
                                resouce?.serial = serial
                                resouce?.tech_value = tech
                                resouce?.purchase_price = price.toLong()
                                resouce?.made_by = madein
                                resouce?.purchase_date = buy_date
                                resouce?.delivery_date = delivery_date
                                onActionUpdateInsertResouce(resouce!!, true)
                            } else {
                                Debug.showAlert(activity, "Lỗi không thể thao tác")
                            }
                        }
                        R.id.btn_cancel -> {
                            (activity as MainActivity).onBackPressed()
                        }
                    }
                }
            }
        }
    }

    fun onActionUpdateInsertResouce(resouce: Resouce, isUpdate: Boolean) {
        resouce.trace()
        val callResponse = GlobalApp().getInstance().baseURL(API.HOST).create(ResouceManager::class.java).insertUpdateResouce(resouce)
        val subscribe = callResponse.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doFinally {
        }.subscribe({
            if (it?.code == 10) {
                // TODO
                if (isUpdate) {
                    Debug.showAlert(activity, "Cập nhật thông tin thành công")
                } else {
                    Debug.showAlert(activity, "Lưu thông tin thành công")
                }
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
                onActionUpdateInsertResouce(resouce, isUpdate)
            }
            pDialog.show()
        })
        compositeDisposable.add(subscribe)
    }
}
