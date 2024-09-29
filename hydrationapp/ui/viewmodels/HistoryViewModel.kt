package com.example.hydrationapp.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hydrationapp.data.DayReport
import com.example.hydrationapp.data.repositories.ReportRepository
import com.example.hydrationapp.data.repositories.UserPreferencesRepository
import com.example.hydrationapp.model.Units
import com.example.hydrationapp.model.getByKey
import com.example.hydrationapp.utils.ChartPointFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class HistoryUiState(
    val reportList: List<DayReport> = listOf(),
    val unit: Units
)

@RequiresApi(Build.VERSION_CODES.O)
class HistoryViewModel(
    val reportRepository: ReportRepository,
    val userPreferencesRepository: UserPreferencesRepository,
    val chartPointFormatter: ChartPointFormatter,
) : ViewModel() {
    private var _historyUiState = MutableStateFlow(UIResource<HistoryUiState>())
    val historyUiState = _historyUiState


    init {
        getHistoryUiState()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getHistoryUiState() {
        viewModelScope.launch {
            val reportList = reportRepository.getReport().first()
            val formattedList = chartPointFormatter.formatReportList(reportList)
            val unit = userPreferencesRepository.units.first()
            val transformedUnit = getByKey(unit)
            _historyUiState.emit(
                UIResource(
                    Status.SUCCESS,
                    HistoryUiState(
                        formattedList,
                        transformedUnit
                    ),
                    null
                )
            )
        }
    }

}