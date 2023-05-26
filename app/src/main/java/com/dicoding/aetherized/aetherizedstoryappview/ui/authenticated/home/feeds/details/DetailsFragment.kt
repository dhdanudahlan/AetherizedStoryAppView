package com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.feeds.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.aetherized.aetherizedstoryappview.R
import com.dicoding.aetherized.aetherizedstoryappview.data.model.Story
import com.dicoding.aetherized.aetherizedstoryappview.data.model.User
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.feeds.FeedsViewModel
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.settings.SettingsViewModel
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.Constants
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.CustomPreference
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.ViewModelFactory

class DetailsFragment : Fragment() {
    private lateinit var view: View
    private lateinit var story: Story


    private val viewModelFactory by lazy { ViewModelFactory(CustomPreference(requireContext())) }
    private val viewModel by viewModels<DetailsViewModel> { viewModelFactory }
    private val feedsViewModel by viewModels<FeedsViewModel> { viewModelFactory }
    private val settingsViewModel by viewModels<SettingsViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_details, container, false)

        @Suppress("DEPRECATION")
        story = arguments?.getParcelable(Constants.PARCELABLE_KEY)!!
        bindUser()
        return view
    }
    private fun bindUser(){
        view.findViewById<TextView>(R.id.tv_item_name).text = story.name
        view.findViewById<TextView>(R.id.tv_item_name_top).text  = story.name
        view.findViewById<TextView>(R.id.tv_item_time).text = story.createdAt
        view.findViewById<TextView>(R.id.tv_item_desc).text = story.description
        Glide.with(this).load(R.drawable.ic_baseline_person_24).into(view.findViewById(R.id.iv_item_avatar))
        Glide.with(this).load(story.photoUrl).into(view.findViewById(R.id.iv_item_story))
    }

}