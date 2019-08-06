package com.smile.studio.resoucemanager.activity

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.smile.studio.resoucemanager.R
import io.reactivex.disposables.CompositeDisposable
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

open class BaseActivity : AppCompatActivity() {

    var progressDialog: ProgressDialog? = null
    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    fun onChangeFragment(fragment: androidx.fragment.app.Fragment, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()
        if (!TextUtils.isEmpty(tag)) {
            transaction.replace(R.id.container, fragment, tag).addToBackStack(tag)
        } else {
            transaction.replace(R.id.container, fragment)
        }
        try {
            transaction.commit()
        } catch (e: IllegalStateException) {
            transaction.commitAllowingStateLoss()
        }
    }

    fun onChangeFragmentClean(fragment: androidx.fragment.app.Fragment, tag: String) {
        supportFragmentManager.popBackStack(tag, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
        val transaction = supportFragmentManager.beginTransaction()
        if (!TextUtils.isEmpty(tag)) {
            transaction.replace(R.id.container, fragment, tag).addToBackStack(tag)
        } else {
            transaction.replace(R.id.container, fragment)
        }
        try {
            transaction.commit()
        } catch (e: IllegalStateException) {
            transaction.commitAllowingStateLoss()
        }
    }

    fun onReloadFragment() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        if (fragment != null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            if (Build.VERSION.SDK_INT >= 26) {
                fragmentTransaction.setReorderingAllowed(false)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                fragmentTransaction.detach(fragment).commitNow()
                fragmentTransaction.attach(fragment).commitNow()
            } else {
                fragmentTransaction.detach(fragment).attach(fragment).commit()
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
        System.gc()
        Runtime.getRuntime().gc()
    }

    fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(this, null, null, true)
            progressDialog?.setContentView(R.layout.custom_progress_dialog)
            progressDialog?.isIndeterminate = true
            progressDialog?.setCancelable(false)
            progressDialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                val drawable = ProgressBar(this).indeterminateDrawable.mutate()
                drawable.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_IN)
                progressDialog?.setIndeterminateDrawable(drawable)
            }
        }
        if (progressDialog != null && !progressDialog?.isShowing!!) {
            progressDialog?.show()
        }
    }

    fun dismissProgressDialog() {
        if (progressDialog != null && progressDialog?.isShowing!!) {
            progressDialog?.dismiss()
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).clearMemory()
        Glide.get(this).clearDiskCache()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Glide.get(this).trimMemory(level)
    }
}
