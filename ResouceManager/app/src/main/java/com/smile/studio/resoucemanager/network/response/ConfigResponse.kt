package com.smile.studio.resoucemanager.network.response

import com.google.gson.annotations.SerializedName
import com.smile.studio.resoucemanager.network.model.*

class ConfigResponse(
        @SerializedName("listStatus") val status: ArrayList<Status>? = null,

        @SerializedName("listDepartment") val department: ArrayList<Department>? = null,

        @SerializedName("listStaff") val staffs: ArrayList<Profile>? = null,

        @SerializedName("listAssetType") val resouceTypes: ArrayList<ResouceType>? = null,

        @SerializedName("listCompany") val companys: ArrayList<Company>? = null
) {
    fun trace() {
        if (!status?.isEmpty()!!) {
            status.forEach {
                it.trace()
            }
        }
        if (!department?.isEmpty()!!) {
            department.forEach {
                it.trace()
            }
        }
        if (!staffs?.isEmpty()!!) {
            staffs.forEach {
                it.trace()
            }
        }
        if (!resouceTypes?.isEmpty()!!) {
            resouceTypes.forEach {
                it.trace()
            }
        }
        if (!companys?.isEmpty()!!) {
            companys.forEach {
                it.trace()
            }
        }
    }
}