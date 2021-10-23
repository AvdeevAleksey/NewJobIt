package ru.netology.newjobit.model

import ru.netology.newjobit.model.dao.LoginDao
import ru.netology.newjobit.model.dto.Login
import java.io.IOException
import java.lang.Long.parseLong
import java.util.*

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String, loginDao: LoginDao): Result<Login> {
        try {
//            val fakeUser = Login(parseLong(UUID.randomUUID().toString()), username)
            val fakeUser = loginDao.getLoginById(loginDao.getLoginIdByDisplayName(username)).toLoginIn()
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}