package ru.netology.newjobit.repository

import androidx.lifecycle.LiveData
import ru.netology.newjobit.model.Result
import ru.netology.newjobit.model.dto.Login

interface LoginRepository {
    fun getAll(): LiveData<List<Login>>
    fun login(username: String, password: String): Result<Login>
    fun saveLogin(login: Login)
    fun logout()
}