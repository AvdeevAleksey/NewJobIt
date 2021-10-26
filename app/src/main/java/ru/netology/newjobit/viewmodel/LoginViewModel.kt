package ru.netology.newjobit.viewmodel

import android.app.Application
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import ru.netology.newjobit.R
import ru.netology.newjobit.model.LoginDataSource
import ru.netology.newjobit.repository.LoginRepositoryRoom
import ru.netology.newjobit.model.Result
import ru.netology.newjobit.model.db.AppDb
import ru.netology.newjobit.model.dto.Login
import ru.netology.newjobit.repository.LoginRepository
import ru.netology.newjobit.view.activity.ui.login.*

private val empty = Login(
    userId = 0L,
    displayName = "",
    passwd = "",
    avatar = ""
)

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val loginRepository: LoginRepository = LoginRepositoryRoom(
        AppDb.getInstance(application).loginDao(),LoginDataSource()
    )

    val loginLiveData = loginRepository.getAll()
    val edited = MutableLiveData(empty)
    private val editedLoginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = editedLoginForm
    private val editedLoginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = editedLoginResult
    private val editedPasswdForm = MutableLiveData<PasswdFormState>()
    val passwdFormState: LiveData<PasswdFormState> = editedPasswdForm
    private val passwdConfirmed = MutableLiveData<PasswdResult>()
    val passwdResult: LiveData<PasswdResult> = passwdConfirmed

    fun checkLogin(username: String, password: String) : Boolean {
        val result = loginRepository.login(username, password)
        return if (result is Result.Success) {
            editedLoginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
            true
        } else {
            editedLoginResult.value = LoginResult(error = R.string.login_failed)
            false
        }
    }

    fun saveLogin() {
        edited.value?.let {
            loginRepository.saveLogin(it)
        }
        edited.value = empty
    }

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)

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

    fun regPasswdConfirmDataChange(passwd: String, passwdConfirm: String ) {
        if (passwd != passwdConfirm) {
            editedPasswdForm.value = PasswdFormState(passwordConfirmError = R.string.passwords_dont_match)
        } else {
            editedPasswdForm.value = PasswdFormState(isDataValid = true)
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