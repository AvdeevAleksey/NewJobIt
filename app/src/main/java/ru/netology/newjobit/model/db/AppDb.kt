package ru.netology.newjobit.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import ru.netology.newjobit.model.dao.LoginDao
import ru.netology.newjobit.model.dao.PostDao
import ru.netology.newjobit.model.dto.Login
import ru.netology.newjobit.model.entity.LoginEntity
import ru.netology.newjobit.model.entity.PostEntity

@Database(entities = [PostEntity::class,LoginEntity::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun loginDao(): LoginDao

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDb::class.java,
            "MyNewJobItRoomDb.db"
        ).allowMainThreadQueries()
            .build()
    }
}