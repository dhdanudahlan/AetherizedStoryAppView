package com.dicoding.aetherized.aetherizedstoryappview.utils

import com.dicoding.aetherized.aetherizedstoryappview.data.model.story.Story
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.UUID


object DataDummy {
    fun generateStoryNewsEntity(size: Int): List<Story> {
        val storyList = ArrayList<Story>()
        for (i in 0..size) {
            val story = Story(
                id = "story-${UUID.randomUUID()}",
                name = "USER-${UUID.randomUUID()}",
                description = "Uploaded on ${DateTimeFormatter.ISO_INSTANT.format(Instant.now())}",
                photoUrl = "R.drawable.image_${(1..4).random()}",
                createdAt = DateTimeFormatter.ISO_INSTANT.format(Instant.now()),
                lat = "${(-90..90).random()}.${(0..999999).random()}",
                lon ="${(-180..180).random()}.${(0..999999).random()}"
            )
            storyList.add(story)
        }
        return storyList
    }
}