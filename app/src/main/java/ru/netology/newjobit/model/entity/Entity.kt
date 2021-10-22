package ru.netology.newjobit.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.newjobit.model.dto.LoggedInUser
import ru.netology.newjobit.model.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    val videoInPost: String,
    val likesCount: Int,
    val shareCount: Int,
    val viewingCount: Int,
    val likedByMe: Boolean
) {
    fun toPost(): Post = Post(id, author, published, content, videoInPost, likesCount, shareCount, viewingCount,likedByMe)

    companion object {
        fun fromPost(post: Post): PostEntity =
            PostEntity(post.id,post.author,post.published,post.content,post.videoInPost,post.likesCount,post.shareCount,post.viewingCount,post.likedByMe)
    }
}

@Entity
data class LoginEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Long,
    val displayName: String
) {
    fun toLoggedIn(): LoggedInUser = LoggedInUser(userId, displayName)

    companion object {
        fun fromLoggedIn(loggedInUser: LoggedInUser): LoginEntity = LoginEntity(loggedInUser.userId,loggedInUser.displayName)
    }
}