package com.smile.studio.resoucemanager.network.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.smile.studio.libsmilestudio.utils.Debug
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class Profile(

        @SerializedName("IsUpload") val isUpload: String? = null,

        @SerializedName("LoginName") val loginName: String? = null,

        @SerializedName("Email") val email: String? = null,

        @SerializedName("MobileConfirm") val mobileConfirm: String? = null,

        @SerializedName("TimeKeeping") val timeKeeping: String? = null,

        @SerializedName("DateIn") val dateIn: String? = null,

        @SerializedName("Sex") val sex: String? = null,

        @SerializedName("Birthday") val birthday: String? = null,

        @SerializedName("EmailConfirm") val emailConfirm: String? = null,

        @SerializedName("LastChangePassword") val lastChangePassword: String? = null,

        @SerializedName("Image") val image: String? = null,

        @SerializedName("DateProbationary") val dateProbationary: String? = null,

        @SerializedName("CreateDate") val createDate: String? = null,

        @SerializedName("Mobile") val mobile: String? = null,

        @SerializedName("ChatID") val chatID: String? = null,

        @SerializedName("GroupRoom") val groupRoom: String? = null,

        @SerializedName("Password") val password: String? = null,

        @SerializedName("IPAccess") val iPAccess: String? = null,

        @SerializedName("DateOut") val dateOut: String? = null,

        @SerializedName("ID")
        var id: Int? = 0,

        @SerializedName("FullName")
        var name: String? = null,

        @SerializedName("Status")
        var status: Int? = null) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int)

    fun trace() {
        Debug.e("\nimage: $image\nsex: $sex\nBirthday: $birthday")
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(isUpload)
        parcel.writeString(loginName)
        parcel.writeString(email)
        parcel.writeString(mobileConfirm)
        parcel.writeString(timeKeeping)
        parcel.writeString(dateIn)
        parcel.writeString(sex)
        parcel.writeString(birthday)
        parcel.writeString(emailConfirm)
        parcel.writeString(lastChangePassword)
        parcel.writeString(image)
        parcel.writeString(dateProbationary)
        parcel.writeString(createDate)
        parcel.writeString(mobile)
        parcel.writeString(chatID)
        parcel.writeString(groupRoom)
        parcel.writeString(password)
        parcel.writeString(iPAccess)
        parcel.writeString(dateOut)
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeValue(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Profile> {

        val UID = "uid"

        override fun createFromParcel(parcel: Parcel): Profile {
            return Profile(parcel)
        }

        override fun newArray(size: Int): Array<Profile?> {
            return arrayOfNulls(size)
        }
    }
}