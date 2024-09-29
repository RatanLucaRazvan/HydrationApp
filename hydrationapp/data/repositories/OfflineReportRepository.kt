package com.example.hydrationapp.data.repositories

import android.util.Log
import com.example.hydrationapp.data.DayReport
import com.example.hydrationapp.data.ReportDao
import kotlinx.coroutines.flow.Flow

class OfflineReportRepository(private val reportDao: ReportDao) : ReportRepository {
    override fun getReport(): Flow<List<DayReport>> {
        return reportDao.getReport()
    }

    override fun getDrunkQuantityFromDay(day: String): Flow<Int?> {
        return reportDao.getDrunkQuantityFromDay(day)
    }

    override suspend fun addReport(report: DayReport) {
        reportDao.insertReport(report)
    }

    override suspend fun updateReport(currentDate: String, newQuantity: Int) {
        reportDao.updateReport(currentDate, newQuantity)
    }

    override suspend fun updateReportRemoveQuantity(currentDate: String, quantityToBeRemoved: Int) {
        reportDao.updateReportRemoveQuantity(currentDate, quantityToBeRemoved)
    }

    override suspend fun saveGoalForDay(day: String, newGoal: Int) {
        Log.d("OfflineReportRepository", "SaveGoalForDay")
        reportDao.saveGoalForDay(day, newGoal)
    }
}