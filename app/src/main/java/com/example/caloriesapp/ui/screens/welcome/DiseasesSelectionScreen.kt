package com.example.caloriesapp.ui.screens.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.caloriesapp.data.UserPreferences
import com.example.caloriesapp.ui.components.MonoFilterChip
import com.example.caloriesapp.ui.components.MonoFilledButton
import com.example.caloriesapp.ui.components.MonoOutlinedButton
import com.example.caloriesapp.ui.theme.MonoTypography

@Composable
fun DiseasesSelectionScreen(
    initialSelection: Set<String> = emptySet(),
    onSave: (Set<String>) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    var selectedDiseases by remember { mutableStateOf(initialSelection) }

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
                        onSave(selectedDiseases)  // Save selection
                        onContinue()              // Navigate to next screen
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(32.dp))

            Text(
                "Health Condition",
                style = MonoTypography.displaySmall
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Select all that apply.",
                style = MonoTypography.bodyMedium
            )

            Spacer(Modifier.height(32.dp))

            FlowRow(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                allDiseases.forEach { disease ->
                    MonoFilterChip(
                        label = disease,
                        selected = disease in selectedDiseases,
                        onClick = {
                            selectedDiseases = if (disease in selectedDiseases)
                                selectedDiseases - disease
                            else
                                selectedDiseases + disease
                        }
                    )
                }
            }
        }
    }
}