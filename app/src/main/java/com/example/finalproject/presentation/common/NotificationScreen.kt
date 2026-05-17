package com.example.finalproject.presentation.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NotificationScreen(viewModel: NotificationViewModel) {
    val notifications = viewModel.notifications.collectAsState().value
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Notifications", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        notifications.forEach { notification ->
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(notification.message)
                    Text("Type: ${notification.type}")
                }
            }
        }
    }
}
