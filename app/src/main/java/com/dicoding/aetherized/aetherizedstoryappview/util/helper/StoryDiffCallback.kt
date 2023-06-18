package com.dicoding.aetherized.aetherizedstoryappview.util.helper

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.aetherized.aetherizedstoryappview.model.story.Story

class StoryDiffCallback(private val mOldStoryList: List<Story>, private val mNewStoryList: List<Story>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldStoryList.size
    }

    override fun getNewListSize(): Int {
        return mNewStoryList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldStoryList[oldItemPosition].id == mNewStoryList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldStoryList[oldItemPosition]
        val newEmployee = mNewStoryList[newItemPosition]
        return oldEmployee.name == newEmployee.name && oldEmployee.description == newEmployee.description  && oldEmployee.photoUrl == newEmployee.photoUrl  && oldEmployee.id == newEmployee.id
    }
}