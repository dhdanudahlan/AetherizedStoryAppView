package com.dicoding.aetherized.aetherizedstoryappview.data.remote.dto

import com.dicoding.aetherized.aetherizedstoryappview.data.local.entity.StoryEntity


data class StoryDto(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: String? = "0",
    val lon: String? = "0"
) {
    fun toStoryEntity(): StoryEntity {
        return StoryEntity(
            storyId = id,
            name = name,
            description = description,
            photoUrl = photoUrl,
            createdAt = createdAt,
            lat = lat,
            lon = lon
        )
    }
}
