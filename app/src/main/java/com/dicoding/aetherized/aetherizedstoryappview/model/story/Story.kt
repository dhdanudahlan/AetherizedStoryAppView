package com.dicoding.aetherized.aetherizedstoryappview.model.story

import android.os.Parcelable
import com.dicoding.aetherized.aetherizedstoryappview.data.local.entity.StoryEntity
import com.dicoding.aetherized.aetherizedstoryappview.data.remote.dto.StoryDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: String? = "0",
    val lon: String? = "0"
): Parcelable {
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
    fun toStoryDto(): StoryDto {
        return StoryDto(
            id = id,
            name = name,
            description = description,
            photoUrl = photoUrl,
            createdAt = createdAt,
            lat = lat,
            lon = lon
        )
    }
}
