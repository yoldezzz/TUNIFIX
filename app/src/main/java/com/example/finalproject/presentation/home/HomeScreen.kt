package com.example.finalproject.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.finalproject.presentation.auth.AuthViewModel
import com.example.finalproject.presentation.dashboard.DashboardScreen
import com.example.finalproject.presentation.incident.IncidentScreen
import com.example.finalproject.presentation.incident.IncidentViewModel
import com.example.finalproject.presentation.map.MapScreen
import com.example.finalproject.presentation.map.MapViewModel
import com.example.finalproject.presentation.dashboard.DashboardViewModel

@Composable
fun HomeScreen(
    incidentViewModel: IncidentViewModel,
    mapViewModel: MapViewModel,
    dashboardViewModel: DashboardViewModel
) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Edit, contentDescription = "Report") },
                    label = { Text("Report") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.LocationOn, contentDescription = "Map") },
                    label = { Text("Map") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Authority") },
                    label = { Text("Authority") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> IncidentScreen(incidentViewModel)
                1 -> MapScreen(mapViewModel)
                2 -> DashboardScreen(dashboardViewModel)
            }
        }
    }
}
