package com.example.vidiary.ui.presentation.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vidiary.ui.presentation.camera.CameraScreen
import com.example.vidiary.ui.presentation.feed.FeedScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppNavigationRoutes.Feed.routeSchema) {
        composable(AppNavigationRoutes.Feed.routeSchema) {
            FeedScreen(
                onNavigateToCamera = { navController.navigate(AppNavigationRoutes.Camera.routeSchema) }
            )
        }
        composable(AppNavigationRoutes.Camera.routeSchema) {
            CameraScreen(
                onVideoSaved = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
