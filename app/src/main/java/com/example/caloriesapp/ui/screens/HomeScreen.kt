package com.example.caloriesapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import com.example.caloriesapp.R
import com.example.caloriesapp.ui.theme.Black
import com.example.caloriesapp.ui.theme.White
import com.example.caloriesapp.ui.components.*
import com.example.caloriesapp.ui.components.BottomNavItem
import com.example.caloriesapp.ui.theme.MonoTypography

@Composable
fun HomeScreen(
    totalCalories: Int = 1490,
    caloriesLeft: Int = 1490,
    proteinLeft: Int = 104,
    fatLeft: Int = 48,
    carbsLeft: Int = 160,
    onCameraClick: () -> Unit,
    onAnalysisClick: () -> Unit,
    onStartTodayClick: () -> Unit = {},
    onChangeGoalClick: () -> Unit = {},
) {
    var selectedTab by remember { mutableStateOf("home") }

    val navItems = listOf(
        BottomNavItem("Home", "home", R.drawable.ic_home),
        BottomNavItem("Scan", "camera", R.drawable.ic_camera),
        BottomNavItem("Analysis", "analysis", R.drawable.ic_stats),
    )

    MonoScreenWithBottomBar(
        bottomBar = {
            MonoBottomNav(
                items = navItems,
                selectedRoute = selectedTab,
                onItemClick = { item ->
                    selectedTab = item.route
                    when (item.route) {
                        "camera" -> onCameraClick()
                        "analysis" -> onAnalysisClick()
                    }
                }
            )
        }
    ) {
        HomeScreenContent(
            caloriesLeft,
            totalCalories,
            proteinLeft,
            fatLeft,
            carbsLeft,
            onStartTodayClick,
            onChangeGoalClick,
            onCameraClick
        )
    }
}

@Composable
private fun HomeScreenContent(
    caloriesLeft: Int,
    totalCalories: Int,
    proteinLeft: Int,
    fatLeft: Int,
    carbsLeft: Int,
    onStartTodayClick: () -> Unit,
    onChangeGoalClick: () -> Unit,
    onCameraClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
            .background(White),
        horizontalAlignment = Alignment.Start
    ) {

        // Title Row
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            //MonoChip("Start today", ) { onStartTodayClick() }
            Icon(
                painter = painterResource(R.drawable.ic_settings),
                contentDescription = null,
                tint = Black,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        DaySelectorMono()

        Spacer(Modifier.height(16.dp))

        StreakRowMono()

        Spacer(Modifier.height(20.dp))

        CaloriesSummaryCard(
            caloriesLeft = caloriesLeft,
            totalCalories = totalCalories,
            proteinLeft = proteinLeft,
            fatLeft = fatLeft,
            carbsLeft = carbsLeft,
            onChangeGoalClick = onChangeGoalClick
        )

        Spacer(Modifier.height(28.dp))

        EmptyTodaySection(onCameraClick)
    }
}

// ----------------------
//   DAY SELECTOR (Mono)
// ----------------------
@Composable
private fun DaySelectorMono() {
    val days = listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su")
    var selected by remember { mutableStateOf(2) }

    Row(
        Modifier
            .fillMaxWidth()
            .height(42.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEachIndexed { index, day ->
            MonoDayChip(
                label = day,
                selected = index == selected,
                onClick = { selected = index }
            )
        }
    }
}

// ----------------------
//     STREAK ROW
// ----------------------
@Composable
private fun StreakRowMono() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("❤️ ❤️ ❤️", fontSize = 20.sp)
        Spacer(Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(White),
            contentAlignment = Alignment.Center
        ) {
            Text("🍅", fontSize = 20.sp)
        }
    }
}

// ----------------------
//   CALORIES SUMMARY
// ----------------------
@Composable
private fun CaloriesSummaryCard(
    caloriesLeft: Int,
    totalCalories: Int,
    proteinLeft: Int,
    fatLeft: Int,
    carbsLeft: Int,
    onChangeGoalClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(24.dp),
        border = MonoBorder
    ) {
        Column(Modifier.padding(18.dp)) {

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("$caloriesLeft", color = Black, fontSize = 36.sp)
                    Text(
                        "$caloriesLeft left of $totalCalories",
                        color = Black.copy(alpha = 0.6f)
                    )
                }

                MonoOutlinedButton(
                    "Change goal",
                    onClick = { onChangeGoalClick() }
                )
            }

            Spacer(Modifier.height(16.dp))
            MonoDivider()
            Spacer(Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                MacroMono("Protein", "${proteinLeft}g")
                MacroMono("Fat", "${fatLeft}g")
                MacroMono("Carbs", "${carbsLeft}g")
            }
        }
    }
}

// ----------------------
//     MACRO ITEM
// ----------------------
@Composable
private fun MacroMono(label: String, value: String) {
    Column {
        Text(value, style = MonoTypography.titleMedium)
        Text(label, style = MonoTypography.bodyMedium)
    }
}

// ----------------------
//     EMPTY STATE
// ----------------------
@Composable
private fun EmptyTodaySection(onCameraClick: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Nothing added today.", style = MonoTypography.bodyMedium)
        Text("Take a photo of your meal.", style = MonoTypography.bodyMedium)

        Spacer(Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_camera),
                contentDescription = "Scan meal",
                tint = Black,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}
