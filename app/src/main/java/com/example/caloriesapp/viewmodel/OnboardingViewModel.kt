package com.example.caloriesapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.caloriesapp.data.UserPreferences
import com.example.caloriesapp.utils.CalorieCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Immutable state describing the whole onboarding progress.
 */
data class OnboardingState(
    val gender: String? = null,
    val age: Int? = null,
    val height: Int? = null,
    val weight: Int? = null,
    val desiredWeight: Int? = null,
    val activityLevel: String? = null,
    val diseases: Set<String> = emptySet(),
    val isLoaded: Boolean = false,
    val isValid: Boolean = true,
    val currentStep: Int = 0
) {
    val isValidForCalories: Boolean
        get() = gender != null &&
                age != null &&
                height != null &&
                weight != null &&
                desiredWeight != null &&
                activityLevel != null
}

/**
 * Single shared ViewModel for all onboarding screens.
 */
class OnboardingViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val appContext = getApplication<Application>().applicationContext

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    init {
        loadSavedData()
    }

    private fun loadSavedData() {
        viewModelScope.launch {
            val gender = UserPreferences.getGender(appContext)
            val age = UserPreferences.getAge(appContext)
            val height = UserPreferences.getHeight(appContext)
            val weight = UserPreferences.getWeight(appContext)
            val desiredWeight = UserPreferences.getDesiredWeight(appContext)
            val activityLevel = UserPreferences.getGender(appContext)
            val diseases = UserPreferences.getDiseases(appContext)

            _state.value = OnboardingState(
                gender = gender,
                age = age,
                height = height,
                weight = weight,
                desiredWeight = desiredWeight,
                activityLevel = activityLevel,
                diseases = diseases
            )
        }
    }

    fun calculateAndSaveNutrition() {
        viewModelScope.launch {
            val currentState = _state.value

            if (currentState.isValid) {
                val nutritionData = CalorieCalculator.calculateNutritionPlan(
                    gender = currentState.gender!!,
                    weight = currentState.weight!!,
                    height = currentState.height!!,
                    age = currentState.age!!,
                    desiredWeight = currentState.desiredWeight!!,
                    activityLevel = currentState.activityLevel!!
                )

                UserPreferences.saveNutritionData(appContext, nutritionData)

                Log.d("OnboardingVM", "Calculated nutrition: $nutritionData")
            }
        }
    }

    private val _diseases = MutableStateFlow<Set<String>>(emptySet())
    val diseases: StateFlow<Set<String>> = _diseases

    // Separate state for AI recommendations
    private val _recommendations = MutableStateFlow<String?>(null)
    val recommendations: StateFlow<String?> = _recommendations

    // Error state
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Loading state for recommendations
    private val _isLoadingRecommendations = MutableStateFlow(false)
    val isLoadingRecommendations: StateFlow<Boolean> = _isLoadingRecommendations

    // Save functions with error handling
    fun saveGender(gender: String) {
        savePreference {
            UserPreferences.saveGender(appContext, gender)
            _state.value = _state.value.copy(gender = gender)
        }
    }

    fun saveAge(age: Int) {
        if (age in 10..120) {
            savePreference {
                UserPreferences.saveAge(appContext, age)
                _state.value = _state.value.copy(age = age)
            }
        } else {
            _error.value = "Age must be between 10 and 120"
        }
    }

    fun saveHeight(height: Int) {
        if (height in 100..250) {
            savePreference {
                UserPreferences.saveHeight(appContext, height)
                _state.value = _state.value.copy(height = height)
            }
        } else {
            _error.value = "Height must be between 100 and 250 cm"
        }
    }

    fun saveWeight(weight: Int) {
        if (weight in 30..300) {
            savePreference {
                UserPreferences.saveWeight(appContext, weight)
                _state.value = _state.value.copy(weight = weight)
            }
        } else {
            _error.value = "Weight must be between 30 and 300 kg"
        }
    }

    fun saveDesiredWeight(desiredWeight: Int) {
        if (desiredWeight in 30..300) {
            savePreference {
                UserPreferences.saveDesiredWeight(appContext, desiredWeight)
                _state.value = _state.value.copy(desiredWeight = desiredWeight)
            }
        } else {
            _error.value = "Desired weight must be between 30 and 300 kg"
        }
    }

    fun saveActivityLevel(activityLevel: String) {
        savePreference {
            UserPreferences.saveGender(appContext, activityLevel)
            _state.value = _state.value.copy(activityLevel = activityLevel)
            calculateAndSaveNutrition()
        }
    }

    fun saveDiseases(diseases: Set<String>) {
        viewModelScope.launch {
            UserPreferences.saveDiseases(appContext, diseases)
            _state.value = _state.value.copy(diseases = diseases)
        }
    }

    private fun savePreference(saveAction: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                saveAction()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to save: ${e.message}"
            }
        }
    }

    // Fetch current diseases for AI recommendations
    fun fetchRecommendations(): Set<String> {
        return state.value.diseases
    }

    // Update AI recommendations
    fun updateRecommendations(newRecommendations: String) {
        _recommendations.value = newRecommendations
        _isLoadingRecommendations.value = false
    }

    // Set loading state for recommendations
    fun setLoadingRecommendations(isLoading: Boolean) {
        _isLoadingRecommendations.value = isLoading
    }

    // Clear error
    fun clearError() {
        _error.value = null
    }

    // Helper for calorie calculation using BMR (Basal Metabolic Rate)
    fun calculateBMR(): Double? {
        val currentState = state.value
        return if (currentState.isValidForCalories) {
            // Harris-Benedict equation
            when (currentState.gender?.lowercase()) {
                "male" -> 88.362 +
                        (13.397 * currentState.weight!!) +
                        (4.799 * currentState.height!!) -
                        (5.677 * currentState.age!!)
                "female" -> 447.593 +
                        (9.247 * currentState.weight!!) +
                        (3.098 * currentState.height!!) -
                        (4.330 * currentState.age!!)
                else -> null
            }
        } else null
    }

    // Calculate daily calorie needs based on activity level
    fun calculateDailyCalories(activityLevel: ActivityLevel = ActivityLevel.MODERATE): Double? {
        val bmr = calculateBMR() ?: return null
        return bmr * activityLevel.multiplier
    }

    // Calculate calorie deficit/surplus for weight goal
    fun calculateTargetCalories(
        weeksToGoal: Int = 12,
        activityLevel: ActivityLevel = ActivityLevel.MODERATE
    ): Double? {
        val currentState = state.value
        if (!currentState.isValidForCalories) return null

        val dailyCalories = calculateDailyCalories(activityLevel) ?: return null
        val weightDifference = currentState.desiredWeight!! - currentState.weight!!

        // 1 kg ≈ 7700 calories
        val totalCalorieDifference = weightDifference * 7700.0
        val dailyCalorieAdjustment = totalCalorieDifference / (weeksToGoal * 7)

        return dailyCalories + dailyCalorieAdjustment
    }

    // Clear all onboarding data
    fun clearAllData() {
        viewModelScope.launch {
            try {
                UserPreferences.clearAll(appContext)
                _recommendations.value = null
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to clear data: ${e.message}"
            }
        }
    }
}

// Activity level enum for calorie calculations
enum class ActivityLevel(val multiplier: Double) {
    SEDENTARY(1.2),      // Little or no exercise
    LIGHT(1.375),        // Light exercise 1-3 days/week
    MODERATE(1.55),      // Moderate exercise 3-5 days/week
    ACTIVE(1.725),       // Hard exercise 6-7 days/week
    VERY_ACTIVE(1.9)     // Very hard exercise & physical job
}