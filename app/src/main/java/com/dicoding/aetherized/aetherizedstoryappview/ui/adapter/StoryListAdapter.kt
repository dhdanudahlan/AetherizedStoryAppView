package com.dicoding.aetherized.aetherizedstoryappview.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.aetherized.aetherizedstoryappview.R
import com.dicoding.aetherized.aetherizedstoryappview.databinding.LinearLayoutListItemBinding
import com.dicoding.aetherized.aetherizedstoryappview.model.story.Story
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.feeds.details.DetailsFragment
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.Constants

class StoryListAdapter :
    PagingDataAdapter<Story, StoryListAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LinearLayoutListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }
    class ViewHolder(binding: LinearLayoutListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val storyImage: ImageView = binding.ivItemStory
        private val userImage: ImageView = binding.ivItemAvatar
        private val name: TextView = binding.tvItemName
        private val nameTop: TextView = binding.tvItemNameTop
        private val storyDesc: TextView = binding.tvItemDesc

        fun bind(story: Story) {
            Glide.with(itemView).load(R.drawable.ic_baseline_person_24).into(userImage)
            Glide.with(itemView).load(story.photoUrl).into(storyImage)
            name.text = story.name
            nameTop.text = story.name
            storyDesc.text = story.createdAt
            itemView.setOnClickListener {
                val bundle = Bundle().apply {
                    putParcelable(Constants.PARCELABLE_KEY, story)
                }

                val detailsFragment = DetailsFragment().apply {
                    arguments = bundle
                }

                val transaction = (itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, detailsFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
        }
    }
}

