package com.smile.studio.resoucemanager.activity

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.zxing.integration.android.IntentIntegrator
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.smile.studio.libsmilestudio.security.AES128
import com.smile.studio.libsmilestudio.security.MD5
import com.smile.studio.libsmilestudio.utils.AndroidUtils
import com.smile.studio.libsmilestudio.utils.Debug
import com.smile.studio.resoucemanager.R
import com.smile.studio.resoucemanager.fragment.MappingResouceFragment
import com.smile.studio.resoucemanager.fragment.PrintQRCodeFragment
import com.smile.studio.resoucemanager.fragment.ResouceByOwnerFragment
import com.smile.studio.resoucemanager.fragment.ResouceEditFragment
import com.smile.studio.resoucemanager.model.GlobalApp
import com.smile.studio.resoucemanager.network.face.ResouceManager
import com.smile.studio.resoucemanager.network.model.API
import com.smile.studio.resoucemanager.network.model.Profile
import com.smile.studio.resoucemanager.network.request.QRCodeRequest
import com.smile.studio.resoucemanager.view.SettingPaintDialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        Log.e("TAG", "sha1: ${AndroidUtils.getSHA1(this@MainActivity)}\nKeyEncrypt: ${MD5.encrypt(AndroidUtils.getSHA1(this@MainActivity), 2)}")
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_qrcode)
        getSettingInfo(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    fun getSettingInfo(isReload: Boolean) {
        showProgressDialog()
        val callResponse = GlobalApp().getInstance().baseURL(API.HOST).create(ResouceManager::class.java).getSetting()
        val subscribe = callResponse.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doFinally {
            dismissProgressDialog()
        }.subscribe({
            if (it?.code == 10) {
                // TODO
                GlobalApp().getInstance().mDataCompany.clear()
                GlobalApp().getInstance().mDataCompany.addAll(it.data?.companys!!)
                GlobalApp().getInstance().mDataDepartment.clear()
                GlobalApp().getInstance().mDataDepartment.addAll(it.data.department!!)
                GlobalApp().getInstance().mDataProfile.clear()
                GlobalApp().getInstance().mDataProfile.add(0, Profile(id = -1, name = "Không xác định"))
                GlobalApp().getInstance().mDataProfile.add(1, Profile(id = 0, name = "Lưu kho"))
                GlobalApp().getInstance().mDataProfile.addAll(it.data.staffs!!)
                GlobalApp().getInstance().mDataStatus.clear()
                GlobalApp().getInstance().mDataStatus.addAll(it.data.status!!)
                GlobalApp().getInstance().mDataResouceType.clear()
                GlobalApp().getInstance().mDataResouceType.addAll(it.data.resouceTypes!!)
                if (isReload) {
                    onReloadFragment()
                }
            }
        }, {
            Debug.e("--- Lỗi: ${it.message}")
            val pDialog = SweetAlertDialog(this@MainActivity, SweetAlertDialog.ERROR_TYPE)
            pDialog.titleText = "Oops..."
            pDialog.contentText = getString(R.string.message_error_connect_internet)
            pDialog.confirmText = getString(R.string.retry)
            pDialog.setConfirmClickListener { sweetAlertDialog ->
                sweetAlertDialog.dismiss()
                //TODO
                getSettingInfo(isReload)
            }
            pDialog.show()
        })
        compositeDisposable.add(subscribe)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                TedPermission.with(this).setPermissionListener(object : PermissionListener {
                    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {

                    }

                    override fun onPermissionGranted() {
                        //TODO action have permission
                        val integrator = IntentIntegrator(this@MainActivity)
                        integrator.setOrientationLocked(true)
                        integrator.captureActivity = CustomScannerActivity::class.java
                        integrator.setCameraId(0)
                        integrator.setBeepEnabled(true)
                        integrator.setBarcodeImageEnabled(true)
                        integrator.initiateScan()
                    }

                })
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.CAMERA).check()
            }
            R.id.action_print -> {
                TedPermission.with(this@MainActivity).setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        val dialog = SettingPaintDialogFragment.newInstance()
                        dialog.iAction = object : SettingPaintDialogFragment.IAction {
                            override fun callBack(page: Int, pageSize: Int, columns: Int, identification: String) {
                                tv_title_bar.setText(R.string.title_activity_print)
                                dialog.dismiss()
                                onChangeFragmentClean(
                                        PrintQRCodeFragment.newInstance(
                                                page,
                                                pageSize,
                                                columns,
                                                identification
                                        ), PrintQRCodeFragment::class.java.simpleName
                                )
                            }
                        }
                        dialog.show(supportFragmentManager, SettingPaintDialogFragment::class.java.simpleName)
                    }

                    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {

                    }
                })
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        ).check()

            }
            R.id.action_reload -> {
                getSettingInfo(true)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkQRInfo(qrCode: String) {
        val plaintDeCrypt = AES128.decrypt(qrCode, MD5.encrypt(AndroidUtils.getSHA1(this@MainActivity), 2))
        val body = QRCodeRequest(plaintDeCrypt)
        showProgressDialog()
        val callResponse = GlobalApp().getInstance().baseURL(API.HOST).create(ResouceManager::class.java).getInfo(body)
        val subscribe = callResponse.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doFinally {
            dismissProgressDialog()
        }.subscribe({
            when (it.code) {
                10 -> {
                    val resouce = it.data
                    if (resouce != null) {
                        tv_title_bar.setText(R.string.title_activity_info)
                        onChangeFragmentClean(ResouceByOwnerFragment.newInstance(resouce), "")
                    } else {
                        showDialogChoice(qrCode)
                    }
                }
                21 -> {
                    showDialogChoice(qrCode)
                }
            }
        }, {
            Debug.e("--- Lỗi: ${it.message}")
            val pDialog = SweetAlertDialog(this@MainActivity, SweetAlertDialog.ERROR_TYPE)
            pDialog.titleText = "Oops..."
            pDialog.contentText = getString(R.string.message_error_connect_internet)
            pDialog.confirmText = getString(R.string.retry)
            pDialog.setConfirmClickListener { sweetAlertDialog ->
                sweetAlertDialog.dismiss()
                //TODO
                checkQRInfo(qrCode)
            }
            pDialog.show()
        })
        compositeDisposable.add(subscribe)
    }

    private fun showDialogChoice(qrCode: String) {
        val alertDialog: AlertDialog.Builder? = AlertDialog.Builder(this@MainActivity)
        alertDialog?.setMessage(R.string.message_question_qrcode)
        alertDialog?.setNegativeButton(R.string.title_button_create) { dialog: DialogInterface, which: Int ->
            tv_title_bar.setText(R.string.title_activity_create_info)
            onChangeFragmentClean(ResouceEditFragment.newInstance(qrCode), "")
        }
        alertDialog?.setPositiveButton(R.string.title_button_mapping) { dialog: DialogInterface, which: Int ->
            tv_title_bar.setText(R.string.title_activity_mapping_resource)
            onChangeFragmentClean(MappingResouceFragment.newInstance(qrCode), "")
        }
        alertDialog?.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                Debug.getBundleToString(data?.extras)
                val content = result.contents
                if (!TextUtils.isEmpty(content)) {
                    checkQRInfo(content)
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        } catch (e: Exception) {

        }
    }

}
