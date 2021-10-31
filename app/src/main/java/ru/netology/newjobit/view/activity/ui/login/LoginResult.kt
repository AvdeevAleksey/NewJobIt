package ru.netology.newjobit.view.activity.ui.login

/**
 * Authentication result : success (user details) or error message.
 */
sealed class LoginResult {
    data class Success(val view: LoggedInUserView? = null): LoginResult()
    object IncorrectPassword : LoginResult()
    object UserNotFound : LoginResult()
}