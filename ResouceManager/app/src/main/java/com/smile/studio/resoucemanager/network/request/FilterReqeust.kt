package com.smile.studio.resoucemanager.network.request

import com.google.gson.annotations.SerializedName
import com.smile.studio.libsmilestudio.utils.Debug

class FilterReqeust(
        @SerializedName("idMember") var uid: Int? = 0,
        @SerializedName("idDepartment") var idDepartment: Int? = 0,
        @SerializedName("AssetType") var assetType: Int? = 0,
        @SerializedName("Status") var status: Int? = 0,
        @SerializedName("keyword") var keyword: String? = "",
        @SerializedName("page") var page: Int? = 1,
        @SerializedName("pageSize") var pageSize: Int? = 20,
        @SerializedName("company") var company: Int? = 0,
        @SerializedName("mapping") var mapping: Int? = -1
) {
    fun trace() {
        Debug.e("idMember: $uid\nidDepartment: $idDepartment\nmapping: $mapping\npage: $page\npageSize: $pageSize")
    }

    enum class TypeMapping(val value: Int) {
        ALL(-1), NotMapping(0), Mapping(1)
    }
}