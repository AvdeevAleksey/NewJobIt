package ru.netology.newjobit.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ru.netology.newjobit.model.LoginDataSource
import ru.netology.newjobit.model.Result
import ru.netology.newjobit.model.dao.LoginDao
import ru.netology.newjobit.model.dto.LoggedInUser
import ru.netology.newjobit.model.dto.Post

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepositoryRoom(
    private val dao: LoginDao
    ) : LoginRepository {

    override fun getAll(): LiveData<List<LoggedInUser>> = Transformations.map(dao.getAll()) { list ->
        list.map { entity ->
            entity.toLoggedIn()
        }
    }

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dao.logout()
    }

    fun login(username: String, password: String): Result<LoggedInUser> {
        // handle login
        val result = dao.login(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}