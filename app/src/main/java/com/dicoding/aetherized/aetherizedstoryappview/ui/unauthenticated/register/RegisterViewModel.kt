package com.dicoding.aetherized.aetherizedstoryappview.ui.unauthenticated.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.CustomPreference
import com.dicoding.aetherized.aetherizedstoryappview.data.model.user.User
import com.dicoding.aetherized.aetherizedstoryappview.data.remote.response.GeneralResponse
import com.dicoding.aetherized.aetherizedstoryappview.data.remote.ApiConfig
import kotlinx.coroutines.launch

class RegisterViewModel(private val preferenceDataStore: CustomPreference) : ViewModel() {
    private val apiService = ApiConfig.getApiService()

    private val _response = MutableLiveData<GeneralResponse>()
    val response: LiveData<GeneralResponse> get() = _response

    fun register(user: User, onResult: (GeneralResponse) -> Unit) {
        viewModelScope.launch {
            try {
                val requestBody = mapOf(
                    "name" to user.name,
                    "email" to user.email,
                    "password" to user.password
                )

                _response.value = apiService.register(requestBody)
                Log.d("RegisterActivity", "==========SUCCESS==========")
                response.value?.let { onResult(it) }
            } catch (exception: Exception) {
                Log.d("RegisterActivity", "==========FAILED==========")
                response.value?.let { onResult(it) }
            }
        }
    }
}