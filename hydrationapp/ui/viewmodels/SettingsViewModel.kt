package com.example.hydrationapp.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hydrationapp.data.repositories.ReportRepository
import com.example.hydrationapp.data.repositories.UserPreferencesRepository
import com.example.hydrationapp.model.Units
import com.example.hydrationapp.model.getByKey
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate

data class SettingsUiState(
    val goal: Int,
    val containerOne: Int,
    val containerTwo: Int,
    val containerThree: Int,
    val unit: Units
)


class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val reportRepository: ReportRepository
) : ViewModel() {
    private var _settingsUiState = MutableStateFlow(UIResource<SettingsUiState>())
    val settingsUiState = _settingsUiState

    var userEditInput: String? by mutableStateOf(null)
        private set

    init {
        getSettingsUiState()
    }


    fun updateUserEditInput(newInput: String?) {
        userEditInput = newInput
    }


    private fun getSettingsUiState() {
        viewModelScope.launch {
            val goal = userPreferencesRepository.dailyGoal.first()
            val containerOne = userPreferencesRepository.containerOne.first()
            val containerTwo = userPreferencesRepository.containerTwo.first()
            val containerThree = userPreferencesRepository.containerThree.first()
            val unit = userPreferencesRepository.units.first()
            val transformedUnit = getByKey(unit)
            _settingsUiState.emit(
                UIResource(
                    Status.SUCCESS,
                    SettingsUiState(
                        goal = goal,
                        containerOne = containerOne,
                        containerTwo = containerTwo,
                        containerThree = containerThree,
                        unit = transformedUnit
                    ),
                    null
                )
            )
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
            )

            val (goal, containerOne, containerTwo, containerThree, unit) = deferredResults.awaitAll()

            val transformedUnit = getByKey(unit as String)
            _settingsUiState.emit(
                UIResource(
                    Status.SUCCESS,
                    SettingsUiState(
                        goal as Int,
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


    @RequiresApi(Build.VERSION_CODES.O)
    fun updateGoal(newGoal: Int) {
        viewModelScope.launch {
            userPreferencesRepository.saveDailyGoal(newGoal)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveGoalToDatabase(newGoal: Int) {
        viewModelScope.launch {
            reportRepository.saveGoalForDay(LocalDate.now().toString(), newGoal)
        }
    }

    fun updateContainerOne(newContainerOne: Int) {
        viewModelScope.launch {
            userPreferencesRepository.saveContainerOne(newContainerOne)
        }
    }

    fun updateContainerTwo(newContainerTwo: Int) {
        viewModelScope.launch {
            userPreferencesRepository.saveContainerTwo(newContainerTwo)
        }
    }

    fun updateContainerThree(newContainerThree: Int) {
        viewModelScope.launch {
            userPreferencesRepository.saveContainerThree(newContainerThree)
        }
    }

    fun updateUnits(newUnits: Units) {
        val unitsValue = if (newUnits == Units.MILLILITERS) "ml" else "oz"
        viewModelScope.launch {
            userPreferencesRepository.saveUnits(unitsValue)
        }
    }

    fun homeNeedsRefresh() {
        viewModelScope.launch {
            userPreferencesRepository.homeNeedsUpdate(true)
        }
    }


}