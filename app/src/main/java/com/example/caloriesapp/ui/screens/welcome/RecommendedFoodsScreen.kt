package com.example.caloriesapp.ui.screens.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caloriesapp.BuildConfig
import com.example.caloriesapp.ai.ChatGPTService
import com.example.caloriesapp.ui.components.MonoFilledButton
import com.example.caloriesapp.ui.components.MonoOutlinedButton
import com.example.caloriesapp.ui.theme.MonoTypography
import com.example.caloriesapp.viewmodel.OnboardingViewModel

@Composable
fun RecommendedFoodsScreen(
    viewModel: OnboardingViewModel,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    // Collecting state from ViewModel

    val diseases by viewModel.diseases.collectAsState()
    val recommendations by viewModel.recommendations.collectAsState()

    // Remembering the AI service instance
    val ai = remember { ChatGPTService(apiKey = BuildConfig.OPENAI_KEY) }

    // Loading state
    var loading by remember { mutableStateOf(false) }

    // Side-effect to fetch recommendations when diseases change
    LaunchedEffect(diseases) {
        loading = true
        try {
            val list = viewModel.fetchRecommendations()
            val foodRecommendations = ai.recommendFoods(list)
            viewModel.updateRecommendations(foodRecommendations)
        } catch (e: Exception) {
            viewModel.updateRecommendations("Error fetching recommendations")
        } finally {
            loading = false
        }
    }

    // Scaffold to provide a basic Material Design layout
    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                MonoOutlinedButton(
                    text = "Back",
                    onClick = { onBack() },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                MonoFilledButton(
                    text = "Continue",
                    onClick = onContinue,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(32.dp))

            Text("Suggested Foods", style = MonoTypography.displaySmall)

            Spacer(Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (loading) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        text = recommendations ?: "No suggestions yet.",
                        style = MonoTypography.bodyLarge
                    )
                }
            }

            //Spacer(Modifier.height(120.dp))

        }
    }
}