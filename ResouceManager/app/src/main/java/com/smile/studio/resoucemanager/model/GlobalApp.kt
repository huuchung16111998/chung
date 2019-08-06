package com.smile.studio.resoucemanager.model

import android.content.Context
import com.google.gson.Gson
import com.smile.studio.libsmilestudio.utils.Debug
import com.smile.studio.resoucemanager.BuildConfig
import com.smile.studio.resoucemanager.network.model.*
import okhttp3.Interceptor
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class GlobalApp {

    var retrofit: Retrofit? = null
    var logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Debug.e("\n" + message) })
    var client: OkHttpClient
    var context: Context? = null
    var decimalFormat = DecimalFormat("00")
    var decimalFormatMoney = DecimalFormat("###,###,###,###,###")
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    val dateFormat1 = SimpleDateFormat("dd/MM/yyyy")
    val dateFormat2 = SimpleDateFormat("yyyy-MM-dd")
    val gson = Gson()
    var mDataCompany = ArrayList<Company>()
    var mDataDepartment = ArrayList<Department>()
    var mDataProfile = ArrayList<Profile>()
    var mDataStatus = ArrayList<Status>()
    var mDataResouceType = ArrayList<ResouceType>()

    companion object {
        private var instance: GlobalApp? = null
    }

    fun getInstance(): GlobalApp {
        if (instance == null) {
            synchronized(GlobalApp::class.java) {
                instance = GlobalApp()

            }
        }
        return instance!!
    }

    init {
        val value = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        logging.level = value
        val interceptor = Interceptor { chain ->
            val request = chain.request()
            request.newBuilder()
                    .header("Content-ResouceType", "application/x-www-form-urlencoded")
                    .header("Accept", "application/json")
                    .method(request.method(), request.body()).build()
            val response = chain.proceed(request)
            return@Interceptor response
        }
        client = OkHttpClient().newBuilder().cookieJar(JavaNetCookieJar(CookieManager())).addInterceptor(interceptor).addNetworkInterceptor(logging).connectTimeout(3000, TimeUnit.SECONDS).writeTimeout(3000, TimeUnit.SECONDS).readTimeout(3000, TimeUnit.SECONDS).build()
    }

    fun baseURL(url: String): Retrofit {
        return Retrofit.Builder().baseUrl(url).client(client).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build()
    }

}