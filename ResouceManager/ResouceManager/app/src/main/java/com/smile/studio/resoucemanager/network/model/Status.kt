package com.smile.studio.resoucemanager.network.model

import com.google.gson.annotations.SerializedName

class Status(
        @SerializedName("sign") val sign: String? = null,
        @SerializedName("order") val order: Int? = null,
        @SerializedName("type") val type: String? = null,
        @SerializedName("value") val value: Int? = 0,
        @SerializedName("parent_id") val parentID: Int? = null
) : BaseModel() {
}