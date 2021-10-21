package ru.netology.newjobit.repository

import androidx.lifecycle.LiveData
import ru.netology.newjobit.model.dao.PostDao
import ru.netology.newjobit.model.dto.Post
import androidx.lifecycle.Transformations
import ru.netology.newjobit.model.entity.PostEntity

class PostRepositoryRoomImpl(
    private val dao: PostDao
) : PostRepository {

    override fun getAll(): LiveData<List<Post>> = Transformations.map(dao.getAll()) { list ->
        list.map { entity ->
            entity.toPost()
        }
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
    }

    override fun viewingById(id: Long) {
        dao.viewingById(id)
    }

    override fun savePost(post: Post) {
        dao.savePost(PostEntity.fromPost(post))
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }
}