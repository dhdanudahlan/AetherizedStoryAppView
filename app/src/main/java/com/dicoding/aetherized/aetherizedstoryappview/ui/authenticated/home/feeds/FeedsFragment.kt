package com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.feeds

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.aetherized.aetherizedstoryappview.R
import com.dicoding.aetherized.aetherizedstoryappview.data.model.story.Story
import com.dicoding.aetherized.aetherizedstoryappview.data.model.user.LoginResult
import com.dicoding.aetherized.aetherizedstoryappview.data.repository.StoryRepository
import com.dicoding.aetherized.aetherizedstoryappview.di.Injection
import com.dicoding.aetherized.aetherizedstoryappview.ui.adapter.LoadingStateAdapter
import com.dicoding.aetherized.aetherizedstoryappview.ui.adapter.StoryListAdapter
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.feeds.maps.MapsActivity
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.settings.SettingsViewModel
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.MyApplication
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.ViewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FeedsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var locationSwitch: SwitchCompat
    private lateinit var viewMap: ImageButton

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

    private val viewModelFactory by lazy { ViewModelFactory(preferenceDataStore, storyRepository) }
    private val viewModel by viewModels<FeedsPagingViewModel> { viewModelFactory }
    private val settingsViewModel by viewModels<SettingsViewModel> { viewModelFactory }
    private lateinit var loginResult: LoginResult

    private var storyList: ArrayList<Story> = ArrayList()

    private lateinit var view: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view =  inflater.inflate(R.layout.fragment_feeds, container, false)
        lifecycleScope.launch {
            preferenceDataStore.loginResultFlow.collect { loginResult ->
                loginResultFlow.value = loginResult
                Log.d("FeedFragment", "Token: ${loginResult.token}")
            }
        }
        initializeView()
        initializeValue()
        observeSettings()
        viewMap.setOnClickListener {
            Log.d("FeedsFragment", "storyList Size: ${storyList.size}")
            val intent = Intent(activity, MapsActivity::class.java)
            intent.putExtra("storyList", ArrayList(storyList))
            startActivity(intent)
        }

        return view
    }

    private fun initializeView() {
        recyclerView = view.findViewById(R.id.rvStory)
        locationSwitch = view.findViewById(R.id.locationSwitch)
        viewMap = view.findViewById(R.id.show_Map)
        viewMap.isEnabled = false
    }
    private fun initializeValue() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        getData()
    }

    private fun observeSettings() {
        settingsViewModel.loginResultLiveData.observe(viewLifecycleOwner) { newLoginResult ->
            loginResult = newLoginResult ?: LoginResult()
        }
    }
    private fun getData() {
        val storyAdapter = StoryListAdapter()
        recyclerView.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )
        loadStories(storyAdapter, false)

        locationSwitch.setOnCheckedChangeListener { _, isChecked ->
            loadStories(storyAdapter, isChecked)
            viewMap.isEnabled = true
        }
    }

    private fun loadStories(storyAdapter: StoryListAdapter, enableLocation: Boolean) {
        viewModel.getStories(enableLocation).observe(viewLifecycleOwner) { pagingData ->
            storyList = ArrayList()
            storyAdapter.submitData(lifecycle, pagingData)
            storyList = storyAdapter.getListData()
        }
    }
}