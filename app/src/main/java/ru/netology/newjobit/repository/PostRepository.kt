package ru.netology.newjobit.repository

import androidx.lifecycle.LiveData
import ru.netology.newjobit.model.dto.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun likeById(id: Long, userLogin: String)
    fun shareById(id: Long)
    fun viewingById(id: Long)
    fun savePost(post: Post)
    fun removeById(id: Long)
}