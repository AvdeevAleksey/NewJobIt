package ru.netology.newjobit.model.dao

import android.provider.ContactsContract.CommonDataKinds.*
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.newjobit.model.entity.LoginEntity

@Dao
interface LoginDao {

    @Query("SELECT * FROM LoginEntity ORDER BY userId DESC")
    fun getAll(): LiveData<List<LoginEntity>>

    @Insert
    fun insert(loginEntity: LoginEntity)

    @Query("SELECT * FROM LoginEntity WHERE userId = :id")
    fun getLoginById(id: Long): LoginEntity

    @Query("SELECT userId FROM LoginEntity WHERE displayName = :displayName")
    fun getLoginIdByDisplayName(displayName: String): Long

    @Query("SELECT displayName AND passwd FROM LoginEntity WHERE displayName = :displayName AND passwd = :passwd")
    fun userLoggedIn(displayName: String, passwd: String): Boolean

    @Query("UPDATE LoginEntity SET displayName = :displayName, passwd = :passwd, avatar = :avatar WHERE userId = :id")
    fun updateLoginById(id: Long, displayName: String, passwd: String, avatar: String)

    fun saveLogin(loginEntity: LoginEntity) =
        if (loginEntity.userId == 0L && loginEntity.displayName != getLoginById(getLoginIdByDisplayName(loginEntity.displayName)).displayName)
            insert(loginEntity)
        else updateLoginById(loginEntity.userId, loginEntity.displayName, loginEntity.passwd, loginEntity.avatar)
}