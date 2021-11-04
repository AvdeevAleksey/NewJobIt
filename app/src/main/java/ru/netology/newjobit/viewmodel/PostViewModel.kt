package ru.netology.newjobit.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.newjobit.model.db.AppDb
import ru.netology.newjobit.model.dto.Post
import ru.netology.newjobit.repository.PostRepository
import ru.netology.newjobit.repository.PostRepositoryRoomImpl
import java.text.SimpleDateFormat
import java.util.*


private val simpleDateFormat = SimpleDateFormat("HH:mm:ss dd.MM.yyyy")
private val empty = Post(
    id = 0L,
    avatar = "",
    author = "Me",
    published = simpleDateFormat.format(Calendar.getInstance().time).toString(),
    content = "",
    videoInPost = "",
    likesCount = 0,
    shareCount = 0,
    viewingCount = 0,
    likedByMe = false,
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val postRepository: PostRepository = PostRepositoryRoomImpl(
        AppDb.getInstance(application).postDao()
    )
    val postLiveData = postRepository.getAll()
    val edited = MutableLiveData(empty)
    fun likeById(id: Long) = postRepository.likeById(id)
    fun shareById(id: Long) = postRepository.shareById(id)
    fun viewingById(id: Long) = postRepository.viewingById(id)
    fun removeById(id: Long) = postRepository.removeById(id)
    fun savePost() {
        edited.value?.let {
            postRepository.savePost(it)
        }
        edited.value = empty
    }

    fun editPost(post: Post) {
        edited.value = post
    }

    fun changeContent(avatar: String, author: String, content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content == text) {
                return
            }
            if (avatar.isNotBlank())
            edited.value = it.copy(avatar = avatar, author = author, content = text)
            else edited.value = it.copy(author = author, content = text)
        }
    }
}