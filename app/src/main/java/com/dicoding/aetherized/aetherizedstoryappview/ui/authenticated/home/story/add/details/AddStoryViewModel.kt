package com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.story.add.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.aetherized.aetherizedstoryappview.data.response.UserResponse
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.CustomPreference
import com.dicoding.aetherized.aetherizedstoryappview.util.network.ApiConfig
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AddStoryViewModel(private val pref: CustomPreference) : ViewModel() {
    private val apiService = ApiConfig.getApiService()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun addNewStory(imageFile: File, description: String, onResult: (UserResponse<String>) -> Unit) {
        viewModelScope.launch {
//            val auth = pref.getLogin()
//            val token = auth.asLiveData().value?.token ?: ""
//            val imageRequestBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
//            val imagePart =
//                MultipartBody.Part.createFormData("image", imageFile.name, imageRequestBody)



//
//            if (token != "") {
//                apiService.addNewStory(token, description, imagePart).enqueue(object:
//                    Callback<UserResponse<String>> {
//                    override fun onResponse(
//                        call: Call<UserResponse<String>>,
//                        response: Response<UserResponse<String>>
//                    ) {
//                        _isLoading.postValue(false)
//                        if (response.isSuccessful) {
//
//                        } else  {
//
//                        }
//                    }
//                    override fun onFailure(call: Call<UserResponse<String>>, t: Throwable) {
//                        _errorMessage.postValue("An error occurred while fetching data. Please check your internet connection and try again.")
//                    }
//                })
//            } else {
//                apiService.addNewStoryGuest(description, imagePart).enqueue(object:
//                    Callback<UserResponse<String>> {
//                    override fun onResponse(
//                        call: Call<UserResponse<String>>,
//                        response: Response<UserResponse<String>>
//                    ) {
//                        _isLoading.postValue(false)
//                        if (response.isSuccessful) {
//
//                        } else  {
//
//                        }
//                    }
//                    override fun onFailure(call: Call<UserResponse<String>>, t: Throwable) {
//                        _errorMessage.postValue("An error occurred while fetching data. Please check your internet connection and try again.")
//                    }
//                })
//            }
        }
    }

}