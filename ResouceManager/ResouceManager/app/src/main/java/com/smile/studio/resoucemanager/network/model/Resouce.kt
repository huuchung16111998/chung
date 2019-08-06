package com.smile.studio.resoucemanager.network.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.smile.studio.libsmilestudio.utils.Debug

class Resouce(
        @SerializedName("id") var id: Int? = 0,
        @SerializedName("name") var name: String? = null,
        @SerializedName("code") var code: String? = null,
        @SerializedName("id_user") var uid: Int,
        @SerializedName("detail") var detail: String? = null,
        @SerializedName("purchase_date") var purchase_date: String,
        @SerializedName("serie") var serial: String? = null,
        @SerializedName("purchase_price") var purchase_price: Long,
        @SerializedName("made_by") var made_by: String? = null,
        @SerializedName("delivery_date") var delivery_date: String? = null,
        @SerializedName("tech_value") var tech_value: String? = null,
        @SerializedName("id_type_asset") var id_type_asset: Int,
        @SerializedName("status", alternate = arrayOf("Status", "active")) var status: Int? = null,
        @SerializedName("qrcode") var qrcode: String? = null,
        @SerializedName("company") var company: Int) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readInt()
    )

    fun trace() {
        Debug.e("uid: $uid\ncode: $code\nqrcode: $qrcode\ndetail: $detail")
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(code)
        parcel.writeInt(uid)
        parcel.writeString(detail)
        parcel.writeString(purchase_date)
        parcel.writeString(serial)
        parcel.writeLong(purchase_price)
        parcel.writeString(made_by)
        parcel.writeString(delivery_date)
        parcel.writeString(tech_value)
        parcel.writeInt(id_type_asset)
        parcel.writeValue(status)
        parcel.writeString(qrcode)
        parcel.writeInt(company)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Resouce> {
        override fun createFromParcel(parcel: Parcel): Resouce {
            return Resouce(parcel)
        }

        override fun newArray(size: Int): Array<Resouce?> {
            return arrayOfNulls(size)
        }
    }

}