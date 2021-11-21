package ru.netology.newjobit.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

object AndroidUtils {
    const val POST_FILE = "posts.json"
    const val POST_KEY = "post"
    const val LOGIN_KEY = "login"
    const val CAMERA_REQUEST_CODE = 0

    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken,0)
    }

    inline fun <T1, T2, R> LiveData<T1>.mergeWith(
        other: LiveData<T2>,
        crossinline merger: (T1?, T2?) -> R
    ): LiveData<R> {
        val mediator = MediatorLiveData<R>()

        val onChanged = Observer<Any?> {
            mediator.value = merger(value, other.value)
        }

        mediator.addSource(this, onChanged)
        mediator.addSource(other, onChanged)

        return mediator
    }
}
