package ru.netology.newjobit.view.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import ru.netology.newjobit.databinding.FragmentUserRegistrationBinding
import ru.netology.newjobit.viewmodel.LoginViewModel
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ru.netology.newjobit.R
import ru.netology.newjobit.model.dto.Login
import ru.netology.newjobit.model.dto.Post
import ru.netology.newjobit.utils.AndroidUtils
import ru.netology.newjobit.view.activity.ui.login.LoggedInUserView

class UserRegistrationFragment : Fragment() {

    private var myBinding: FragmentUserRegistrationBinding? = null
    private val loginViewModel: LoginViewModel by viewModels(ownerProducer = ::requireParentFragment)
    private val binding get() = myBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myBinding = FragmentUserRegistrationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userLogin = binding.userLoginEditText
        val userPasswd = binding.userPasswdEditText
        val userConformPasswd = binding.userPasswdConfirmEditText
        val userAvatarButton = binding.userAvatarImageButton
        val registrationButton = binding.registrationButton

        val login : Login? = arguments?.getParcelable<Login>(AndroidUtils.LOGIN_KEY)
        arguments?.remove(AndroidUtils.LOGIN_KEY)

        with(binding.userLoginEditText) {
            if (login != null) {
                text.append(login.displayName)
            } else {
                text.append("")
            }
        }

        with(binding.userPasswdEditText) {
            if (login != null) {
                text.append(login.passwd)
            } else {
                text.append("")
            }
        }

        loginViewModel.passwdFormState.observe(viewLifecycleOwner,
            Observer { passwdFormState ->
                if (passwdFormState == null) {
                    return@Observer
                }
                registrationButton.isEnabled = passwdFormState.isDataValid
                passwdFormState.passwordConfirmError?.let {
                    userConformPasswd.error = getString(it)
                }
            })

        loginViewModel.passwdResult.observe(viewLifecycleOwner,
            Observer { passwdResult ->
                passwdResult ?: return@Observer
                passwdResult.error?.let {
                    showPasswdConfirmFailed(it)
                }
                passwdResult.success?.let {
                    updatePasswdConfirm(it)
                }

            }
        )

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.regPasswdConfirmDataChange(
                    userPasswd.text.toString(),
                    userConformPasswd.text.toString()
                )
            }
        }
        userConformPasswd.addTextChangedListener(afterTextChangedListener)

        registrationButton.setOnClickListener {
            loginViewModel.saveLogin()
            AndroidUtils.hideKeyboard(binding.root)
            findNavController().navigate(
                R.id.action_userRegistrationFragment_to_feedFragment,
                bundleOf(AndroidUtils.LOGIN_KEY to login)
            )
        }

    }

    private fun updatePasswdConfirm(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.displayName
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    private fun showPasswdConfirmFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

}