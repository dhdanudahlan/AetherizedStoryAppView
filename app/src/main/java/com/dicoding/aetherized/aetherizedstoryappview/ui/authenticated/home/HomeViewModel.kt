package com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.aetherized.aetherizedstoryappview.data.model.story.Story
import com.dicoding.aetherized.aetherizedstoryappview.data.model.user.LoginResult
import com.dicoding.aetherized.aetherizedstoryappview.data.remote.ApiConfig
import com.dicoding.aetherized.aetherizedstoryappview.data.remote.response.UserResponse
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.CustomPreference

class  HomeViewModel(private val preferenceDataStore: CustomPreference) : ViewModel() {
    private val apiService = ApiConfig.getApiService()

    private val _loginResult = MutableLiveData<LoginResult?>()
    val loginResult: LiveData<LoginResult?> get() = _loginResult

    private val _response = MutableLiveData<UserResponse<LoginResult>>()
    val response: LiveData<UserResponse<LoginResult>> get() = _response

    private val _listStory = MutableLiveData<List<Story>>()
    val listStory: LiveData<List<Story>> get() = _listStory

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage


}