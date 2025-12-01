package com.example.caloriesapp

import android.app.Application
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.caloriesapp.ui.screens.HomeScreen
import com.example.caloriesapp.ui.screens.welcome.AgeSelectionScreen
import com.example.caloriesapp.ui.screens.welcome.DiseasesSelectionScreen
import com.example.caloriesapp.ui.screens.welcome.GenderSelectionScreen
import com.example.caloriesapp.ui.screens.welcome.HeightSelectionScreen
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

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val onboardingViewModel: OnboardingViewModel = viewModel(
        factory = OnboardingViewModelFactory(LocalContext.current.applicationContext as Application)
    )



    val selectedDiseases by onboardingViewModel.diseases.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {

        composable("welcome") {
            WelcomeScreen(
                onContinue = {
                    navController.navigate("gender") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        }

        composable("gender") {
            GenderSelectionScreen(
                initialGender = onboardingViewModel.gender, // or load from DataStore/VM
                onContinue = { gender ->
                    onboardingViewModel.saveGender(gender)
                    navController.navigate("age")
                }
            )
        }

        composable("age") {
            AgeSelectionScreen(
                onSave = { viewModel.saveAge(it) },
                onContinue = { navController.navigate("weight") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("weight") {
            WeightSelectionScreen(
                onSave = { viewModel.saveWeight(it) },
                onContinue = { navController.navigate("height") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("height") {
            HeightSelectionScreen(
                onSave = { viewModel.saveHeight(it) },
                onContinue = { navController.navigate("diseases") },
                onBack = { navController.popBackStack() }
            )
        }



        composable("diseases") {
            DiseasesSelectionScreen(
                initialSelection = selectedDiseases,
                onSave = { newSet ->
                    onboardingViewModel.updateDiseases(newSet)
                },
                onContinue = {
                    onboardingViewModel.updateDiseases(selectedDiseases)
                    navController.navigate("recommended")
                },
                onBack = { navController.navigateUp() }
            )
        }

        composable("recommended") {
            RecommendedFoodsScreen(
                onboardingViewModel,
                onContinue = {
                    navController.navigate("home") {
                        popUpTo("recommended") { inclusive = true }
                    }
                },
                onBack = { navController.navigateUp() }
            )
        }


        // -------------------
        // HOME SCREEN
        // -------------------
        composable("home") {
            HomeScreen(
                onCameraClick = {
                    navController.navigate("camera")
                },
                onAnalysisClick = {
                    navController.navigate("analysis") // optional future screen
                }
            )
        }

        // -------------------
        // CAMERA SCREEN
        // -------------------
        composable("camera") {
            CameraScreen(
                onImageCaptured = { file ->
                    val encoded = Uri.encode(file.absolutePath)
                    navController.navigate("preview/$encoded")
                },
                onClose = { navController.popBackStack() }
            )
        }

        // -------------------
        // PREVIEW SCREEN
        // -------------------
        composable(
            route = "preview/{path}",
            arguments = listOf(
                navArgument("path") { type = NavType.StringType }
            )
        ) { entry ->

            val encodedPath = entry.arguments?.getString("path")!!
            val realPath = Uri.decode(encodedPath)
            val file = File(realPath)

            // → GPT will be called *inside CapturedImagePreview* using LaunchedEffect
            CapturedImagePreview(
                file = file,
                onRetake = { navController.popBackStack() },
                onFinish = {
                    navController.navigate("home") {
                        popUpTo("camera") { inclusive = true }
                    }
                },
                onAnalyze = { imageFile ->
                    val ai = ChatGPTService(BuildConfig.OPENAI_KEY)
                    ai.analyzeFoodImage(imageFile) // <-- must return String
                }
            )
        }
    }
}
