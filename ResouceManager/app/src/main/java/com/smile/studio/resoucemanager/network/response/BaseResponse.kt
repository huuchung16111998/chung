package com.smile.studio.resoucemanager.network.response

import com.google.gson.annotations.SerializedName
import com.smile.studio.libsmilestudio.utils.Debug

class BaseResponse<T>(

        @SerializedName("code", alternate = arrayOf("res_code"))
        val code: Int? = 0,
        @SerializedName("message")
        val message: String? = null,
        @SerializedName("data")
        val data: T? = null
) {

    fun trace() {
        Debug.e("code: $code\nmessage: $message")
    }

}