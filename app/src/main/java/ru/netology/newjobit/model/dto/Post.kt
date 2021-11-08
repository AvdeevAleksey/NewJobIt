package ru.netology.newjobit.model.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(
    val id: Long,
    val avatar: String,
    val author: String,
    val published: String,
    val content: String,
    val videoInPost: String,
    val likesCount: MutableList<Long>,
    val shareCount: Int,
    val viewingCount: Int,
    val likedByMe: Boolean = false
): Parcelable