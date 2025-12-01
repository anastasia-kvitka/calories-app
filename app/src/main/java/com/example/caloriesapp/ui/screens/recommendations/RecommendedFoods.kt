package com.example.caloriesapp.ui.screens.recommendations

data class FoodSuggestion(
    val food: String,
    val reason: String
)

fun recommendFoodsForDiseases(diseases: Set<String>): List<FoodSuggestion> {

    val map = mapOf(
        "Diabetes" to listOf(
            FoodSuggestion("Avocado", "Low glycemic impact and stabilizes blood sugar."),
            FoodSuggestion("Eggs", "Excellent low-carb protein source."),
            FoodSuggestion("Chickpeas", "High in fiber, slows glucose absorption."),
        ),

        "Hypertension" to listOf(
            FoodSuggestion("Bananas", "High potassium helps balance sodium."),
            FoodSuggestion("Beetroot", "Naturally lowers blood pressure."),
            FoodSuggestion("Oats", "Beta-glucans help reduce BP."),
        ),

        "High cholesterol" to listOf(
            FoodSuggestion("Almonds", "Raise HDL and lower LDL."),
            FoodSuggestion("Olive oil", "Heart-healthy monounsaturated fats."),
            FoodSuggestion("Salmon", "Rich in omega-3 fatty acids."),
        ),

        "Thyroid disorder" to listOf(
            FoodSuggestion("Yogurt", "Supports thyroid hormone conversion."),
            FoodSuggestion("Brazil nuts", "High selenium supports T3 production."),
        ),

        "Obesity" to listOf(
            FoodSuggestion("Leafy greens", "High volume, low calories."),
            FoodSuggestion("Greek yogurt", "High protein, increases satiety."),
        ),

        "PCOS" to listOf(
            FoodSuggestion("Berries", "Low sugar and antioxidant rich."),
            FoodSuggestion("Lentils", "Stabilize insulin levels."),
        ),

        "Gastritis" to listOf(
            FoodSuggestion("Boiled potatoes", "Gentle on the stomach lining."),
            FoodSuggestion("Oatmeal", "Reduces gastric irritation."),
        ),

        "Celiac disease" to listOf(
            FoodSuggestion("Quinoa", "Gluten-free complete protein."),
            FoodSuggestion("Sweet potatoes", "Gut-friendly carbohydrates."),
        ),

        "Lactose intolerance" to listOf(
            FoodSuggestion("Lactose-free yogurt", "Easier digestion with probiotics."),
            FoodSuggestion("Hard cheese", "Very low lactose."),
        ),

        "Kidney disease" to listOf(
            FoodSuggestion("Cauliflower", "Low in potassium and phosphorus."),
            FoodSuggestion("Rice", "Kidney-friendly carb source."),
        ),

        "Heart disease" to listOf(
            FoodSuggestion("Oats", "Helps reduce LDL cholesterol."),
            FoodSuggestion("Blueberries", "Rich in heart-protective antioxidants."),
        ),

        "Arthritis" to listOf(
            FoodSuggestion("Turmeric", "Strong anti-inflammatory benefits."),
            FoodSuggestion("Fatty fish", "Omega-3 reduces joint inflammation."),
        )
    )

    if (diseases.isEmpty()) {
        return listOf(
            FoodSuggestion("Yogurt", "Good for digestion and protein intake."),
            FoodSuggestion("Oats", "Stable energy and cholesterol benefits."),
            FoodSuggestion("Salmon", "High omega-3 for heart & brain health."),
        )
    }

    return diseases.flatMap { map[it].orEmpty() }.distinctBy { it.food }
}
