package com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.feeds.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.aetherized.aetherizedstoryappview.model.story.Story
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.CustomPreference

class DetailsViewModel(private val preferenceDataStore: CustomPreference) : ViewModel() {
    private val _story = MutableLiveData<List<Story>>()
    val story: LiveData<List<Story>> get() = _story

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message
}