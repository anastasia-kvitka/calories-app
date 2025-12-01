package com.example.caloriesapp.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * DataStore for storing user onboarding values such as:
 * - age
 * - weight
 * - height
 * - gender
 * - diseases (set)
 */

object UserPreferences {

    private val Context.dataStore by preferencesDataStore("user_prefs")

    // KEYS
    private val AGE = intPreferencesKey("age")
    private val WEIGHT = intPreferencesKey("weight")
    private val HEIGHT = intPreferencesKey("height")
    private val GENDER = stringPreferencesKey("gender")
    private val DISEASES = stringSetPreferencesKey("diseases")

    // --------------------
    // READ
    // --------------------
    fun genderFlow(context: Context): Flow<String?> =
        context.dataStore.data.map { it[GENDER] }

    fun ageFlow(context: Context): Flow<Int?> =
        context.dataStore.data.map { it[AGE] }

    fun weightFlow(context: Context): Flow<Int?> =
        context.dataStore.data.map { it[WEIGHT] }

    fun heightFlow(context: Context): Flow<Int?> =
        context.dataStore.data.map { it[HEIGHT] }

    fun diseasesFlow(context: Context): Flow<Set<String>> =
        context.dataStore.data.map { it[DISEASES] ?: emptySet() }

    // -------------------------
    // SAVE FUNCTIONS
    // -------------------------

    suspend fun saveAge(context: Context, age: Int) {
        context.dataStore.edit { prefs ->
            prefs[AGE] = age
        }
    }

    suspend fun saveWeight(context: Context, weight: Int) {
        context.dataStore.edit { prefs ->
            prefs[WEIGHT] = weight
        }
    }

    suspend fun saveHeight(context: Context, height: Int) {
        context.dataStore.edit { prefs ->
            prefs[HEIGHT] = height
        }
    }

    suspend fun saveGender(context: Context, gender: String) {
        context.dataStore.edit { prefs ->
            prefs[GENDER] = gender
        }
    }

    suspend fun saveDiseases(context: Context, diseases: Set<String>) {
        context.dataStore.edit { prefs ->
            prefs[DISEASES] = diseases
        }
    }

    // -------------------------
    // READ FLOWS
    // -------------------------

    fun getAge(context: Context): Flow<Int?> =
        context.dataStore.data.map { it[AGE] }

    fun getWeight(context: Context): Flow<Int?> =
        context.dataStore.data.map { it[WEIGHT] }

    fun getHeight(context: Context): Flow<Int?> =
        context.dataStore.data.map { it[HEIGHT] }

    fun getGender(context: Context): Flow<String?> =
        context.dataStore.data.map { it[GENDER] }

    fun getSelectedDiseases(context: Context): Flow<Set<String>> =
        context.dataStore.data.map { prefs ->
            prefs[DISEASES] ?: emptySet()
        }
}
