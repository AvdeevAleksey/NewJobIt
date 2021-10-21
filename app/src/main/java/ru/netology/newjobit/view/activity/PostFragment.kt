package ru.netology.newjobit.view.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.newjobit.databinding.FragmentPostBinding
import ru.netology.newjobit.model.dto.Post
import ru.netology.newjobit.utils.AndroidUtils
import ru.netology.newjobit.utils.AndroidUtils.POST_KEY
import ru.netology.newjobit.viewmodel.PostViewModel

class PostFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPostBinding.inflate(inflater, container, false)
        binding.editPostContent.requestFocus()

        val post : Post? = arguments?.getParcelable<Post>(POST_KEY)
        arguments?.remove(POST_KEY)

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
                viewModel.changeContent(content)
                viewModel.savePost()
            }
            AndroidUtils.hideKeyboard(binding.root)
            findNavController().navigateUp()
        }
        return binding.root
    }
}