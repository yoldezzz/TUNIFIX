package com.example.finalproject.domain.usecase

import com.example.finalproject.domain.repository.CommentRepository

class CommentUseCases(private val commentRepository: CommentRepository) {
    suspend fun addComment(comment: com.example.finalproject.domain.model.Comment) = commentRepository.addComment(comment)
    suspend fun getCommentsForIncident(incidentId: String) = commentRepository.getCommentsForIncident(incidentId)
}