package com.example.hydrationapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.hydrationapp.data.DayReport
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class ChartPointFormatter {
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatReportList(reportList: List<DayReport>): List<DayReport> {
        val today = LocalDate.now()
        val firstDayFrom30 = today.minusDays(29)
        val validDayReportsList = mutableListOf<DayReport>()
        reportList.forEach { dayReport ->
            val day = LocalDate.parse(dayReport.day)
            if (day in firstDayFrom30..today) {
                validDayReportsList.add(dayReport)
            }
        }

        val formattedList = MutableList<DayReport>(30) { DayReport("", 0, 0) }
        validDayReportsList.forEach { dayReport ->
            val day = LocalDate.parse(dayReport.day)
            val dayRank = ChronoUnit.DAYS.between(firstDayFrom30, day)
            formattedList[dayRank.toInt()] = dayReport
        }


        return formattedList

    }
}