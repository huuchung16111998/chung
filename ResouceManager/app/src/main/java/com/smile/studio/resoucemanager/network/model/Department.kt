package com.smile.studio.resoucemanager.network.model

import com.google.gson.annotations.SerializedName

class Department(
        @SerializedName("ParentID") val parentID: Int? = null,
        @SerializedName("Descr") val descr: String? = null,
        @SerializedName("Type") val type: Int? = null,
        @SerializedName("Level") val Level: Int? = null
) : BaseModel() {
}