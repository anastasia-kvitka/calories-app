package com.example.caloriesapp.ui.screens

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.caloriesapp.R
import com.example.caloriesapp.ui.components.*
import com.example.caloriesapp.ui.theme.Black
import com.example.caloriesapp.ui.theme.White
import com.example.caloriesapp.ui.theme.MonoTypography
import com.example.caloriesapp.viewmodel.HomeViewModel

@Composable
fun HomeScreenWrapper(
    onCameraClick: () -> Unit,
    onAnalysisClick: () -> Unit,
    onSettingsClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val viewModel: HomeViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(context.applicationContext as Application) as T
            }
        }
    )

    val state by viewModel.state.collectAsState()

    HomeScreen(
        totalCalories = state.dailyCalorieGoal,
        caloriesLeft = state.caloriesRemaining,
        proteinLeft = state.proteinRemaining,
        fatLeft = state.fatRemaining,
        carbsLeft = state.carbsRemaining,
        currentStreak = state.streak,
        onCameraClick = onCameraClick,
        onAnalysisClick = onAnalysisClick,
        onSettingsClick = onSettingsClick
    )
}

@Composable
fun HomeScreen(
    totalCalories: Int = 1490,
    caloriesLeft: Int = 1490,
    proteinLeft: Int = 104,
    fatLeft: Int = 48,
    carbsLeft: Int = 160,
    currentStreak: Int = 3,
    onCameraClick: () -> Unit,
    onAnalysisClick: () -> Unit,
    onSettingsClick: () -> Unit = {},
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
            caloriesLeft = caloriesLeft,
            totalCalories = totalCalories,
            proteinLeft = proteinLeft,
            fatLeft = fatLeft,
            carbsLeft = carbsLeft,
            currentStreak = currentStreak,
            onCameraClick = onCameraClick,
            onSettingsClick = onSettingsClick
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
    currentStreak: Int,
    onCameraClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Today",
                style = MonoTypography.displaySmall,
                color = Black,
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = onSettingsClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = "Settings",
                    tint = Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        DaySelectorMono()

        Spacer(Modifier.height(20.dp))

        StreakRowMono(currentStreak)

        Spacer(Modifier.height(24.dp))

        CaloriesSummaryCard(
            caloriesLeft = caloriesLeft,
            totalCalories = totalCalories,
            proteinLeft = proteinLeft,
            fatLeft = fatLeft,
            carbsLeft = carbsLeft
        )

        Spacer(Modifier.height(28.dp))

        EmptyTodaySection(onCameraClick)

        Spacer(Modifier.height(80.dp)) // Bottom padding for nav bar
    }
}

@Composable
private fun DaySelectorMono() {
    val days = listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su")
    val dates = listOf("1", "2", "3", "4", "5", "6", "7")
    var selected by remember { mutableStateOf(2) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEachIndexed { index, day ->
            MonoDayChip(
                label = day,
                date = dates[index],
                selected = index == selected,
                isToday = index == 2,
                onClick = { selected = index }
            )
        }
    }
}

@Composable
private fun MonoDayChip(
    label: String,
    date: String,
    selected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(48.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (selected) Black else White
            )
            .border(
                width = if (isToday && !selected) 2.dp else 1.dp,
                color = if (isToday && !selected) Black else Color.Transparent,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            style = MonoTypography.labelSmall,
            color = if (selected) White else Black.copy(alpha = 0.6f),
            fontSize = 11.sp
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = date,
            style = MonoTypography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = if (selected) White else Black,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun StreakRowMono(streak: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(16.dp),
        border = MonoBorder
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🔥", fontSize = 28.sp)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = "$streak day streak",
                        style = MonoTypography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Black
                    )
                    Text(
                        text = "Keep going!",
                        style = MonoTypography.bodySmall,
                        color = Black.copy(alpha = 0.6f)
                    )
                }
            }

            // Hearts
            Row {
                repeat(3) {
                    Text("❤️", fontSize = 20.sp)
                    if (it < 2) Spacer(Modifier.width(4.dp))
                }
            }
        }
    }
}

@Composable
private fun CaloriesSummaryCard(
    caloriesLeft: Int,
    totalCalories: Int,
    proteinLeft: Int,
    fatLeft: Int,
    carbsLeft: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(24.dp),
        border = MonoBorder
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "$caloriesLeft",
                        color = Black,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "left of $totalCalories kcal",
                        style = MonoTypography.bodyMedium,
                        color = Black.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
            MonoDivider()
            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MacroMono("Protein", "${proteinLeft}g", "💪")
                MacroMono("Fat", "${fatLeft}g", "🥑")
                MacroMono("Carbs", "${carbsLeft}g", "🍞")
            }
        }
    }
}

// ==========================================
// MACRO ITEM
// ==========================================
@Composable
private fun MacroMono(label: String, value: String, emoji: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(emoji, fontSize = 24.sp)
        Spacer(Modifier.height(6.dp))
        Text(
            text = value,
            style = MonoTypography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Black
        )
        Text(
            text = label,
            style = MonoTypography.bodySmall,
            color = Black.copy(alpha = 0.6f)
        )
    }
}

// ==========================================
// EMPTY STATE SECTION
// ==========================================
@Composable
private fun EmptyTodaySection(onCameraClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No meals logged today",
            style = MonoTypography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Black
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Tap the camera to scan your meal",
            style = MonoTypography.bodyMedium,
            color = Black.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(32.dp))

        // Camera Button
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Black)
                .clickable(onClick = onCameraClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_camera),
                contentDescription = "Scan meal",
                tint = White,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}