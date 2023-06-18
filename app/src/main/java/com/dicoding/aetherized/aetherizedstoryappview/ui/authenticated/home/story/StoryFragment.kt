package com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.story

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.dicoding.aetherized.aetherizedstoryappview.R
import com.dicoding.aetherized.aetherizedstoryappview.model.story.Story
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.story.add.details.AddStoryActivity
import java.io.File

class StoryFragment : Fragment() {

    private lateinit var view: View
    private lateinit var story: Story


    private var capturedImageFile: File? = null
    private val startAddStoryActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val capturedImageFilePath = result.data?.getStringExtra("capturedImageFile")
                capturedImageFile = capturedImageFilePath?.let { File(it) }
            }
        }

    companion object {
        const val ADD_STORY_REQUEST_CODE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_story, container, false)
        if (capturedImageFile == null) {
            val intent = Intent(requireContext(), AddStoryActivity::class.java)
            startAddStoryActivity.launch(intent)
        }
        bindUser()
        return view
    }

    private fun bindUser(){
        Glide.with(this).load(R.drawable.ic_baseline_person_24).into(view.findViewById(R.id.iv_item_avatar))
        Glide.with(this).load(R.drawable.outline_camera_24).into(view.findViewById(R.id.iv_item_story))
    }

}