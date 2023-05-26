package com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.feeds

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.aetherized.aetherizedstoryappview.R
import com.dicoding.aetherized.aetherizedstoryappview.data.model.LoginResult
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.settings.SettingsViewModel
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.CustomPreference
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.ViewModelFactory

private val Context.dataStore by preferencesDataStore(name = "settings")

class FeedsFragment : Fragment() {

    private lateinit var storyAdapter: StoryAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private val viewModelFactory by lazy { ViewModelFactory(CustomPreference(requireContext())) }
    private val viewModel by viewModels<FeedsViewModel> { viewModelFactory }
    private val settingsViewModel by viewModels<SettingsViewModel> { viewModelFactory }
    private lateinit var loginResult: LoginResult

    private lateinit var view: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_feeds, container, false)

        observeSettings()
        observeViewModel()
        initializeValue()



        recyclerView.adapter = storyAdapter

        return view
    }

    private fun initializeValue() {
        loginResult = LoginResult("GUEST","GUEST","GUEST")

        storyAdapter = StoryAdapter(ArrayList())

        progressBar = view.findViewById(R.id.progressBar)
        recyclerView = view.findViewById(R.id.rvStory)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeSettings() {
        settingsViewModel.loginResultLiveData.observe(viewLifecycleOwner) { newLoginResult ->
            loginResult = newLoginResult ?: LoginResult("GUEST","GUEST","GUEST")
            viewModel.getAllStories(loginResult)
        }
    }

    private fun observeViewModel() {
        viewModel.listStory.observe(viewLifecycleOwner) {
            Log.d("FeedsFragment", "NEWEST PHOTO URL: ${it[0].photoUrl}")
            storyAdapter.updateList(it)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}