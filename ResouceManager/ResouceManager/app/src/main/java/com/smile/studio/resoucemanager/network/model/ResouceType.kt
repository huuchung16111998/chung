package com.smile.studio.resoucemanager.network.model

import com.google.gson.annotations.SerializedName
import com.smile.studio.libsmilestudio.utils.Debug

class ResouceType(
        @SerializedName("sign") val sign: String? = null,
        @SerializedName("order") val order: Int? = 0,
        @SerializedName("type") val type: String? = null,
        @SerializedName("value") val value: Int? = 0,
        @SerializedName("parent_id") val parentID: Int? = 0
) : BaseModel() {
    override fun trace() {
        super.trace()
        Debug.e("sign: $sign\norder: $order\ntype: $type\nvalue: $value\nparent_id: $parentID")
    }
}