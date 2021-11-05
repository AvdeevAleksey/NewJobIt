package ru.netology.newjobit.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ru.netology.newjobit.model.dao.LoginDao
import ru.netology.newjobit.model.dto.Login
import ru.netology.newjobit.model.entity.LoginEntity

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepositoryRoom(
    private val loginDao: LoginDao
    ) : LoginRepository {

    override fun getAll(): LiveData<List<Login>> = Transformations.map(loginDao.getAll()) { list ->
        list.map { entity ->
            entity.toLoginIn()
        }
    }

    override fun logout() {
        val user = null
    }

    override fun login(username: String, password: String): Login {
        return (if (loginDao.userLoggedIn(username, password)) {
            loginDao.getLoginById(
                loginDao.getLoginIdByDisplayName(username)
            )
        } else LoginEntity(0L, username, "", "")).toLoginIn()
    }

    override fun getLoginById(userId: Long): Login {
        return loginDao.getLoginById(userId).toLoginIn()
    }

    override fun saveLogin(login: Login) {
        loginDao.saveLogin(LoginEntity.fromLoginIn(login))
    }
}