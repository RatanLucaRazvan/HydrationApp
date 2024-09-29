package com.example.hydrationapp.data.repositories

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.hydrationapp.data.DEFAULT_CONTAINER_ONE
import com.example.hydrationapp.data.DEFAULT_CONTAINER_THREE
import com.example.hydrationapp.data.DEFAULT_CONTAINER_TWO
import com.example.hydrationapp.data.DEFAULT_GOAL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    val units: Flow<String> = dataStore.data
        .catch {
            if (it is Exception) {
                Log.e(TAG, "Error reading preferences", it)
                emit(emptyPreferences())
            }
        }
        .map { preferences ->
            preferences[UNITS] ?: "ml"
        }

    val dailyGoal: Flow<Int> = dataStore.data
        .catch {
            if (it is Exception) {
                Log.e(TAG, "Error reading preferences", it)
                emit(emptyPreferences())
            }
        }
        .map { preferences ->
            preferences[DAILY_GOAL] ?: DEFAULT_GOAL
        }

    val containerOne: Flow<Int> = dataStore.data
        .catch {
            if (it is Exception) {
                Log.e(TAG, "Error reading preferences", it)
                emit(emptyPreferences())
            }
        }
        .map { preferences ->
            preferences[CONTAINER_ONE] ?: DEFAULT_CONTAINER_ONE
        }

    val containerTwo: Flow<Int> = dataStore.data
        .catch {
            if (it is Exception) {
                Log.e(TAG, "Error reading preferences", it)
                emit(emptyPreferences())
            }
        }
        .map { preferences ->
            preferences[CONTAINER_TWO] ?: DEFAULT_CONTAINER_TWO
        }

    val containerThree: Flow<Int> = dataStore.data
        .catch {
            if (it is Exception) {
                Log.e(TAG, "Error reading preferences", it)
                emit(emptyPreferences())
            }
        }
        .map { preferences ->
            preferences[CONTAINER_THREE] ?: DEFAULT_CONTAINER_THREE
        }

    val updateHome: Flow<Boolean> = dataStore.data
        .catch {
            if (it is Exception) {
                Log.e(TAG, "Error reading preferences", it)
                emit(emptyPreferences())
            }
        }
        .map { preferences ->
            val prefs = preferences[HOME_NEEDS_UPDATE]
            Log.d(TAG, "update home = $prefs")

            prefs ?: false
        }


    suspend fun saveUnits(newUnits: String) {
        dataStore.edit { preferences ->
            preferences[UNITS] = newUnits
        }
    }

    suspend fun saveDailyGoal(newDailyGoal: Int) {
        dataStore.edit { preferences ->
            preferences[DAILY_GOAL] = newDailyGoal
        }
    }

    suspend fun saveContainerOne(newContainerOne: Int) {
        dataStore.edit { preferences ->
            preferences[CONTAINER_ONE] = newContainerOne
        }
    }

    suspend fun saveContainerTwo(newContainerTwo: Int) {
        dataStore.edit { preferences ->
            preferences[CONTAINER_TWO] = newContainerTwo
        }
    }

    suspend fun saveContainerThree(newContainerThree: Int) {
        dataStore.edit { preferences ->
            preferences[CONTAINER_THREE] = newContainerThree
        }
    }

    suspend fun homeNeedsUpdate(newValue: Boolean) {
        Log.d(TAG, "newValue is $newValue")
        dataStore.edit { preferences ->
            preferences[HOME_NEEDS_UPDATE] = newValue
        }
    }

    private companion object {
        const val TAG = "UserSettingsRepository"
        val UNITS = stringPreferencesKey("measure_units")
        val DAILY_GOAL = intPreferencesKey("daily_goal")
        val CONTAINER_ONE = intPreferencesKey("container_one")
        val CONTAINER_TWO = intPreferencesKey("container_two")
        val CONTAINER_THREE = intPreferencesKey("container_three")
        val HOME_NEEDS_UPDATE = booleanPreferencesKey("home")
    }

}