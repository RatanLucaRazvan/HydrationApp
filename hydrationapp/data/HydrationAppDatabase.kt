package com.example.hydrationapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DayReport::class], version = 1, exportSchema = false)
abstract class HydrationAppDatabase : RoomDatabase() {
    abstract fun reportDao(): ReportDao


    companion object {
        @Volatile
        private var Instance: HydrationAppDatabase? = null

        fun getDatabase(context: Context): HydrationAppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, HydrationAppDatabase::class.java, "hydration_app")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}