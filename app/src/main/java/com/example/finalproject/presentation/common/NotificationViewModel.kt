package com.example.finalproject.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.domain.model.Notification
import com.example.finalproject.domain.usecase.NotificationUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationViewModel(private val notificationUseCases: NotificationUseCases) : ViewModel() {
    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications

    fun loadNotifications(userId: String) {
        viewModelScope.launch {
            _notifications.value = notificationUseCases.getNotificationsForUser(userId)
        }
    }
}
