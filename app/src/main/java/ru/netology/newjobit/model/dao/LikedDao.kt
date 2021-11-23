package ru.netology.newjobit.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.newjobit.model.entity.LikedEntity

@Dao
interface LikedDao {
    @Query("SELECT * FROM LikedEntity ORDER BY postId DESC")
    fun getAll(): LiveData<List<LikedEntity>>

    @Insert
    fun insert(likedEntity: LikedEntity)

    @Query("DELETE FROM LikedEntity WHERE postId = :postId AND userLogin = :userLogin")
    fun removeLikeById(postId: Long, userLogin: String)

    fun changeLike(likedEntity: LikedEntity) =
        if (!userLikedPost(likedEntity.postId,likedEntity.userLogin))
            insert(likedEntity)
        else removeLikeById(likedEntity.postId, likedEntity.userLogin)

    @Query("SELECT * FROM LikedEntity WHERE postId LIKE :postId AND userLogin LIKE :userLogin")
    fun userLikedPost(postId: Long, userLogin: String): Boolean

}