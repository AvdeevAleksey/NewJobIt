package ru.netology.newjobit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import ru.netology.newjobit.R
import ru.netology.newjobit.repository.LoginRepositoryRoom
import ru.netology.newjobit.model.Result

import ru.netology.newjobit.view.activity.ui.login.LoggedInUserView
import ru.netology.newjobit.view.activity.ui.login.LoginFormState
import ru.netology.newjobit.view.activity.ui.login.LoginResult

class LoginViewModel(
    private val loginRepositoryRoom: LoginRepositoryRoom
    ) : ViewModel() {

    private val editedLoginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = editedLoginForm

    private val editedLoginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = editedLoginResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepositoryRoom.login(username, password)

        if (result is Result.Success) {
            editedLoginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            editedLoginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            editedLoginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            editedLoginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            editedLoginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}