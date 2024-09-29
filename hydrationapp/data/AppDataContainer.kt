package com.example.hydrationapp.data

import android.content.Context
import com.example.hydrationapp.data.repositories.OfflineReportRepository
import com.example.hydrationapp.data.repositories.ReportRepository

class AppDataContainer(private val context: Context) : AppContainer {
    override val reportRepository: ReportRepository by lazy {
        OfflineReportRepository(HydrationAppDatabase.getDatabase(context).reportDao())
    }
}