package com.example.finalproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.finalproject.ui.theme.FinalprojectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinalprojectTheme {
                val navController = androidx.navigation.compose.rememberNavController()
                
                // Manual dependency injection for now
                val authViewModel = androidx.lifecycle.viewmodel.compose.viewModel {
                    com.example.finalproject.presentation.auth.AuthViewModel(
                        com.example.finalproject.domain.usecase.AuthUseCases(
                            com.example.finalproject.data.repository.FirebaseUserRepository()
                        )
                    )
                }
                
                val firestoreIncidentRepository = com.example.finalproject.data.repository.FirestoreIncidentRepository()
                
                val incidentViewModel = androidx.lifecycle.viewmodel.compose.viewModel {
                    com.example.finalproject.presentation.incident.IncidentViewModel(
                        com.example.finalproject.domain.usecase.IncidentUseCases(firestoreIncidentRepository)
                    )
                }
                
                val mapViewModel = androidx.lifecycle.viewmodel.compose.viewModel {
                    com.example.finalproject.presentation.map.MapViewModel(
                        com.example.finalproject.domain.usecase.IncidentUseCases(firestoreIncidentRepository)
                    )
                }
                
                val dashboardViewModel = androidx.lifecycle.viewmodel.compose.viewModel {
                    com.example.finalproject.presentation.dashboard.DashboardViewModel(
                        com.example.finalproject.domain.usecase.IncidentUseCases(firestoreIncidentRepository)
                    )
                }
                
                val notificationViewModel = androidx.lifecycle.viewmodel.compose.viewModel {
                    com.example.finalproject.presentation.common.NotificationViewModel(
                        com.example.finalproject.domain.usecase.NotificationUseCases(
                            object : com.example.finalproject.domain.repository.NotificationRepository {
                                override suspend fun getNotificationsForUser(userId: String) = emptyList<com.example.finalproject.domain.model.Notification>()
                                override suspend fun sendNotification(notification: com.example.finalproject.domain.model.Notification) = true
                            }
                        )
                    )
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        MainNavGraph(
                            navController = navController,
                            authViewModel = authViewModel,
                            incidentViewModel = incidentViewModel,
                            mapViewModel = mapViewModel,
                            dashboardViewModel = dashboardViewModel,
                            notificationViewModel = notificationViewModel
                        )
                    }
                }
            }
        }
    }
}
