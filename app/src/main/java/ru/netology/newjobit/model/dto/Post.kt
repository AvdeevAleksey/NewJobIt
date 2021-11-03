package ru.netology.newjobit.model.dto

import android.os.Parcel
import android.os.Parcelable

data class Post(
        val id: Long,
        val avatar: String,
        val author: String,
        val published: String,
        val content: String,
        val videoInPost: String,
        val likesCount: Int,
        val shareCount: Int,
        val viewingCount: Int,
        val likedByMe: Boolean = false
): Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readLong(),
                requireNotNull(parcel.readString()),
                requireNotNull(parcel.readString()),
                requireNotNull(parcel.readString()),
                requireNotNull(parcel.readString()),
                requireNotNull(parcel.readString()),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readByte() != 0.toByte(),
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeLong(id)
                parcel.writeString(avatar)
                parcel.writeString(author)
                parcel.writeString(published)
                parcel.writeString(content)
                parcel.writeString(videoInPost)
                parcel.writeInt(likesCount)
                parcel.writeInt(shareCount)
                parcel.writeInt(viewingCount)
                parcel.writeByte(if (likedByMe) 1 else 0)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<Post> {
                override fun createFromParcel(parcel: Parcel): Post {
                        return Post(parcel)
                }

                override fun newArray(size: Int): Array<Post?> {
                        return arrayOfNulls(size)
                }
        }
}
