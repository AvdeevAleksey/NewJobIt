package ru.netology.newjobit.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import ru.netology.newjobit.R
import ru.netology.newjobit.view.adapter.countMyClick
import ru.netology.newjobit.databinding.FragmentCardPostBinding
import ru.netology.newjobit.model.dto.Post
import ru.netology.newjobit.utils.AndroidUtils
import ru.netology.newjobit.utils.AndroidUtils.LOGIN_KEY
import ru.netology.newjobit.utils.AndroidUtils.POST_KEY
import ru.netology.newjobit.viewmodel.PostViewModel

class CardPostFragment : Fragment() {

    private val postViewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentCardPostBinding.inflate(inflater, container, false)

        val postId = arguments?.getParcelable<Post>(POST_KEY)?.id
        val loginId = arguments?.getLong(LOGIN_KEY)?: 0L
        postViewModel.postLiveData.map { posts ->
            posts.find { it.id == postId }
        }.observe(viewLifecycleOwner) { post ->
            post ?: run {
                findNavController().navigateUp()
                return@observe
            }

            binding.apply {
                if (!post.avatar.isNullOrBlank()) avatarImageView.setImageURI(Uri.parse(post.avatar))
                authorTextView.text = post.author
                publishedTextView.text = post.published
                contentTextView.text = post.content
                videoContent.setImageURI(Uri.parse(post.videoInPost))
                shareImageButton.text = countMyClick(post.shareCount)
                viewsImageButton.text = countMyClick(post.viewingCount)
                likeImageButton.isChecked = post.likedByMe
                likeImageButton.text = countMyClick(post.likesCount)
                videoGroup.isVisible = post.videoInPost.isNotBlank()

                playVideoButton.setOnClickListener {
                    playVideo(post)
                }
                videoGroup.setOnClickListener {
                    playVideo(post)
                }
                likeImageButton.setOnClickListener {
                    postViewModel.likeById(post.id)
                }
                shareImageButton.setOnClickListener {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plane"
                    }
                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)

                    postViewModel.shareById(post.id)
                }
                viewsImageButton.setOnClickListener {
                    postViewModel.viewingById(post.id)
                }
                postMenuImageView.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.options_post_menu)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.postRemove -> {
                                    postViewModel.removeById(post.id)
                                    true
                                }
                                R.id.postEdit -> {
                                    findNavController().navigate(
                                        R.id.action_fragmentCardPost_to_postFragment,
                                        bundleOf(
                                            POST_KEY to post,
                                            LOGIN_KEY to loginId
                                        )
                                    )
                                    postViewModel.editPost(post)
                                    true
                                }
                                else -> false
                            }

                        }
                    }.show()
                }
            }
        }

        return binding.root
    }

    private fun playVideo(post: Post) {
        val intent = Intent().apply {
            Intent.ACTION_VIEW
            data = Uri.parse(post.videoInPost)
        }
        val playVideoIntent = Intent.createChooser(intent, getString(R.string.play_video_app_chooser))
        startActivity(playVideoIntent)
    }
}