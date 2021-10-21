package ru.netology.newjobit.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.newjobit.model.db.AppDb
import ru.netology.newjobit.model.dto.Post
import ru.netology.newjobit.repository.PostRepository
import ru.netology.newjobit.repository.PostRepositoryRoomImpl
import java.util.*

private val empty = Post(
    id = 0L,
    author = "Me",
    published = Calendar.getInstance().time.toString(),
    content = "",
    videoInPost = "",
    likesCount = 0,
    shareCount = 0,
    viewingCount = 0,
    likedByMe = false
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryRoomImpl(
        AppDb.getInstance(application).postDao()
    )
    val data = repository.getAll()
    val edited = MutableLiveData(empty)
    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun viewingById(id: Long) = repository.viewingById(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun savePost() {
        edited.value?.let {
            repository.savePost(it)
        }
        edited.value = empty
    }

    fun editPost(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content == text) {
                return
            }
            edited.value = it.copy(content = text)
        }
    }
}