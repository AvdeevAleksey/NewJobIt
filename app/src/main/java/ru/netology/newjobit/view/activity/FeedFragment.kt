package ru.netology.newjobit.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.newjobit.view.adapter.OnInteractionListener
import ru.netology.newjobit.view.adapter.PostsAdapter
import ru.netology.newjobit.model.dto.Post
import ru.netology.newjobit.utils.AndroidUtils.POST_KEY
import ru.netology.newjobit.viewmodel.PostViewModel
import ru.netology.newjobit.R
import ru.netology.newjobit.databinding.FragmentFeedBinding
import ru.netology.newjobit.utils.AndroidUtils
import ru.netology.newjobit.viewmodel.LoginViewModel

class FeedFragment : Fragment() {

    private val postViewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
    private val loginViewModel: LoginViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

        val loginId = arguments?.getLong(AndroidUtils.LOGIN_KEY)
        val login = loginViewModel.loginLiveData.value?.find {
                it.userId == loginId
                }

        if (login != null) {
            Toast.makeText(context,getString(R.string.welcome) + login.displayName, Toast.LENGTH_SHORT).show()
        }
        arguments?.remove(AndroidUtils.LOGIN_KEY)
        val binding = FragmentFeedBinding.inflate(inflater,container,false)
        val postsAdapter = PostsAdapter (object : OnInteractionListener {
            override fun onLike(post: Post) {
                postViewModel.likeById(post.id)
            }
            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT,post.content)
                    type = "text/plain"
                }
                val shareIntent =
                        Intent.createChooser(intent,getString(R.string.chooser_share_post))
                startActivity(shareIntent)
                postViewModel.shareById(post.id)
            }
            override fun onViewing(post: Post) {
                postViewModel.viewingById(post.id)
            }
            override fun onPostEdit(post: Post) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_postFragment,
                    bundleOf("post" to post)
                )
                postViewModel.editPost(post)
            }
            override fun onPostRemove(post: Post) {
                postViewModel.removeById(post.id)
            }
            override fun onPlayVideo(post: Post) {
                val intent = Intent().apply {
                    Intent.ACTION_VIEW
                    data = Uri.parse(post.videoInPost)
                }
                val playVideoIntent = Intent.createChooser(intent,getString(R.string.play_video_app_chooser))
                startActivity(playVideoIntent)
            }

            override fun onPostOpen(post: Post) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_fragmentCardPost,
                    bundleOf(POST_KEY to post)
                )
            }
        })

        binding.rvPostRecyclerView.adapter = postsAdapter

        postViewModel.postLiveData.observe(viewLifecycleOwner) { posts ->
            postsAdapter.submitList(posts)
        }

        postViewModel.edited.observe(viewLifecycleOwner) { post ->
            if (post.id == 0L) {
                return@observe
            }
        }

        binding.fabAddPost.setOnClickListener {
//            val post: Post = viewModel.edited.value.let { post ->
//                if (post?.id == 0L) post.copy(author = login?.displayName ?: "Anonymous") else return@setOnClickListener
//            }
            val post: Post? = postViewModel.edited.value?.copy(author = login?.displayName ?: "Anonymous")
            findNavController().navigate(
                R.id.action_feedFragment_to_postFragment,
                bundleOf(POST_KEY to post)
            )
        }
        return binding.root
    }
}
