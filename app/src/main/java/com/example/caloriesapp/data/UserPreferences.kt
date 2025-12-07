package com.example.caloriesapp.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.caloriesapp.utils.CalorieCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Extension property to create DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

object UserPreferences {
    // Define preference keys
    private const val PREFS_NAME = "onboarding_prefs"

    private const val KEY_GENDER = "gender"
    private const val KEY_AGE = "age"
    private const val KEY_HEIGHT = "height"
    private const val KEY_WEIGHT = "weight"
    private const val KEY_DESIRED_WEIGHT = "desired_weight"
    private const val KEY_DISEASES = "diseases"
    private const val KEY_ONBOARDING_COMPLETE = "onboarding_complete"

    private const val KEY_DAILY_CALORIE_GOAL = "daily_calorie_goal"
    private const val KEY_PROTEIN_GOAL = "protein_goal"
    private const val KEY_FAT_GOAL = "fat_goal"
    private const val KEY_CARBS_GOAL = "carbs_goal"
    private const val KEY_BMR = "bmr"
    private const val KEY_TDEE = "tdee"

    private fun getPrefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Save functions
    suspend fun saveGender(context: Context, gender: String) {
        withContext(Dispatchers.IO) {
            getPrefs(context).edit().putString(KEY_GENDER, gender).apply()
        }
    }

    suspend fun saveAge(context: Context, age: Int) {
        withContext(Dispatchers.IO) {
            getPrefs(context).edit().putInt(KEY_AGE, age).apply()
        }
    }

    suspend fun saveHeight(context: Context, height: Int) {
        withContext(Dispatchers.IO) {
            getPrefs(context).edit().putInt(KEY_HEIGHT, height).apply()
        }
    }

    suspend fun saveWeight(context: Context, weight: Int) {
        withContext(Dispatchers.IO) {
            getPrefs(context).edit().putInt(KEY_WEIGHT, weight).apply()
        }
    }

    suspend fun saveDesiredWeight(context: Context, desiredWeight: Int) {
        withContext(Dispatchers.IO) {
            getPrefs(context).edit().putInt(KEY_DESIRED_WEIGHT, desiredWeight).apply()
        }
    }

    suspend fun saveDiseases(context: Context, diseases: Set<String>) {
        withContext(Dispatchers.IO) {
            Log.d("UserPreferences", "Saving diseases: $diseases")
            getPrefs(context).edit()
                .putStringSet(KEY_DISEASES, diseases.toSet()) // Create mutable copy
                .apply()
            val saved = getPrefs(context).getStringSet(KEY_DISEASES, null)?.toSet() ?: emptySet()
            Log.d("UserPreferences", "Diseases verification - saved value: $saved")
        }
    }

    suspend fun saveNutritionData(context: Context, nutritionData: CalorieCalculator.NutritionData) {
        withContext(Dispatchers.IO) {
            getPrefs(context).edit().apply {
                putInt(KEY_DAILY_CALORIE_GOAL, nutritionData.dailyCalorieGoal)
                putInt(KEY_PROTEIN_GOAL, nutritionData.proteinGoal)
                putInt(KEY_FAT_GOAL, nutritionData.fatGoal)
                putInt(KEY_CARBS_GOAL, nutritionData.carbsGoal)
                putInt(KEY_BMR, nutritionData.bmr)
                putInt(KEY_TDEE, nutritionData.tdee)
                apply()
            }
        }
    }

    // -------------------------
    // READ FLOWS
    // -------------------------

    suspend fun getGender(context: Context): String? {
        return withContext(Dispatchers.IO) {
            getPrefs(context).getString(KEY_GENDER, null)
        }
    }

    suspend fun getAge(context: Context): Int {
        return withContext(Dispatchers.IO) {
            getPrefs(context).getInt(KEY_AGE, 0)
        }
    }

    suspend fun getHeight(context: Context): Int {
        return withContext(Dispatchers.IO) {
            getPrefs(context).getInt(KEY_HEIGHT, 0)
        }
    }

    suspend fun getWeight(context: Context): Int {
        return withContext(Dispatchers.IO) {
            getPrefs(context).getInt(KEY_WEIGHT, 0)
        }
    }

    suspend fun getDesiredWeight(context: Context): Int {
        return withContext(Dispatchers.IO) {
            getPrefs(context).getInt(KEY_DESIRED_WEIGHT, 0)
        }
    }

    suspend fun getDiseases(context: Context): Set<String> {
        return withContext(Dispatchers.IO) {
            // ✅ Return empty set if null, also create mutable copy
            getPrefs(context).getStringSet(KEY_DISEASES, null)?.toSet() ?: emptySet()
        }
    }

    suspend fun getDailyCalorieGoal(context: Context): Int {
        return withContext(Dispatchers.IO) {
            getPrefs(context).getInt(KEY_DAILY_CALORIE_GOAL, 2000)
        }
    }

    suspend fun getProteinGoal(context: Context): Int {
        return withContext(Dispatchers.IO) {
            getPrefs(context).getInt(KEY_PROTEIN_GOAL, 150)
        }
    }

    suspend fun getFatGoal(context: Context): Int {
        return withContext(Dispatchers.IO) {
            getPrefs(context).getInt(KEY_FAT_GOAL, 67)
        }
    }

    suspend fun getCarbsGoal(context: Context): Int {
        return withContext(Dispatchers.IO) {
            getPrefs(context).getInt(KEY_CARBS_GOAL, 200)
        }
    }

    suspend fun getBMR(context: Context): Int {
        return withContext(Dispatchers.IO) {
            getPrefs(context).getInt(KEY_BMR, 1500)
        }
    }

    suspend fun getTDEE(context: Context): Int {
        return withContext(Dispatchers.IO) {
            getPrefs(context).getInt(KEY_TDEE, 2000)
        }
    }

    suspend fun setOnboardingComplete(context: Context, complete: Boolean) {
        withContext(Dispatchers.IO) {
            getPrefs(context).edit().putBoolean(KEY_ONBOARDING_COMPLETE, complete).apply()
        }
    }

    suspend fun isOnboardingComplete(context: Context): Boolean {
        return withContext(Dispatchers.IO) {
            getPrefs(context).getBoolean(KEY_ONBOARDING_COMPLETE, false)
        }
    }

    // Clear all preferences
    suspend fun clearAll(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}