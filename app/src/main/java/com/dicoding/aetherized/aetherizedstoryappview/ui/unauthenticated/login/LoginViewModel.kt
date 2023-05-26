package com.dicoding.aetherized.aetherizedstoryappview.ui.unauthenticated.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.CustomPreference
import com.dicoding.aetherized.aetherizedstoryappview.data.model.LoginResult
import com.dicoding.aetherized.aetherizedstoryappview.data.model.User
import com.dicoding.aetherized.aetherizedstoryappview.data.response.UserResponse
import com.dicoding.aetherized.aetherizedstoryappview.util.network.ApiConfig
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: CustomPreference): ViewModel() {
    private val apiService = ApiConfig.getApiService()

    private val _loginResult = MutableLiveData<LoginResult?>()
    val loginResult: LiveData<LoginResult?> get() = _loginResult

    private val _response = MutableLiveData<UserResponse<LoginResult>>()
    val response: LiveData<UserResponse<LoginResult>> get() = _response

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun login(email: String, password: String, onResult: (UserResponse<LoginResult>) -> Unit) {
        viewModelScope.launch {
            try {
                val requestBody = mapOf(
                    "email" to email,
                    "password" to password
                )

                _response.value = apiService.login(requestBody)
                response.value?.let {
                    _loginResult.value = it.data
                    if (it.data != null) { saveLogin(it.data) }
                    onResult(it)
                }
            } catch (exception: Exception) {
                Log.d("LoginActivity", "==========FAILED==========")
                response.value?.let { onResult(it) }
            }
        }
    }

    private fun saveLogin(loginResult: LoginResult) {
        viewModelScope.launch {
            pref.saveLogin(loginResult)
        }
    }

}