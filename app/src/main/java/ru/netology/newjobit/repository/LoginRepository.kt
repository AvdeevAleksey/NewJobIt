package ru.netology.newjobit.repository

import androidx.lifecycle.LiveData
import ru.netology.newjobit.model.dto.LoggedInUser

interface LoginRepository {
    fun login(username: String, password: String): LiveData<List<LoggedInUser>>
    fun logout()
}