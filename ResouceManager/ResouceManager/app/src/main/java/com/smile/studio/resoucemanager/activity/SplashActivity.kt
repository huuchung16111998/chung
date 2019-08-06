package com.smile.studio.resoucemanager.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.smile.studio.libsmilestudio.utils.AndroidDeviceInfo
import com.smile.studio.resoucemanager.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)
        tv_version_app.text = getString(R.string.version_application, getString(R.string.app_name), AndroidDeviceInfo.getAppVersionName(this))
        Handler().postDelayed(Runnable {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1500)
    }
}
