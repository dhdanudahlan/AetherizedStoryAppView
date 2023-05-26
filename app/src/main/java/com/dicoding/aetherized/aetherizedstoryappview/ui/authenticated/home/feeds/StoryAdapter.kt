package com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.feeds

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.aetherized.aetherizedstoryappview.R
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.Constants
import com.dicoding.aetherized.aetherizedstoryappview.data.model.Story
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.story.StoryFragment

class StoryAdapter (private var storyList: ArrayList<Story>) :
    RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    fun updateList(stories: List<Story>) {
        val diffCallback = StoryDiffCallback(this.storyList, stories)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        storyList.clear()
        storyList.addAll(stories)

        Log.d("StoryAdapter", "NEWEST STORY ID: ${stories[0].id}")
        diffResult.dispatchUpdatesTo(this)
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.linear_layout_list_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val story = storyList[position]

        viewHolder.bind(story)
    }

    override fun getItemCount(): Int = storyList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val storyImage: ImageView = itemView.findViewById(R.id.iv_item_story)
        private val userImage: ImageView = itemView.findViewById(R.id.iv_item_avatar)
        private val name: TextView = itemView.findViewById(R.id.tv_item_name)
        private val name_top: TextView = itemView.findViewById(R.id.tv_item_name_top)
        private val storyDesc: TextView = itemView.findViewById(R.id.tv_item_desc)

        fun bind(story: Story) {
            Glide.with(itemView).load(R.drawable.ic_baseline_person_24).into(userImage)
            Glide.with(itemView).load(story.photoUrl).into(storyImage)
            Log.d("StoryAdapter", story.photoUrl)
            name.text = story.name
            name_top.text = story.name
            storyDesc.text = story.createdAt
            itemView.setOnClickListener {
                val bundle = Bundle().apply {
                    putParcelable(Constants.PARCELABLE_KEY, story)
                }

                val storyFragment = StoryFragment().apply {
                    arguments = bundle
                }

                val transaction = (itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, storyFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }
    }

    class StoryDiffCallback(private val oldList: List<Story>, private val newList: List<Story>) :
        DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldStory = oldList[oldItemPosition]
            val newStory = newList[newItemPosition]
            return oldStory.id == newStory.id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldStory = oldList[oldItemPosition]
            val newStory = newList[newItemPosition]
            return oldStory == newStory
        }
    }
}