package com.example.finalproject

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.finalproject.presentation.auth.AuthScreen
import com.example.finalproject.presentation.home.HomeScreen
import com.example.finalproject.presentation.auth.AuthViewModel
import com.example.finalproject.presentation.incident.IncidentViewModel
import com.example.finalproject.presentation.map.MapViewModel
import com.example.finalproject.presentation.dashboard.DashboardViewModel
import com.example.finalproject.presentation.common.NotificationViewModel

@Composable
fun MainNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    incidentViewModel: IncidentViewModel,
    mapViewModel: MapViewModel,
    dashboardViewModel: DashboardViewModel,
    notificationViewModel: NotificationViewModel
) {
    NavHost(navController = navController, startDestination = "auth") {
        composable("auth") { 
            AuthScreen(
                viewModel = authViewModel,
                onAuthSuccess = {
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            ) 
        }
        composable("home") { 
            HomeScreen(
                incidentViewModel = incidentViewModel,
                mapViewModel = mapViewModel,
                dashboardViewModel = dashboardViewModel
            )
        }
    }
}
