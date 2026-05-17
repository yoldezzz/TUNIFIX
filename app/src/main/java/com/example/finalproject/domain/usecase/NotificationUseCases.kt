package com.example.finalproject.domain.usecase

import com.example.finalproject.domain.repository.NotificationRepository

class NotificationUseCases(private val notificationRepository: NotificationRepository) {
    suspend fun getNotificationsForUser(userId: String) = notificationRepository.getNotificationsForUser(userId)
    suspend fun sendNotification(notification: com.example.finalproject.domain.model.Notification) = notificationRepository.sendNotification(notification)
}