package com.example.hydrationapp.ui.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hydrationapp.data.DEFAULT_GOAL
import com.example.hydrationapp.data.DayReport
import com.example.hydrationapp.data.repositories.ReportRepository
import com.example.hydrationapp.data.repositories.UserPreferencesRepository
import com.example.hydrationapp.model.Units
import com.example.hydrationapp.model.getByKey
import com.example.hydrationapp.utils.Formatter
import com.example.hydrationapp.utils.component6
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate


data class HomeUiState(
    val goal: Int,
    val drunkQuantity: Int,
    val percentage: Int,
    val containerOne: Int,
    val containerTwo: Int,
    val containerThree: Int,
    val unit: Units,
)


@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val reportRepository: ReportRepository,
    private val formatter: Formatter
) : ViewModel() {
    private var _homeUiState = MutableStateFlow(UIResource<HomeUiState>())
    val homeUiState = _homeUiState

    init {
        getHomeUiState()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getHomeUiState() {
        Log.d("HomeViewModel", "We entered getHomeUiState")
        viewModelScope.launch {
            val goal = userPreferencesRepository.dailyGoal.first()
            val containerOne = userPreferencesRepository.containerOne.first()
            val containerTwo = userPreferencesRepository.containerTwo.first()
            val containerThree = userPreferencesRepository.containerThree.first()
            val unit = userPreferencesRepository.units.first()
            val transformedUnit = getByKey(unit)
            val drunkQuantity =
                reportRepository.getDrunkQuantityFromDay(LocalDate.now().toString()).first() ?: 0
            _homeUiState.emit(
                UIResource(
                    Status.SUCCESS,
                    HomeUiState(
                        goal = goal,
                        containerOne = containerOne,
                        containerTwo = containerTwo,
                        containerThree = containerThree,
                        unit = transformedUnit,
                        drunkQuantity = drunkQuantity,
                        percentage = formatter.calculatePercentage(drunkQuantity, goal)
                    ),
                    null
                )
            )

            Log.d("HomeViewModel", "HomeUiState: $homeUiState")
        }
    }

    private fun updateState() {
        viewModelScope.launch {
            val deferredResults = listOf(
                async { userPreferencesRepository.dailyGoal.first() },
                async { userPreferencesRepository.containerOne.first() },
                async { userPreferencesRepository.containerTwo.first() },
                async { userPreferencesRepository.containerThree.first() },
                async { userPreferencesRepository.units.first() },
                async {
                    reportRepository.getDrunkQuantityFromDay(LocalDate.now().toString()).first()
                }
            )

            val (goal, containerOne, containerTwo, containerThree, unit, drunkQuantity) = deferredResults.awaitAll()

            val transformedUnit = getByKey(unit as String)
            val drunkQuantityToday = (drunkQuantity as Int?) ?: 0
            val percentage = formatter.calculatePercentage(drunkQuantityToday, goal as Int)

            _homeUiState.emit(
                UIResource(
                    Status.SUCCESS,
                    HomeUiState(
                        goal,
                        drunkQuantity ?: 0,
                        percentage,
                        containerOne as Int,
                        containerTwo as Int,
                        containerThree as Int,
                        transformedUnit
                    ),
                    null
                )
            )

        }
    }


    fun syncDailyGoal() {
        viewModelScope.launch {
            userPreferencesRepository.dailyGoal.collectLatest {
                updateState()
            }
        }
    }


    fun syncContainerOne() {
        viewModelScope.launch {
            userPreferencesRepository.containerOne.collectLatest {
                updateState()
            }
        }
    }

    fun syncContainerTwo() {
        viewModelScope.launch {
            userPreferencesRepository.containerTwo.collectLatest {
                updateState()
            }
        }
    }

    fun syncContainerThree() {
        viewModelScope.launch {
            userPreferencesRepository.containerThree.collectLatest {
                updateState()
            }
        }
    }

    fun syncUnit() {
        viewModelScope.launch {
            userPreferencesRepository.units.collectLatest {
                updateState()
            }
        }
    }

    fun syncDrunkQuantity() {
        viewModelScope.launch {
            reportRepository.getDrunkQuantityFromDay(LocalDate.now().toString()).collectLatest {
                updateState()
            }
        }
    }

    fun updateDrunkQuantity(newQuantity: Int) {
        viewModelScope.launch {
            if (reportRepository.getDrunkQuantityFromDay(LocalDate.now().toString())
                    .first() == null
            ) {
                reportRepository.addReport(
                    DayReport(
                        LocalDate.now().toString(),
                        newQuantity,
                        _homeUiState.value.data?.goal ?: DEFAULT_GOAL
                    )
                )
            } else {
                reportRepository.updateReport(LocalDate.now().toString(), newQuantity)
            }
        }
    }

    fun updateDrunkQuantityRemove(newQuantity: Int) {
        viewModelScope.launch {
            if (reportRepository.getDrunkQuantityFromDay(LocalDate.now().toString())
                    .first() == null
            ) {
                reportRepository.addReport(
                    DayReport(
                        LocalDate.now().toString(),
                        newQuantity,
                        _homeUiState.value.data?.goal ?: DEFAULT_GOAL
                    )
                )
            } else {
                reportRepository.updateReportRemoveQuantity(LocalDate.now().toString(), newQuantity)
            }
        }
    }

    fun doesNeedUpdate(): Flow<Boolean> {
        return userPreferencesRepository.updateHome
    }

    fun homeUpdated() {
        viewModelScope.launch {
            userPreferencesRepository.homeNeedsUpdate(false)
        }
    }


}

