package com.example.finalproject.domain.repository

import com.example.finalproject.domain.model.Notification

interface NotificationRepository {
    suspend fun getNotificationsForUser(userId: String): List<Notification>
    suspend fun sendNotification(notification: Notification): Boolean
}