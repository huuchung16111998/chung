package com.smile.studio.resoucemanager.network.model

import com.google.gson.annotations.SerializedName
import com.smile.studio.libsmilestudio.utils.Debug
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class Company(

        @SerializedName("parent_id") val parentID: Int? = null,

        @SerializedName("sign") val sign: String? = null,

        @SerializedName("type") val type: String? = null,

        @SerializedName("value") val value: Int? = null,

        @SerializedName("order") val order: Int? = null
) : BaseModel() {
    override fun trace() {
        super.trace()
        Debug.e("sign: $sign\norder: $order\ntype: $type\nvalue: $value\nparent_id: $parentID")
    }
}