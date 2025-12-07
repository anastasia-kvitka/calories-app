package com.example.caloriesapp

import android.app.Application
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.caloriesapp.ai.ChatGPTService
import com.example.caloriesapp.viewmodel.OnboardingViewModel
import com.example.caloriesapp.ui.screens.CameraScreen
import com.example.caloriesapp.ui.screens.CapturedImagePreview
import com.example.caloriesapp.ui.screens.HomeScreenWrapper
import com.example.caloriesapp.ui.screens.welcome.ActivityLevelScreen
import com.example.caloriesapp.ui.screens.welcome.AgeSelectionScreen
import com.example.caloriesapp.ui.screens.welcome.DesiredWeightSelectionScreen
import com.example.caloriesapp.ui.screens.welcome.DiseasesSelectionScreen
import com.example.caloriesapp.ui.screens.welcome.GenderSelectionScreen
import com.example.caloriesapp.ui.screens.welcome.HeightSelectionScreen
import com.example.caloriesapp.ui.screens.welcome.ProfileSummaryScreen
import com.example.caloriesapp.ui.screens.welcome.RecommendedFoodsScreen
import com.example.caloriesapp.ui.screens.welcome.WeightSelectionScreen
import com.example.caloriesapp.ui.screens.welcome.WelcomeScreen
import com.example.caloriesapp.ui.theme.MonoTheme
import com.example.caloriesapp.viewmodel.OnboardingViewModelFactory
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MonoTheme {
                AppNavigation()
            }
        }
    }
}

object Navigation {
    const val WELCOME = "welcome"
    const val GENDER = "gender"
    const val AGE = "age"
    const val HEIGHT = "height"
    const val WEIGHT = "weight"
    const val DESIRED_WEIGHT = "desiredWeight"
    const val ACTIVITY_LEVEL = "activityLevel"
    const val DISEASES = "diseases"
    const val PROFILE_SUMMARY = "profile_summary"
    const val RECOMMENDED = "recommended"
    const val HOME = "home"
    const val CAMERA = "camera"
    const val ANALYSIS = "analysis"
    const val SETTINGS = "settings"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val onboardingViewModel: OnboardingViewModel = viewModel(
        factory = OnboardingViewModelFactory(LocalContext.current.applicationContext as Application)
    )

    val onboardingState by onboardingViewModel.state.collectAsState()

    NavHost(navController = navController, startDestination = Navigation.WELCOME) {
        composable(Navigation.WELCOME) {
            WelcomeScreen(onContinue = { navController.navigate(Navigation.GENDER) { popUpTo(Navigation.WELCOME) { inclusive = true } } })
        }
        composable(Navigation.GENDER) {
            GenderSelectionScreen(
                viewModel = onboardingViewModel,
                onSave = { onboardingViewModel.saveGender(it) },
                onContinue = { navController.navigate(Navigation.AGE) }
            )
        }
        composable(Navigation.AGE) {
            AgeSelectionScreen(
                viewModel = onboardingViewModel,
                onSave = { onboardingViewModel.saveAge(it) },
                onContinue = { navController.navigate(Navigation.HEIGHT) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Navigation.HEIGHT) {
            HeightSelectionScreen(
                viewModel = onboardingViewModel,
                onSave = { onboardingViewModel.saveHeight(it) },
                onContinue = { navController.navigate(Navigation.WEIGHT) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Navigation.WEIGHT) {
            WeightSelectionScreen(
                viewModel = onboardingViewModel,
                onSave = { onboardingViewModel.saveWeight(it) },
                onContinue = { navController.navigate(Navigation.DESIRED_WEIGHT) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Navigation.DESIRED_WEIGHT) {
            DesiredWeightSelectionScreen(
                viewModel = onboardingViewModel,
                onSave = { onboardingViewModel.saveDesiredWeight(it) },
                onContinue = { navController.navigate(Navigation.ACTIVITY_LEVEL) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Navigation.ACTIVITY_LEVEL) {
            ActivityLevelScreen(
                viewModel = onboardingViewModel,
                onSave = { onboardingViewModel.saveActivityLevel(it) },
                onContinue = { navController.navigate(Navigation.DISEASES) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Navigation.DISEASES) {
            DiseasesSelectionScreen(
                viewModel = onboardingViewModel,
                onSave = { onboardingViewModel.saveDiseases(it) },
                onContinue = {
                    navController.navigate(Navigation.PROFILE_SUMMARY)
                },
                onBack = { navController.navigateUp() }
            )
        }
        composable(Navigation.PROFILE_SUMMARY) {
            ProfileSummaryScreen(
                viewModel = onboardingViewModel,
                onContinue = {
                    navController.navigate(Navigation.RECOMMENDED) {
                        popUpTo(Navigation.PROFILE_SUMMARY) { inclusive = true }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(Navigation.RECOMMENDED) {
            RecommendedFoodsScreen(
                onboardingViewModel,
                onContinue = { navController.navigate(Navigation.HOME) {
                    popUpTo(Navigation.RECOMMENDED) { inclusive = true }
                } },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Navigation.HOME) {
            HomeScreenWrapper(
                onCameraClick = { navController.navigate(Navigation.CAMERA) },
                onAnalysisClick = { navController.navigate(Navigation.ANALYSIS) },
                onSettingsClick = { navController.navigate(Navigation.SETTINGS) }
            )
        }
        composable(Navigation.CAMERA) {
            CameraScreen(
                onImageCaptured = { file ->
                    val encoded = Uri.encode(file.absolutePath)
                    navController.navigate("preview/$encoded")
                },
                onClose = { navController.popBackStack() }
            )
        }
        composable(route = "preview/{path}", arguments = listOf(navArgument("path") { type = NavType.StringType })) { entry ->
            val encodedPath = entry.arguments?.getString("path")!!
            val realPath = Uri.decode(encodedPath)
            val file = File(realPath)

            val analysisResult = remember { mutableStateOf<String?>(null) }

            CapturedImagePreview(
                file = file,
                onRetake = { navController.popBackStack() },
                onFinish = { navController.navigate(Navigation.HOME) { popUpTo(Navigation.CAMERA) { inclusive = true } } },
                onAnalyze = { imageFile ->
                    val ai = ChatGPTService(BuildConfig.OPENAI_KEY)
                    ai.analyzeFoodImage(imageFile) // <-- must return String
                }
            )

            LaunchedEffect(file) {
                val ai = ChatGPTService(BuildConfig.OPENAI_KEY)
                analysisResult.value = ai.analyzeFoodImage(file)
            }

            analysisResult.value?.let { result ->
                Text(text = result)
            }
        }
    }
}
