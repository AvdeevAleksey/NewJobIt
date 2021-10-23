package ru.netology.newjobit.model.dto

import android.provider.ContactsContract.CommonDataKinds.*
import java.net.PasswordAuthentication

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class Login(
    val userId: Long,
    val displayName: String,
    val passwd: String,
    val email: String
)