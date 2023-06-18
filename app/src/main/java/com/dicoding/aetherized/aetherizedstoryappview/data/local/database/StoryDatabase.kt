package com.dicoding.aetherized.aetherizedstoryappview.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.aetherized.aetherizedstoryappview.data.local.dao.RemoteKeysDao
import com.dicoding.aetherized.aetherizedstoryappview.data.local.dao.StoryDao
import com.dicoding.aetherized.aetherizedstoryappview.data.local.entity.StoryEntity

@Database(
    entities = [StoryEntity::class, RemoteKeys::class],
    version = 2,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract val storyDao: StoryDao
    abstract val remoteKeysDao: RemoteKeysDao
    companion object {
        @Volatile
        private var INSTANCE: StoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context) : StoryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java,
                    "story_database_v.1.0"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}