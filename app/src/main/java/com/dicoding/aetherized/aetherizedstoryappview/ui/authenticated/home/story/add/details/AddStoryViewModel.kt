package com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.story.add.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.aetherized.aetherizedstoryappview.data.model.user.LoginResult
import com.dicoding.aetherized.aetherizedstoryappview.data.response.GeneralResponse
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.CustomPreference
import com.dicoding.aetherized.aetherizedstoryappview.util.network.ApiConfig
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryViewModel(private val preferenceDataStore: CustomPreference) : ViewModel() {
    private val apiService = ApiConfig.getApiService()

    private val _response = MutableLiveData<GeneralResponse>()
    val response: LiveData<GeneralResponse> get() = _response

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun addNewStory(imageFile: File, description: String, latitude: Double, longitude: Double, loginResult: LoginResult, onResult: (GeneralResponse) -> Unit) {
        viewModelScope.launch {
            val imageRequestBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imagePart =
                MultipartBody.Part.createFormData("photo", imageFile.name, imageRequestBody)
            val descriptionRequestBody = description.toRequestBody()
            val latitudeRequestBody = latitude.toString().toRequestBody()
            val longitudeRequestBody = longitude.toString().toRequestBody()

            if (loginResult.token != "GUEST") {
                viewModelScope.launch {
                    val token = "Bearer ${loginResult.token}"
                    try {
                        val responseTemp = apiService.addNewStory(token, descriptionRequestBody, imagePart, latitudeRequestBody, longitudeRequestBody)
                        responseTemp.let { _response.postValue(it) }
                        onResult(responseTemp)
                    } catch (exception: Exception) {
                        Log.d("FeedsFragment", "==========FAILED TO GET STORIES========== ${exception.message}")
                        onResult(GeneralResponse(true, exception.message ?: "EXCEPTION ERROR"))
                    }
                }
            } else {
                viewModelScope.launch {
                    try {
                        val responseTemp = apiService.addNewStoryGuest(descriptionRequestBody, imagePart, latitudeRequestBody, longitudeRequestBody)
                        responseTemp.let { _response.postValue(it) }
                        onResult(responseTemp)
                    } catch (exception: Exception) {
                        Log.d("FeedsFragment", "==========FAILED TO GET STORIES========== ${exception.message}")
                        onResult(GeneralResponse(true, exception.message ?: "EXCEPTION ERROR"))
                    }
                }
            }
        }
    }

}