package com.codennamdi.ancopapp.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.Serializable

@Serializable
data class HymnData(
    val title: String,
    val hymns: List<Hymn>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.createTypedArrayList(Hymn)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeTypedList(hymns)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<HymnData> {
        override fun createFromParcel(parcel: Parcel): HymnData {
            return HymnData(parcel)
        }

        override fun newArray(size: Int): Array<HymnData?> {
            return arrayOfNulls(size)
        }
    }
}

@Serializable
data class Hymn(
    val title: String,
    val num: Int,
    val verses: List<String>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.createStringArrayList()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeInt(num)
        parcel.writeStringList(verses)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Hymn> {
        override fun createFromParcel(parcel: Parcel): Hymn {
            return Hymn(parcel)
        }

        override fun newArray(size: Int): Array<Hymn?> {
            return arrayOfNulls(size)
        }
    }
}