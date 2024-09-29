package com.example.hydrationapp

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.hydrationapp.data.AppContainer
import com.example.hydrationapp.data.AppDataContainer
import com.example.hydrationapp.data.repositories.UserPreferencesRepository
import com.example.hydrationapp.utils.ChartPointFormatter
import com.example.hydrationapp.utils.Formatter

class HydrationApplication : Application() {
    lateinit var container: AppContainer
    lateinit var userPreferencesRepository: UserPreferencesRepository
    lateinit var formatter: Formatter
    lateinit var chartPointFormatter: ChartPointFormatter


    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        userPreferencesRepository = UserPreferencesRepository(dataStore = dataStore)
        formatter = Formatter()
        chartPointFormatter = ChartPointFormatter()
    }
}


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "AppData"
)