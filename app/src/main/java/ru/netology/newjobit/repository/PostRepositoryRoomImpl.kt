package ru.netology.newjobit.repository

import androidx.lifecycle.LiveData
import ru.netology.newjobit.model.dao.PostDao
import ru.netology.newjobit.model.dto.Post
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import ru.netology.newjobit.model.dao.LikedDao
import ru.netology.newjobit.model.dao.LoginDao
import ru.netology.newjobit.model.entity.LikedEntity
import ru.netology.newjobit.model.entity.PostEntity
import ru.netology.newjobit.utils.AndroidUtils.mergeWith

class PostRepositoryRoomImpl(
    private val postDao: PostDao,
    private val likedDao: LikedDao
) : PostRepository {

    override fun getAll(): LiveData<List<Post>> = Transformations.map(postDao.getAll()) { list ->
        list.map { entity ->
            entity.toPost()
        }
    }.mergeWith(likedDao.getAll()) { posts, likes ->
        posts.orEmpty()
            .map { post ->
                post.copy(
                    likesCount = likes.orEmpty().filter { liked ->
                        liked.postId == post.id
                    }.map { like ->
                        like.loginId
                    },
                    likedByMe = !likes.orEmpty().none { it.postId == post.id }
                )
            }
    }

    override fun likeById(id: Long, userId: Long) {
        likedDao.changeLike(LikedEntity.fromLiked(id,userId))
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