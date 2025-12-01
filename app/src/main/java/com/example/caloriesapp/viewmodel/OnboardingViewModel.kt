package com.example.caloriesapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caloriesapp.data.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * Immutable state describing the whole onboarding progress.
 */
data class OnboardingState(
    val gender: String? = null,
    val age: Int? = null,
    val weight: Int? = null,
    val height: Int? = null,
    val diseases: Set<String> = emptySet(),
    val isLoaded: Boolean = false
)

/**
 * Single shared ViewModel for all onboarding screens.
 */
class OnboardingViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val genderFlow = UserPreferences.genderFlow(context)
    private val ageFlow = UserPreferences.ageFlow(context)
    private val weightFlow = UserPreferences.weightFlow(context)
    private val heightFlow = UserPreferences.heightFlow(context)
    private val diseasesFlow = UserPreferences.diseasesFlow(context)

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state

    init {
        viewModelScope.launch {
            combine(
                genderFlow,
                ageFlow,
                weightFlow,
                heightFlow,
                diseasesFlow
            ) { gender, age, weight, height, diseases ->

                OnboardingState(
                    gender = gender,
                    age = age,
                    weight = weight,
                    height = height,
                    diseases = diseases,
                    isLoaded = true
                )
            }.collect { combined ->
                _state.value = combined
            }
        }

    }

    fun saveGender(context: Context, value: String) =
        viewModelScope.launch { UserPreferences.saveGender(context, value) }

    fun saveAge(context: Context, value: Int) =
        viewModelScope.launch { UserPreferences.saveAge(context, value) }

    fun saveWeight(context: Context, value: Int) =
        viewModelScope.launch { UserPreferences.saveWeight(context, value) }

    fun saveHeight(context: Context, value: Int) =
        viewModelScope.launch { UserPreferences.saveHeight(context, value) }

    fun saveDiseases(context: Context, values: Set<String>) =
        viewModelScope.launch { UserPreferences.saveDiseases(context, values) }
}

