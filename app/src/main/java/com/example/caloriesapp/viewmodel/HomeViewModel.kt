package com.example.caloriesapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.caloriesapp.data.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeState(
    val dailyCalorieGoal: Int = 2000,
    val caloriesConsumed: Int = 0,
    val caloriesRemaining: Int = 2000,
    val proteinGoal: Int = 150,
    val proteinConsumed: Int = 0,
    val proteinRemaining: Int = 150,
    val fatGoal: Int = 67,
    val fatConsumed: Int = 0,
    val fatRemaining: Int = 67,
    val carbsGoal: Int = 200,
    val carbsConsumed: Int = 0,
    val carbsRemaining: Int = 200,
    val streak: Int = 0
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val context = getApplication<Application>()

            val dailyCalorieGoal = UserPreferences.getDailyCalorieGoal(context)
            val proteinGoal = UserPreferences.getProteinGoal(context)
            val fatGoal = UserPreferences.getFatGoal(context)
            val carbsGoal = UserPreferences.getCarbsGoal(context)

            _state.value = _state.value.copy(
                dailyCalorieGoal = dailyCalorieGoal,
                caloriesRemaining = dailyCalorieGoal,
                proteinGoal = proteinGoal,
                proteinRemaining = proteinGoal,
                fatGoal = fatGoal,
                fatRemaining = fatGoal,
                carbsGoal = carbsGoal,
                carbsRemaining = carbsGoal
            )
        }
    }

    // TODO: Add functions to update consumed values when meals are logged
}