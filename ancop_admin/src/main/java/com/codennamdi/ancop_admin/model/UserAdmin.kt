package com.codennamdi.ancop_admin.model

import android.os.Parcel
import android.os.Parcelable

data class UserAdmin(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val image: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(image)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<UserAdmin> {
        override fun createFromParcel(parcel: Parcel): UserAdmin {
            return UserAdmin(parcel)
        }

        override fun newArray(size: Int): Array<UserAdmin?> {
            return arrayOfNulls(size)
        }
    }
}
