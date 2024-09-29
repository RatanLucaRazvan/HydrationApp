package com.example.hydrationapp.data.repositories

import com.example.hydrationapp.data.DayReport
import kotlinx.coroutines.flow.Flow

interface ReportRepository {
    fun getReport(): Flow<List<DayReport>>
    fun getDrunkQuantityFromDay(day: String): Flow<Int?>
    suspend fun addReport(report: DayReport)
    suspend fun updateReport(currentDate: String, newQuantity: Int)
    suspend fun updateReportRemoveQuantity(currentDate: String, quantityToBeRemoved: Int)
    suspend fun saveGoalForDay(day: String, newGoal: Int)
}