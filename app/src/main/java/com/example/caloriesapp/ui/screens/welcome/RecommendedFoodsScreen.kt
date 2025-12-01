package com.example.caloriesapp.ui.screens.welcome

import androidx.compose.foundation.layout.*
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
    val diseases by viewModel.selectedDiseases.collectAsState()
    val recommendations by viewModel.recommendations.collectAsState()

    val ai = remember { ChatGPTService(apiKey = BuildConfig.OPENAI_KEY) }

    var loading by remember { mutableStateOf(false) }

    LaunchedEffect(diseases) {
        loading = true
        viewModel.fetchRecommendations { list ->
            ai.recommendFoods(list)     // GPT call
        }
        loading = false
    }

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

            if (loading) {
                CircularProgressIndicator()
            } else {
                Text(
                    text = recommendations ?: "No suggestions yet.",
                    style = MonoTypography.bodyLarge
                )
            }
        }
    }
}
