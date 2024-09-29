package com.example.hydrationapp.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.hydrationapp.HydrationApplication


object AppViewModelProvider {
    @RequiresApi(Build.VERSION_CODES.O)
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                inventoryApplication().userPreferencesRepository,
                inventoryApplication().container.reportRepository,
                inventoryApplication().formatter
            )
        }
        initializer {
            HistoryViewModel(
                inventoryApplication().container.reportRepository,
                inventoryApplication().userPreferencesRepository,
                inventoryApplication().chartPointFormatter
            )
        }

        initializer {
            SettingsViewModel(
                inventoryApplication().userPreferencesRepository,
                inventoryApplication().container.reportRepository
            )
        }
    }
}


fun CreationExtras.inventoryApplication(): HydrationApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as HydrationApplication)