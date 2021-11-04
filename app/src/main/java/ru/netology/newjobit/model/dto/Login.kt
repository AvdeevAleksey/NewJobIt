package ru.netology.newjobit.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class Login(
    val userId: Long,
    val displayName: String,
    val passwd: String,
    val avatar: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        requireNotNull(parcel.readString()),
        requireNotNull(parcel.readString()),
        requireNotNull(parcel.readString())
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(userId)
        parcel.writeString(displayName)
        parcel.writeString(passwd)
        parcel.writeString(avatar)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Login> {
        override fun createFromParcel(parcel: Parcel): Login {
            return Login(parcel)
        }

        override fun newArray(size: Int): Array<Login?> {
            return arrayOfNulls(size)
        }
    }

}
