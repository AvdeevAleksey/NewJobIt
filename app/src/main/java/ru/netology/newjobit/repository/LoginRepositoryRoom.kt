package ru.netology.newjobit.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ru.netology.newjobit.model.LoginDataSource
import ru.netology.newjobit.model.Result
import ru.netology.newjobit.model.dao.LoginDao
import ru.netology.newjobit.model.dto.Login
import ru.netology.newjobit.model.entity.LoginEntity

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepositoryRoom(
    private val loginDao: LoginDao,
    private val loginDataSource: LoginDataSource
    ) : LoginRepository {

    override fun getAll(): LiveData<List<Login>> = Transformations.map(loginDao.getAll()) { list ->
        list.map { entity ->
            entity.toLoginIn()
        }
    }

    // in-memory cache of the loggedInUser object
    var user: Login? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    override fun logout() {
        user = null
    }

    override fun login(username: String, password: String): Result<Login> {
        val result = loginDataSource.login(username, password, loginDao)
        if (result is Result.Success<Login>) {
            setLoggedInUser(result.data)
        }
        return result
    }

    override fun saveLogin(login: Login) {
        loginDao.saveLogin(LoginEntity.fromLoginIn(login))
    }

    private fun setLoggedInUser(login: Login) {
        this.user = login
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}