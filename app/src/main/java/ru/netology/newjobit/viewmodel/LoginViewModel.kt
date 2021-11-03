package ru.netology.newjobit.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.*
import ru.netology.newjobit.R
import ru.netology.newjobit.repository.LoginRepositoryRoom
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
        AppDb.getInstance(application).loginDao()
    )

    val loginLiveData = loginRepository.getAll()
    val edited = MutableLiveData(empty)
    private val editedLoginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = editedLoginForm
    private val editedLoginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = editedLoginResult
    private val editedPasswdForm = MutableLiveData<PasswdFormState>()
    val passwdFormState: LiveData<PasswdFormState> = editedPasswdForm
    private val editedPasswordResult = MutableLiveData<PasswdResult>()
    val passwdResult: LiveData<PasswdResult> = editedPasswordResult



    fun saveLogin() {
        edited.value?.let {
            loginRepository.saveLogin(it)
        }
        edited.value = empty
    }

    fun login(username: String, password: String) {
//        val login = loginRepository.getAll().value.orEmpty().find { login ->
//            login.displayName == username
//        }
        val login = loginRepository.login(username, password)

        editedLoginResult.value = when {
            login.userId == 0L -> LoginResult.UserNotFound
            login.passwd != password -> LoginResult.IncorrectPassword
            else -> LoginResult.Success(LoggedInUserView(username, password))
        }
    }

    fun accessVerification(username: String, password: String) {
        if (!isUserNameValid(username)) {
            editedLoginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            editedLoginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            editedLoginForm.value = LoginFormState(isDataValid = true)
        }
    }


    fun loginChanged(login: Login) {
        if (login(login.displayName,login.passwd) != null) {
            edited.value?.let {
                val username = login.displayName.trim()
                val passwd = login.passwd.trim()
                val avatar = login.avatar.trim()
                if (it.displayName == username && it.passwd == passwd) {
                    return
                }
                edited.value = it.copy(displayName = username, passwd = passwd, avatar = avatar)
            }
        }
    }

    fun regPasswdConfirm(username: String, passwd: String, passwdConfirm: String ) {
        editedPasswordResult.value = when {
            passwd == null -> PasswdResult.PasswordNotEntered
            passwd != passwdConfirm -> PasswdResult.PasswordsNotMach
            else -> PasswdResult.Success(LoggedInUserView(username,passwd))
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