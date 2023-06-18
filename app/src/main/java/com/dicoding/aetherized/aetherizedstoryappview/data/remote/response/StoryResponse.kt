package com.dicoding.aetherized.aetherizedstoryappview.data.remote.response

import com.dicoding.aetherized.aetherizedstoryappview.data.remote.dto.StoryDto
import com.google.gson.annotations.SerializedName

data class StoryResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("story")
    val data: StoryDto
)

data class StoriesResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("listStory")
    val data: List<StoryDto>
)
