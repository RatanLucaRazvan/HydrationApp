package com.example.hydrationapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {
    @Query("SELECT * FROM report ORDER BY day ASC LIMIT 30")
    fun getReport(): Flow<List<DayReport>>

    @Insert
    suspend fun insertReport(report: DayReport)

    @Query("UPDATE report SET quantity = quantity + :newQuantity WHERE day == :currentDate")
    suspend fun updateReport(currentDate: String, newQuantity: Int)

    @Query("SELECT quantity FROM report WHERE day == :day")
    fun getDrunkQuantityFromDay(day: String): Flow<Int?>

    @Query("UPDATE report SET quantity = quantity - :quantityToBeRemoved WHERE day == :currentDate")
    suspend fun updateReportRemoveQuantity(currentDate: String, quantityToBeRemoved: Int)

    @Query("UPDATE report SET goal = :newGoal WHERE (EXISTS(SELECT * FROM report WHERE day == :day)) AND day == :day")
    suspend fun saveGoalForDay(day: String, newGoal: Int)
}