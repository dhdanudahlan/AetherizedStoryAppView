package com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.feeds

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.CustomPreference
import com.dicoding.aetherized.aetherizedstoryappview.data.model.LoginResult
import com.dicoding.aetherized.aetherizedstoryappview.data.model.Story
import com.dicoding.aetherized.aetherizedstoryappview.data.response.StoriesResponse
import com.dicoding.aetherized.aetherizedstoryappview.util.network.ApiConfig
import kotlinx.coroutines.launch

class FeedsViewModel (private val pref: CustomPreference) : ViewModel() {
    private val apiService = ApiConfig.getApiService()

    private val _loginResult = MutableLiveData<LoginResult?>()
    val loginResult: LiveData<LoginResult?> get() = _loginResult

    private val _response = MutableLiveData<StoriesResponse>()
    val response: LiveData<StoriesResponse> get() = _response

    private val _listStory = MutableLiveData<List<Story>>()
    val listStory: LiveData<List<Story>> get() = _listStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage


    companion object {
        private const val TAG = "FeedsViewModel"
    }

    init {
        _isLoading.postValue(true)
    }

    fun getAllStories(login: LoginResult) {
        _isLoading.postValue(false)
        _loginResult.postValue(login)
        viewModelScope.launch {
            val token = "Bearer ${login.token}"
            val page = 1
            val size = 20
            try {
                val responseTemp = apiService.getAllStories(token, page, size)
                _response.postValue(responseTemp)
                responseTemp?.let { _listStory.postValue(it.data!!) }
            } catch (exception: Exception) {
                Log.d("FeedsFragment", "==========FAILED TO GET STORIES========== ${exception.toString()}")
            }
        }
    }
}