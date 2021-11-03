package ru.netology.newjobit.view.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.map
import ru.netology.newjobit.databinding.FragmentUserRegistrationBinding
import ru.netology.newjobit.viewmodel.LoginViewModel
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_card_post.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_user_registration.*
import ru.netology.newjobit.R
import ru.netology.newjobit.model.dto.Login
import ru.netology.newjobit.utils.AndroidUtils
import ru.netology.newjobit.utils.AndroidUtils.CAMERA_REQUEST_CODE
import ru.netology.newjobit.view.activity.ui.login.PasswdResult

const val Image_Capture_Code = 24
const val REQUEST_CODE = 100
const val REQUEST_TAKE_PHOTO=24

class UserRegistrationFragment : Fragment() {

//    private var myBinding: FragmentUserRegistrationBinding? = null
    private val loginViewModel: LoginViewModel by viewModels(ownerProducer = ::requireParentFragment)
    private var imageAddress: Uri? = null
//    private val binding get() = myBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentUserRegistrationBinding.inflate(inflater,container,false)

        val loginDisplayName = arguments?.getString(AndroidUtils.LOGIN_KEY)
        arguments?.remove(AndroidUtils.LOGIN_KEY)

        val userLogin = binding.userLoginEditText
        if (!loginDisplayName.isNullOrEmpty()) userLogin.text.append(loginDisplayName)
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
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)

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
                    loginViewModel.loginLiveData.observe(viewLifecycleOwner){
                        binding.root
                    }
                    registrationButton.isEnabled = true
                }
            }
        }

        loginViewModel.loginLiveData.observe(viewLifecycleOwner){
            binding.root
        }

        loginViewModel.loginResult.observe(viewLifecycleOwner) {
            binding.root
        }

        registrationButton.setOnClickListener {
//            val login = if (userLogin.text.isNotBlank() && userPasswd.text.isNotBlank()) {
//                loginViewModel.edited.value?.copy(
//                    displayName = userLogin.text.toString(),
//                    passwd = userPasswd.text.toString(),
//                    avatar = ""
//                )
//                } else {
//                    loginViewModel.edited.value?.let { login ->
//                    if (login.userId == 0L) login else return@setOnClickListener
//                    }
//                }

            val login = Login(
                    userId = 0L,
                    displayName = userLogin.text.toString(),
                    passwd = userPasswd.text.toString(),
                    avatar = imageAddress.toString()
            )
            if (login != null) {
                loginViewModel.loginChanged(login)
            }
            loginViewModel.saveLogin()
//            if (login != null) {
//                findNavController().navigate(
//                    R.id.action_userRegistrationFragment_to_loginFragment,
//                    bundleOf(AndroidUtils.LOGIN_KEY to login)
//                )
//            }
            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode== REQUEST_CODE && resultCode == Activity.RESULT_OK){
            imageAddress = data?.data
            userAvatarImageButton.setImageURI(imageAddress)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }
}