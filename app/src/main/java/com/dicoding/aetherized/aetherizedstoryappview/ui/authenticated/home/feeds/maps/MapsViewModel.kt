package com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.feeds.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.aetherized.aetherizedstoryappview.data.repository.StoryRepository
import com.dicoding.aetherized.aetherizedstoryappview.model.story.Story
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getLocalStoryList(): List<Story> {
        var storyList = emptyList<Story>()
        viewModelScope.launch(Dispatchers.IO) {
            storyList = storyRepository.getStoryList()
        }
        return storyList
    }

}