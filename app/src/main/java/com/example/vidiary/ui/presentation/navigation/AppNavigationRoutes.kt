package com.example.vidiary.ui.presentation.navigation

sealed class AppNavigationRoutes(val routeSchema: String) {
    data object Feed : AppNavigationRoutes("/feed")
    data object Camera : AppNavigationRoutes("/camera")
}