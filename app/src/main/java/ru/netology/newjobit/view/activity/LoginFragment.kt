package ru.netology.newjobit.view.activity

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_login.*
import ru.netology.newjobit.R
import ru.netology.newjobit.databinding.FragmentLoginBinding
import ru.netology.newjobit.model.dto.Login
import ru.netology.newjobit.utils.AndroidUtils.LOGIN_KEY
import ru.netology.newjobit.view.activity.ui.login.LoginResult
import ru.netology.newjobit.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModels(ownerProducer = ::requireParentFragment)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(inflater,container,false)

        loginViewModel.loginLiveData.observe(viewLifecycleOwner){
            binding.root
        }

        val loginDisplayName = arguments?.getParcelable<Login>(LOGIN_KEY)?.displayName
        val login = loginViewModel.loginLiveData.value?.find {
            it.displayName == loginDisplayName
        }
        arguments?.remove(LOGIN_KEY)

        val usrLoginEditText = binding.usernameEditText
        val passwdEditText = binding.passwordEditText
        val singInButton = binding.loginAndRegisterButton
        if (login != null) {
            usrLoginEditText.append(login.displayName)
            passwdEditText.append(login.passwd)
        }


        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.accessVerification(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
                singInButton.isEnabled = true
            }
        }
        usrLoginEditText.addTextChangedListener(afterTextChangedListener)
        passwdEditText.addTextChangedListener(afterTextChangedListener)



        loginViewModel.loginResult.observe(viewLifecycleOwner) { it ->
            when (it) {
                LoginResult.IncorrectPassword -> {
                    Toast.makeText(requireContext(),R.string.incorrect_password,Toast.LENGTH_LONG).show()
                }
                is LoginResult.Success -> {

                    val loginId: Long = loginViewModel.loginLiveData.value?.find {
                        it.displayName == usrLoginEditText.text.toString()
                    }?.userId ?: 0L
                    findNavController().navigate(
                        R.id.action_loginFragment_to_feedFragment,
                        bundleOf(LOGIN_KEY to loginId)
                    )
                }
                LoginResult.UserNotFound -> {
                    Toast.makeText(requireContext(),"User not found",Toast.LENGTH_LONG).show()
                    findNavController().navigate(
                        R.id.action_loginFragment_to_userRegistrationFragment,
                        bundleOf(LOGIN_KEY to usrLoginEditText.text.toString())
                    )
                }
            }
        }

        singInButton.setOnClickListener {
            loginViewModel.login(
                usrLoginEditText.text.toString(),
                passwdEditText.text.toString()
            )
        }

        return binding.root
    }
}