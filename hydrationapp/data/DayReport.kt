package com.example.hydrationapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "report")
data class DayReport(
    @PrimaryKey
    val day: String,
    val quantity: Int,
    val goal: Int
)