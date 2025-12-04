package com.example.caloriesapp.ui.screens.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caloriesapp.ui.components.MonoFilledButton
import com.example.caloriesapp.ui.theme.MonoTypography
import com.example.caloriesapp.ui.components.MonoSelectableCard
import com.example.caloriesapp.viewmodel.OnboardingViewModel

@Composable
fun GenderSelectionScreen(
    viewModel: OnboardingViewModel,
    onContinue: () -> Unit,
    onSave: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    var gender by remember { mutableStateOf(state.gender ?: "male") }

    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                MonoFilledButton(
                    text = "Continue",
                    onClick = {
                        onSave(gender)
                        onContinue() },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(40.dp))

            Text(
                "Select gender",
                style = MonoTypography.displaySmall
            )

            Spacer(Modifier.height(12.dp))

            Text(
                "Choose the option you identify with.\nThis helps improve macro recommendations.",
                style = MonoTypography.bodyMedium
            )

            Spacer(Modifier.height(40.dp))

            // --- OPTIONS ---
            MonoSelectableCard(
                label = "Male",
                selected = gender == "male",
                onClick = { gender = "male" }
            )

            Spacer(Modifier.height(12.dp))

            MonoSelectableCard(
                label = "Female",
                selected = gender == "female",
                onClick = { gender = "female" }
            )

            Spacer(Modifier.height(12.dp))

            MonoSelectableCard(
                label = "Other",
                selected = gender == "other",
                onClick = { gender = "other" }
            )
        }
    }
}
