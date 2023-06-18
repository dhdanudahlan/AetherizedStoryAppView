package com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.dicoding.aetherized.aetherizedstoryappview.R
import com.dicoding.aetherized.aetherizedstoryappview.data.model.user.LoginResult
import com.dicoding.aetherized.aetherizedstoryappview.data.repository.StoryRepository
import com.dicoding.aetherized.aetherizedstoryappview.di.Injection
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.MyApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {


    private val preferenceDataStore by lazy { (requireActivity().application as MyApplication).customPreference }
    private val loginResultFlow = MutableStateFlow<LoginResult?>(null)

    private val storyRepository: StoryRepository by lazy {
        val loginResult = loginResultFlow.value
        if (loginResult != null) {
            Injection().provideRepository(requireContext(), loginResult)
        } else {
            throw IllegalStateException("LoginResult is not available.")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        lifecycleScope.launch {
            preferenceDataStore.loginResultFlow.collect { loginResult ->
                loginResultFlow.value = loginResult
            }
        }
        val userName = view.findViewById<TextView>(R.id.profile_name)
        val userId = view.findViewById<TextView>(R.id.profile_userId)
        val userToken = view.findViewById<TextView>(R.id.profile_token)
        userName.text = loginResultFlow.value?.name
        userId.text = loginResultFlow.value?.userId
        userToken.text = loginResultFlow.value?.token

        return view
    }

}