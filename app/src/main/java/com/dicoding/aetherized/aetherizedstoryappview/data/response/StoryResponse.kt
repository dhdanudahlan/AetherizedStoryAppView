package com.dicoding.aetherized.aetherizedstoryappview.data.response

import com.dicoding.aetherized.aetherizedstoryappview.data.model.Story
import com.google.gson.annotations.SerializedName

data class StoryResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("story")
    val data: Story
)

data class StoriesResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("listStory")
    val data: List<Story>
)
