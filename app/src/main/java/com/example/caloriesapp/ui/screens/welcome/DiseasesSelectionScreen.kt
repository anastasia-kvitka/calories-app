package com.example.caloriesapp.ui.screens.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caloriesapp.ui.components.MonoFilterChip
import com.example.caloriesapp.ui.components.MonoFilledButton
import com.example.caloriesapp.ui.components.MonoOutlinedButton
import com.example.caloriesapp.ui.theme.MonoTypography
import com.example.caloriesapp.viewmodel.OnboardingViewModel

@Composable
fun DiseasesSelectionScreen(
    viewModel: OnboardingViewModel,
    onSave: (Set<String>) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {

    val state by viewModel.state.collectAsState()
    var diseases by remember { mutableStateOf(state.diseases) }

    val allDiseases = listOf(
        "Diabetes",
        "Hypertension",
        "High cholesterol",
        "Thyroid disorder",
        "Obesity",
        "PCOS",
        "Gastritis",
        "Celiac disease",
        "Lactose intolerance",
        "Kidney disease",
        "Heart disease",
        "Arthritis"
    ).sorted()

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
                        onSave(diseases)  // Save selection
                        onContinue()              // Navigate to next screen
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(40.dp))

            Text(
                "Health Condition",
                style = MonoTypography.displaySmall
            )

            Spacer(Modifier.height(12.dp))

            Text(
                "Select all that apply.",
                style = MonoTypography.bodyMedium
            )

            Spacer(Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                FlowRow(
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    allDiseases.forEach { disease ->
                        MonoFilterChip(
                            label = disease,
                            selected = disease in diseases,
                            onClick = {
                                diseases = if (disease in diseases)
                                    diseases - disease
                                else
                                    diseases + disease
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(120.dp))
        }
    }
}