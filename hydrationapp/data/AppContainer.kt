package com.example.hydrationapp.data

import com.example.hydrationapp.data.repositories.ReportRepository

interface AppContainer {
    val reportRepository: ReportRepository
}