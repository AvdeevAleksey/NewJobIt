package ru.netology.newjobit.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.newjobit.R
import ru.netology.newjobit.databinding.FragmentCardPostBinding
import ru.netology.newjobit.model.dto.Post

interface OnInteractionListener {
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onViewing(post: Post)
    fun onPostEdit(post: Post)
    fun onPostRemove(post: Post)
    fun onPlayVideo(post: Post)
    fun onPostOpen(post: Post)
}

class PostsAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = FragmentCardPostBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class PostDiffCallBack : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}

class PostViewHolder(
    private val binding: FragmentCardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root)  {
    fun bind(post: Post) {
        binding.apply {
            Glide.with(avatarImageView).load(Uri.parse(post.avatar)).into(avatarImageView)
            authorTextView.text = post.author
            publishedTextView.text = post.published
            contentTextView.text = post.content
            videoContent.setImageURI(Uri.parse(post.videoInPost))
            shareImageButton.text = countMyClick(post.shareCount)
            viewsImageButton.text = countMyClick(post.viewingCount)
            likeImageButton.isChecked = post.likedByMe
            likeImageButton.text = countMyClick(post.likesCount)
            videoGroup.isVisible = post.videoInPost.isNotBlank()

            cardPost.setOnClickListener {
                onInteractionListener.onPostOpen(post)
            }
            playVideoButton.setOnClickListener {
                onInteractionListener.onPlayVideo(post)
            }
            videoGroup.setOnClickListener {
                onInteractionListener.onPlayVideo(post)
            }
            likeImageButton.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            shareImageButton.setOnClickListener {
                onInteractionListener.onShare(post)
            }
            viewsImageButton.setOnClickListener {
                onInteractionListener.onViewing(post)
            }
            postMenuImageView.setOnClickListener {
                PopupMenu(it.context,it).apply {
                    inflate(R.menu.options_post_menu)
                    setOnMenuItemClickListener { item ->
                        when(item.itemId) {
                            R.id.postRemove -> {
                                onInteractionListener.onPostRemove(post)
                                true
                            }
                            R.id.postEdit -> {
                                onInteractionListener.onPostEdit(post)
                                true
                            }
                            else -> false
                        }

                    }
                }.show()
            }
        }
    }
}

fun dischargesReduction(click: Int, t: Int = 1000): String {
    return when (click) {
        in t until t*t -> "k"
        else -> "M"
    }
}

fun countMyClick(click:Int, t:Int = 1000): String {
    return when (click) {
        in 0 until t -> click.toString()
        in click/t%10*t until click/t%10*t+100 -> "${click/t%10}${dischargesReduction(click)}"
        in click/t%10*t+100 until click/t%10*t+t -> "${click/t%10},${click/100-click/t%10*10}${dischargesReduction(click)}"
        in click/t%10*t until click/t%100*t+t -> "${click/t%100}${dischargesReduction(click)}"
        in click/t%100*t until click/t%1000*t+t -> "${click/t%1000}${dischargesReduction(click)}"
        in click/(t*t)%10*(t*t)+t*100 until click/(t*t)%10*(t*t)+(t*t) -> "${click/(t*t)%10},${click/(t*100)-click/(t*t)%10*10}${dischargesReduction(click)}"
        else -> "${click/(t*t)}${dischargesReduction(click)}"
    }
}