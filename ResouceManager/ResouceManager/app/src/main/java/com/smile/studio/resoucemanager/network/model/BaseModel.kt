package com.smile.studio.resoucemanager.network.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.smile.studio.libsmilestudio.utils.Debug

open class BaseModel(
        @SerializedName("id", alternate = arrayOf("ID"))
        var id: Int? = 0,
        @SerializedName("name", alternate = arrayOf("Name", "FullName"))
        var name: String? = null,
        @SerializedName("status", alternate = arrayOf("Status", "active"))
        var status: Int? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readValue(Int::class.java.classLoader) as? Int, parcel.readString(), parcel.readValue(Int::class.java.classLoader) as? Int)

    open fun trace() {
        Debug.e("id: $id\nname: $name")
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeValue(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BaseModel> {
        override fun createFromParcel(parcel: Parcel): BaseModel {
            return BaseModel(parcel)
        }

        override fun newArray(size: Int): Array<BaseModel?> {
            return arrayOfNulls(size)
        }
    }

}