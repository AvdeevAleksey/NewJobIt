package ru.netology.newjobit.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object AndroidUtils {
    const val POST_FILE = "posts.json"
    const val POST_KEY = "post"
    const val LOGIN_KEY = "login"
    const val CAMERA_REQUEST_CODE = 0

    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken,0)
    }
}
