package ru.netology.newjobit.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.newjobit.model.entity.LikedEntity

@Dao
interface LikedDao {
    @Query("SELECT * FROM LikedEntity ORDER BY postId DESC")
    fun getAll(): List<LikedEntity>

    @Insert
    fun insert(likedEntity: LikedEntity)

    @Query("DELETE FROM LikedEntity WHERE postId = :postId AND loginId = :loginId")
    fun removeLikeById(postId: Long, loginId: Long)

    fun changeLike(likedEntity: LikedEntity) =
        if (!userLikedPost(likedEntity.postId,likedEntity.loginId))
            insert(likedEntity)
        else removeLikeById(likedEntity.postId, likedEntity.loginId)

    @Query("SELECT * FROM LikedEntity WHERE postId LIKE :postId AND loginId LIKE :loginId")
    fun userLikedPost(postId: Long, loginId: Long): Boolean

}