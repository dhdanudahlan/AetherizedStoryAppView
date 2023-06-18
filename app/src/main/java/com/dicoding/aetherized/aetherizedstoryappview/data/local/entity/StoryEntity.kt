package com.dicoding.aetherized.aetherizedstoryappview.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dicoding.aetherized.aetherizedstoryappview.data.model.story.Story
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "story")
@Parcelize
data class StoryEntity(
    @PrimaryKey(autoGenerate = true)
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("story_id")
    val storyId: String,

    @field:SerializedName("story_name")
    val name: String,

    @field:SerializedName("story_description")
    val description: String,

    @field:SerializedName("story_photo_url")
    val photoUrl: String,

    @field:SerializedName("story_created_at")
    val createdAt: String,

    @field:SerializedName("story_lay")
    val lat: String? = "0",

    @field:SerializedName("story_lon")
    val lon: String? = "0"
): Parcelable {
    fun toStory(): Story {
        return Story(
            id = storyId,
            name = name,
            description = description,
            photoUrl = photoUrl,
            createdAt = createdAt,
            lat = lat,
            lon = lon
        )
    }
}
