package com.dicoding.aetherized.aetherizedstoryappview.ui.unauthenticated.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.CustomPreference
import com.dicoding.aetherized.aetherizedstoryappview.data.model.user.LoginResult
import com.dicoding.aetherized.aetherizedstoryappview.data.model.user.User
import com.dicoding.aetherized.aetherizedstoryappview.data.response.UserResponse
import com.dicoding.aetherized.aetherizedstoryappview.util.network.ApiConfig
import kotlinx.coroutines.launch

class LoginViewModel(private val preferenceDataStore: CustomPreference): ViewModel() {
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

                val responseTemp = apiService.login(requestBody)
                _response.postValue(responseTemp)
                responseTemp.let {
                    _loginResult.postValue(it.data)
                    _errorMessage.postValue(it.message)
                    onResult(it)
                }
                if (!responseTemp.error) {
                    responseTemp.data?.let { loginResult ->
                        saveLogin(loginResult)
                    }
                }
            } catch (exception: Exception) {
                Log.d("LoginViewModelExc", "==========FAILED========== ${exception}")
                response.value?.let { onResult(it) }
                _errorMessage.postValue("An error occurred: ${exception.message}")
            }
        }
    }
    fun loginGuest(onResult: (LoginResult)  -> Unit) {
        viewModelScope.launch {
            try {
                val guestLoginResult = LoginResult("GUEST", "GUEST", "GUEST")
                _loginResult.postValue(guestLoginResult)
                _errorMessage.postValue("lOGGED IN AS GUEST")
                Log.d("LoginViewModel", "LOGGED IN AS GUEST")
                saveLogin(guestLoginResult)
                onResult(guestLoginResult)
            } catch (exception: Exception) {
                val guestLoginResult = LoginResult("GUEST", "GUEST", "GUEST")
                Log.d("LoginViewModelExc", "==========FAILED========== ${exception}")
                onResult(guestLoginResult)
                _errorMessage.postValue("An error occurred: ${exception.message}")
            }
        }
    }

    private fun saveLogin(loginResult: LoginResult) {
        viewModelScope.launch {
            preferenceDataStore.saveLogin(loginResult)
        }
    }

}