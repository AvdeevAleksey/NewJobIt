package ru.netology.newjobit.view.activity.ui.login

sealed class PasswdResult {
    data class Success(val view: LoggedInUserView? = null): PasswdResult()
    object PasswordsNotMach : PasswdResult()
    object PasswordNotEntered : PasswdResult()
}