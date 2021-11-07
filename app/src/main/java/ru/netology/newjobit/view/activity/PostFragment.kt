package ru.netology.newjobit.view.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.newjobit.R
import ru.netology.newjobit.databinding.FragmentPostBinding
import ru.netology.newjobit.model.dto.Post
import ru.netology.newjobit.utils.AndroidUtils
import ru.netology.newjobit.utils.AndroidUtils.LOGIN_KEY
import ru.netology.newjobit.utils.AndroidUtils.POST_KEY
import ru.netology.newjobit.viewmodel.LoginViewModel
import ru.netology.newjobit.viewmodel.PostViewModel

class PostFragment : Fragment() {

    private val postViewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
    private val loginViewModel: LoginViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPostBinding.inflate(inflater, container, false)
        binding.editPostContent.requestFocus()

        val post : Post? = arguments?.getParcelable<Post>(POST_KEY)
        val loginId = arguments?.getLong(LOGIN_KEY)?: 0L
        val login = loginViewModel.loginLiveData.value?.find {
            it.userId == loginId
        }?: loginViewModel.getLoginById(loginId)
        arguments?.remove(POST_KEY)
        arguments?.remove(LOGIN_KEY)

        with(binding.editPostContent) {
            if (post != null && post.content.isNotBlank()) {
                text.append(post.content)
            } else {
                text.append("")
            }
        }

        binding.fabOk.setOnClickListener {
            if (!binding.editPostContent.text.isNullOrBlank()) {
                val content = binding.editPostContent.text.toString()
                if (login != null) {
                    postViewModel.changeContent(login.avatar,login.displayName,content)
                }
                postViewModel.savePost()
            }
            val loginId = login.userId
            AndroidUtils.hideKeyboard(binding.root)
            findNavController().navigate(
                R.id.action_postFragment_to_feedFragment,
                bundleOf(LOGIN_KEY to loginId)
            )
        }
        return binding.root
    }
}