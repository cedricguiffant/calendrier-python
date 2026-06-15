package com.lolinsight.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lolinsight.presentation.analysis.AnalysisScreen
import com.lolinsight.presentation.camera.CameraScreen
import com.lolinsight.presentation.history.HistoryScreen
import com.lolinsight.presentation.home.HomeScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Camera : Screen("camera")
    object Analysis : Screen("analysis?analysisId={analysisId}") {
        fun createRoute(analysisId: Long = -1L) = "analysis?analysisId=$analysisId"
    }
    object History : Screen("history")
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCamera = { navController.navigate(Screen.Camera.route) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) }
            )
        }

        composable(Screen.Camera.route) {
            CameraScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAnalysis = { analysisId ->
                    navController.navigate(Screen.Analysis.createRoute(analysisId)) {
                        popUpTo(Screen.Camera.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Analysis.route,
            arguments = listOf(
                navArgument("analysisId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val analysisId = backStackEntry.arguments?.getLong("analysisId") ?: -1L
            AnalysisScreen(
                analysisId = analysisId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCamera = {
                    navController.navigate(Screen.Camera.route) {
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAnalysis = { analysisId ->
                    navController.navigate(Screen.Analysis.createRoute(analysisId))
                }
            )
        }
    }
}
