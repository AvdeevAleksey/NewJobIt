package ru.netology.newjobit.view.activity

import androidx.lifecycle.Observer
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_user_registration.*
import ru.netology.newjobit.R
import ru.netology.newjobit.databinding.FragmentLoginBinding
import ru.netology.newjobit.model.dto.Login
import ru.netology.newjobit.utils.AndroidUtils
import ru.netology.newjobit.utils.AndroidUtils.LOGIN_KEY
import ru.netology.newjobit.view.activity.ui.login.LoggedInUserView
import ru.netology.newjobit.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModels(ownerProducer = ::requireParentFragment)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(inflater,container,false)

        val usrLoginEditText = binding.usernameEditText
        val passwdEditText = binding.passwordEditText
        val singInButton = binding.loginAndRegisterButton
        singInButton.isEnabled = true

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }
        usrLoginEditText.addTextChangedListener(afterTextChangedListener)
        passwdEditText.addTextChangedListener(afterTextChangedListener)

        loginViewModel.edited.observe(viewLifecycleOwner) {
            if (it.userId == 0L) it else return@observe
        }

        loginViewModel.loginLiveData.map { logins ->
            logins.find { it.equals(loginViewModel.login(usrLoginEditText.text.toString(),passwdEditText.text.toString())) }
        }.observe(viewLifecycleOwner) { login ->
            login ?: kotlin.run {
                loginViewModel.edited.value.let {
                    if (login?.userId == 0L) login else return@observe
                }
            }
            binding.apply {


                singInButton.setOnClickListener {
                    val login = loginViewModel.edited.value.let {
                        if (login?.userId == 0L) login else return@setOnClickListener
                    }
                    if (login.userId != 0L) {
                        findNavController().navigate(
                            R.id.action_loginFragment_to_feedFragment,
                            bundleOf(LOGIN_KEY to login)
                        )
                    }else {
                        findNavController().navigate(
                            R.id.action_loginFragment_to_userRegistrationFragment,
                            bundleOf(LOGIN_KEY to login)
                        )
                    }
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.displayName
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }
}