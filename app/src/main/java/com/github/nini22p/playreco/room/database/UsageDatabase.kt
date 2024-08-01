package com.github.nini22p.playreco.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.nini22p.playreco.room.dao.UsageDao
import com.github.nini22p.playreco.room.entity.UsageEntity

@Database(entities = [UsageEntity::class], version = 1)
abstract class UsageDatabase : RoomDatabase() {
    abstract fun usageDao(): UsageDao

    companion object {
        @Volatile
        private var INSTANCE: UsageDatabase? = null

        fun getDatabase(context: Context): UsageDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UsageDatabase::class.java,
                    "usage_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
