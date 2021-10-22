package ru.netology.newjobit.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.newjobit.model.entity.LoginEntity
import ru.netology.newjobit.model.entity.PostEntity

@Dao
interface LoginDao {

    @Query("SELECT * FROM LoginEntity ORDER BY userId DESC")
    fun getAll(): LiveData<List<LoginEntity>>

    @Insert
    fun insert(loginEntity: LoginEntity)

    @Query("UPDATE LoginEntity SET displayName = :displayName WHERE userId = :id")
    fun updateLoginById(id: Long, displayName: String)

    fun saveLogin(loginEntity: LoginEntity) =
        if (loginEntity.userId == 0L)
            insert(loginEntity)
        else updateLoginById(loginEntity.userId, loginEntity.displayName)
}