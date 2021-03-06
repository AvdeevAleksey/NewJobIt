package ru.netology.newjobit.model.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.newjobit.model.dto.Login
import ru.netology.newjobit.model.dto.Post

//@Entity(foreignKeys = [ForeignKey(entity = LoginEntity::class,
//            parentColumns = arrayOf("userId"),
//            childColumns = arrayOf("author")
//        )]
//)
@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val avatar: String = "",
    val author: String,
    val published: String,
    val content: String,
    val videoInPost: String,
    val likesCount: Int,
    val shareCount: Int,
    val viewingCount: Int,
    val likedByMe: Boolean
) {
    fun toPost(): Post = Post(
        id,
        avatar,
        author,
        published,
        content,
        videoInPost,
        likesCount,
        shareCount,
        viewingCount,
        likedByMe,
    )

    companion object {
        fun fromPost(post: Post): PostEntity =
            PostEntity(
                post.id,
                post.avatar,
                post.author,
                post.published,
                post.content,
                post.videoInPost,
                post.likesCount,
                post.shareCount,
                post.viewingCount,
                post.likedByMe,
            )
    }
}

@Entity
data class LoginEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Long,
    val displayName: String,
    val passwd: String,
    val avatar: String
) {
    fun toLoginIn(): Login = Login(userId, displayName, passwd, avatar)

    companion object {
        fun fromLoginIn(login: Login): LoginEntity = LoginEntity(login.userId,login.displayName, login.passwd, login.avatar)
    }
}