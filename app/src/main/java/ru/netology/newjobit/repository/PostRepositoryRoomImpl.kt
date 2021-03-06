package ru.netology.newjobit.repository

import androidx.lifecycle.LiveData
import ru.netology.newjobit.model.dao.PostDao
import ru.netology.newjobit.model.dto.Post
import androidx.lifecycle.Transformations
import ru.netology.newjobit.model.entity.PostEntity

class PostRepositoryRoomImpl(
    private val postDao: PostDao
) : PostRepository {

    override fun getAll(): LiveData<List<Post>> = Transformations.map(postDao.getAll()) { list ->
        list.map { entity ->
            entity.toPost()
        }
    }

    override fun likeById(id: Long) {
        postDao.likeById(id)
    }

    override fun shareById(id: Long) {
        postDao.shareById(id)
    }

    override fun viewingById(id: Long) {
        postDao.viewingById(id)
    }

    override fun savePost(post: Post) {
        postDao.savePost(PostEntity.fromPost(post))
    }

    override fun removeById(id: Long) {
        postDao.removeById(id)
    }
}