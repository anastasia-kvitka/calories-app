package com.example.caloriesapp.utils

object CalorieCalculator {

    /**
     * Calculate Basal Metabolic Rate (BMR) using Mifflin-St Jeor Equation
     * @param gender "male" or "female"
     * @param weight in kg
     * @param height in cm
     * @param age in years
     * @return BMR in calories
     */
    fun calculateBMR(
        gender: String,
        weight: Int,
        height: Int,
        age: Int
    ): Double {
        return if (gender.lowercase() == "male") {
            (10 * weight) + (6.25 * height) - (5 * age) + 5
        } else {
            (10 * weight) + (6.25 * height) - (5 * age) - 161
        }
    }

    /**
     * Get activity level multiplier
     * @param activityLevel: sedentary, light, moderate, very_active, extremely_active
     * @return multiplier value
     */
    fun getActivityMultiplier(activityLevel: String): Double {
        return when (activityLevel.lowercase()) {
            "sedentary" -> 1.2          // Little or no exercise
            "light" -> 1.375            // Light exercise 1-3 days/week
            "moderate" -> 1.55          // Moderate exercise 3-5 days/week
            "very_active" -> 1.725      // Hard exercise 6-7 days/week
            "extremely_active" -> 1.9   // Very hard exercise, physical job
            else -> 1.55                // Default to moderate
        }
    }

    /**
     * Calculate Total Daily Energy Expenditure (TDEE)
     * This is the maintenance calories
     */
    fun calculateTDEE(
        gender: String,
        weight: Int,
        height: Int,
        age: Int,
        activityLevel: String
    ): Int {
        val bmr = calculateBMR(gender, weight, height, age)
        val multiplier = getActivityMultiplier(activityLevel)
        return (bmr * multiplier).toInt()
    }

    /**
     * Calculate daily calorie goal based on weight goal
     * @param currentWeight in kg
     * @param desiredWeight in kg
     * @param tdee Total Daily Energy Expenditure
     * @return adjusted daily calorie goal
     */
    fun calculateDailyCalorieGoal(
        currentWeight: Int,
        desiredWeight: Int,
        tdee: Int
    ): Int {
        return when {
            // Weight loss: create 500 calorie deficit (lose ~0.5kg/week)
            desiredWeight < currentWeight -> {
                val deficit = 500
                maxOf(tdee - deficit, 1200) // Never go below 1200 calories
            }
            // Weight gain: create 500 calorie surplus (gain ~0.5kg/week)
            desiredWeight > currentWeight -> {
                val surplus = 500
                tdee + surplus
            }
            // Maintain weight
            else -> tdee
        }
    }

    /**
     * Calculate macronutrient distribution
     * @param totalCalories daily calorie goal
     * @return Triple of (protein in g, fat in g, carbs in g)
     */
    fun calculateMacros(totalCalories: Int): Triple<Int, Int, Int> {
        // Standard macro split: 30% protein, 30% fat, 40% carbs
        val proteinCalories = totalCalories * 0.30
        val fatCalories = totalCalories * 0.30
        val carbsCalories = totalCalories * 0.40

        // Convert calories to grams
        // Protein: 4 cal/g, Fat: 9 cal/g, Carbs: 4 cal/g
        val proteinGrams = (proteinCalories / 4).toInt()
        val fatGrams = (fatCalories / 9).toInt()
        val carbsGrams = (carbsCalories / 4).toInt()

        return Triple(proteinGrams, fatGrams, carbsGrams)
    }

    /**
     * Calculate all nutritional data at once
     */
    data class NutritionData(
        val bmr: Int,
        val tdee: Int,
        val dailyCalorieGoal: Int,
        val proteinGoal: Int,
        val fatGoal: Int,
        val carbsGoal: Int,
        val weeklyWeightChange: Double // kg per week
    )

    fun calculateNutritionPlan(
        gender: String,
        weight: Int,
        height: Int,
        age: Int,
        desiredWeight: Int,
        activityLevel: String
    ): NutritionData {
        val bmr = calculateBMR(gender, weight, height, age).toInt()
        val tdee = calculateTDEE(gender, weight, height, age, activityLevel)
        val dailyCalorieGoal = calculateDailyCalorieGoal(weight, desiredWeight, tdee)
        val (protein, fat, carbs) = calculateMacros(dailyCalorieGoal)

        // Calculate expected weekly weight change
        val calorieDeficit = tdee - dailyCalorieGoal
        val weeklyWeightChange = (calorieDeficit * 7) / 7700.0 // 7700 cal ≈ 1kg

        return NutritionData(
            bmr = bmr,
            tdee = tdee,
            dailyCalorieGoal = dailyCalorieGoal,
            proteinGoal = protein,
            fatGoal = fat,
            carbsGoal = carbs,
            weeklyWeightChange = weeklyWeightChange
        )
    }
}