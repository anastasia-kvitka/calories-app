package com.example.caloriesapp.ui.screens.welcome

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caloriesapp.ui.components.MonoFilledButton
import com.example.caloriesapp.ui.components.MonoOutlinedButton
import com.example.caloriesapp.ui.components.MonoSelectableCard
import com.example.caloriesapp.ui.theme.MonoTypography
import com.example.caloriesapp.viewmodel.OnboardingViewModel

@Composable
fun ActivityLevelScreen(
    viewModel: OnboardingViewModel,
    onSave: (String) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var activityLevel by remember { mutableStateOf(state.activityLevel ?: "moderate") }

    // Sync with ViewModel when navigating back
    LaunchedEffect(state.activityLevel) {
        state.activityLevel?.let {
            activityLevel = it
        }
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
                    onClick = {
                        onSave(activityLevel)
                        onContinue()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))

            Text(
                "Activity Level",
                style = MonoTypography.displaySmall
            )

            Spacer(Modifier.height(12.dp))

            Text(
                "Select your typical daily activity level.\nThis helps calculate your calorie needs.",
                style = MonoTypography.bodyMedium
            )

            Spacer(Modifier.height(40.dp))

            // --- ACTIVITY OPTIONS ---

            // Sedentary
            MonoSelectableCard(
                label = "Sedentary",
                description = "Little or no exercise, desk job",
                selected = activityLevel == "sedentary",
                onClick = { activityLevel = "sedentary" }
            )

            Spacer(Modifier.height(12.dp))

            // Lightly Active
            MonoSelectableCard(
                label = "Lightly Active",
                description = "Light exercise 1-3 days/week",
                selected = activityLevel == "light",
                onClick = { activityLevel = "light" }
            )

            Spacer(Modifier.height(12.dp))

            // Moderately Active
            MonoSelectableCard(
                label = "Moderately Active",
                description = "Moderate exercise 3-5 days/week",
                selected = activityLevel == "moderate",
                onClick = { activityLevel = "moderate" }
            )

            Spacer(Modifier.height(12.dp))

            // Very Active
            MonoSelectableCard(
                label = "Very Active",
                description = "Hard exercise 6-7 days/week",
                selected = activityLevel == "very_active",
                onClick = { activityLevel = "very_active" }
            )

            Spacer(Modifier.height(12.dp))

            // Extremely Active
            MonoSelectableCard(
                label = "Extremely Active",
                description = "Very hard exercise, physical job or training twice/day",
                selected = activityLevel == "extremely_active",
                onClick = { activityLevel = "extremely_active" }
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}