package ru.netology.newjobit.view.activity

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout.VERSION
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import ru.netology.newjobit.databinding.FragmentUserRegistrationBinding
import ru.netology.newjobit.viewmodel.LoginViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_card_post.*
import kotlinx.android.synthetic.main.fragment_login.*
import ru.netology.newjobit.R
import ru.netology.newjobit.model.dto.Login
import ru.netology.newjobit.model.dto.Post
import ru.netology.newjobit.utils.AndroidUtils
import ru.netology.newjobit.utils.AndroidUtils.CAMERA_REQUEST_CODE
import ru.netology.newjobit.view.activity.ui.login.LoggedInUserView
import ru.netology.newjobit.view.activity.ui.login.PasswdResult
import ru.netology.newjobit.view.adapter.countMyClick
import java.io.File
import java.util.jar.Manifest

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

        val userDisplayName = arguments?.getParcelable<Login>(AndroidUtils.LOGIN_KEY)?.displayName
        arguments?.remove(AndroidUtils.LOGIN_KEY)

        val userLogin = binding.userLoginEditText
        userLogin.text.append(userDisplayName)
        val userPasswd = binding.userPasswdEditText
        val userConformPasswd = binding.userPasswdConfirmEditText
        val userAvatarButton = binding.userAvatarImageButton
        val registrationButton = binding.registrationButton

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.regPasswdConfirm(
                    userLogin.text.toString(),
                    userPasswd.text.toString(),
                    userConformPasswd.text.toString())
            }
        }
        userLogin.addTextChangedListener(afterTextChangedListener)
        userPasswd.addTextChangedListener(afterTextChangedListener)
        userConformPasswd.addTextChangedListener(afterTextChangedListener)
        userAvatarButton.setOnClickListener {
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (callCameraIntent.resolveActivity(requireContext().packageManager) != null) {
                startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
            }

            Toast.makeText(context, "Clicked on avatar",Toast.LENGTH_SHORT).show()
        }

        loginViewModel.passwdResult.observe(viewLifecycleOwner) {
            when (it) {
                PasswdResult.PasswordNotEntered -> {
                    registrationButton.isEnabled = false
                }
                PasswdResult.PasswordsNotMach -> {
                    registrationButton.isEnabled = false
                }
                is PasswdResult.Success -> {
                    Login(0L, userLogin.text.toString(), userPasswd.text.toString(), "")
                    registrationButton.isEnabled = true
                }
            }
        }

        registrationButton.setOnClickListener {
            val login = if (binding.userLoginEditText.text.isNotBlank() && binding.userPasswdEditText.text.isNotBlank()) {
                loginViewModel.edited.value?.copy(
                    displayName = userLogin.text.toString(),
                    passwd = userPasswd.text.toString(),
                    avatar = ""
                )
                } else {
                    loginViewModel.edited.value.let { login ->
                    if (login?.userId == 0L) login else return@setOnClickListener
                    }
                }

            if (login != null) {
                loginViewModel.loginChanged(login)
            }
            loginViewModel.saveLogin()
            AndroidUtils.hideKeyboard(binding.root)
            findNavController().navigate(
                R.id.action_userRegistrationFragment_to_feedFragment,
                bundleOf(AndroidUtils.LOGIN_KEY to login)
            )
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    avatarImageView.setImageBitmap(data.extras?.get("data") as Bitmap)
                }

            }
            else -> {
                Toast.makeText(context, "Unrecognized request code", Toast.LENGTH_SHORT)
            }
        }
    }
}