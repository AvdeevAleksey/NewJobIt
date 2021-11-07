package ru.netology.newjobit.view.activity

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.map
import ru.netology.newjobit.databinding.FragmentUserRegistrationBinding
import ru.netology.newjobit.viewmodel.LoginViewModel
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_user_registration.*
import ru.netology.newjobit.R
import ru.netology.newjobit.model.dto.Login
import ru.netology.newjobit.utils.AndroidUtils
import ru.netology.newjobit.utils.AndroidUtils.LOGIN_KEY
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
            val permissionStatus =
                this.context?.let { ContextCompat.checkSelfPermission(it,READ_EXTERNAL_STORAGE) }

            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Clicked on avatar",Toast.LENGTH_SHORT).show()
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            } else {
                ActivityCompat.requestPermissions(
                    context as Activity, arrayOf(READ_EXTERNAL_STORAGE),
                    REQUEST_CODE)
            }
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

        registrationButton.setOnClickListener {
            val login = Login(
                userId = 0L,
                displayName = userLogin.text.toString(),
                passwd = userPasswd.text.toString(),
                avatar = if (imageAddress!=null) imageAddress.toString() else ""
            )
            loginViewModel.loginChanged(login)
            loginViewModel.saveLogin()
            imageAddress = null
            findNavController().navigate(
                R.id.action_userRegistrationFragment_to_loginFragment,
                bundleOf(LOGIN_KEY to login)
            )
        }
        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode== REQUEST_CODE && resultCode == Activity.RESULT_OK){
            imageAddress = data?.data
            Glide.with(this).load(imageAddress).into(userAvatarImageButton)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }
}