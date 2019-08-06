package com.smile.studio.resoucemanager.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
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
import com.smile.studio.resoucemanager.network.model.Profile
import com.smile.studio.resoucemanager.network.model.Resouce
import com.smile.studio.resoucemanager.network.request.ProfileRequest
import com.smile.studio.resoucemanager.view.ProfileDialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_resouce_by_owner.*

/**
 * A placeholder fragment containing a simple view.
 */
class ResouceByOwnerFragment : BaseFragment(), View.OnClickListener {

    val barcodeEncoder = BarcodeEncoder()
    var resouce: Resouce? = null
    var profile: Profile? = null
    var companyAdapter: CompanyAdapter? = null
    var resouceTypeTypeAdapter: ResouceTypeAdapter? = null
    var statusAdapter: StatusAdapter? = null

    companion object {

        fun newInstance(resouce: Resouce): ResouceByOwnerFragment {
            val bundle = Bundle()
            bundle.putParcelable(Resouce::class.java.simpleName, resouce)
            val fragment = ResouceByOwnerFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_resouce_by_owner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            resouce = arguments?.getParcelable(Resouce::class.java.simpleName)
            val bitmap = barcodeEncoder.encodeBitmap(resouce?.qrcode, BarcodeFormat.QR_CODE, Utils.convertDpToPixel(activity, 400), Utils.convertDpToPixel(activity, 400))
            img_qrcode.setImageBitmap(bitmap)
            tv_uid.text = Utils.fromHtml(getString(R.string.code_member, resouce?.uid.toString()))
            tv_qrcode.text = resouce?.qrcode
            tv_code.text = resouce?.code
            tv_name.text = resouce?.name
            edt_detail.setText(resouce?.detail)
            edt_serial.setText(resouce?.serial)
            edt_tech_value.setText(resouce?.tech_value)
            edt_price.setText(GlobalApp().getInstance().decimalFormatMoney.format(resouce?.purchase_price))
            edt_madein.setText(resouce?.made_by)
            try {
                tv_purchase_date.setText(GlobalApp().getInstance().dateFormat1.format(GlobalApp().getInstance().dateFormat2.parse(resouce?.purchase_date)))
            } catch (e: Exception) {
                Debug.e("--- Lỗi định dạng ${e.message}")
            }
            try {
                tv_delivery_date.setText(GlobalApp().getInstance().dateFormat1.format(GlobalApp().getInstance().dateFormat2.parse(resouce?.delivery_date)))
            } catch (e: Exception) {
                Debug.e("--- Lỗi định dạng ${e.message}")
            }
            getProfileInfo(resouce?.uid!!)

            companyAdapter = CompanyAdapter(activity!!, ArrayList())
            companyAdapter?.addAll(GlobalApp().getInstance().mDataCompany)
            spinner_company.adapter = companyAdapter
            companyAdapter?.mData?.forEachIndexed { index, company ->
                if (company.value == resouce?.id_type_asset) {
                    spinner_company.setSelection(index)
                    return@forEachIndexed
                }
            }

            resouceTypeTypeAdapter = ResouceTypeAdapter(activity!!, ArrayList())
            resouceTypeTypeAdapter?.addAll(GlobalApp().getInstance().mDataResouceType)
            spinner_resouce_type.adapter = resouceTypeTypeAdapter
            resouceTypeTypeAdapter?.mData?.forEachIndexed { index, resouceType ->
                if (resouceType.value == resouce?.id_type_asset) {
                    spinner_resouce_type.setSelection(index)
                    return@forEachIndexed
                }
            }

            statusAdapter = StatusAdapter(activity!!, ArrayList())
            statusAdapter?.addAll(GlobalApp().getInstance().mDataStatus)
            spinner_resouce_status.adapter = statusAdapter
            statusAdapter?.mData?.forEachIndexed { index, status ->
                if (status.value == resouce?.status) {
                    spinner_resouce_status.setSelection(index)
                    return@forEachIndexed
                }
            }
            btn_edit.setOnClickListener(this)
            btn_profile.setOnClickListener(this)
        }
    }

    private fun getProfileInfo(userID: Int) {
        (activity as MainActivity).showProgressDialog()
        val profileRequest = ProfileRequest(userID)
        val callResponse = GlobalApp().getInstance().baseURL(API.HOST).create(ResouceManager::class.java).getProfileInfo(profileRequest)
        val subscribe = callResponse.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doFinally {
            (activity as MainActivity).dismissProgressDialog()
            btn_profile.isEnabled = profile != null
        }.subscribe({
            if (it?.code == 10) {
                // TODO
                profile = it.data
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
                getProfileInfo(userID)
            }
            pDialog.show()
        })
        compositeDisposable.add(subscribe)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_edit -> {
                (activity as MainActivity).tv_title_bar.setText(R.string.title_activity_edit)
                (activity as MainActivity).onChangeFragmentClean(ResouceEditFragment.newInstance(resouce!!), "")
            }
            R.id.btn_profile -> {
                ProfileDialogFragment.newInstance(profile!!).show(fragmentManager, ProfileDialogFragment::class.java.simpleName)
            }
        }
    }
}
