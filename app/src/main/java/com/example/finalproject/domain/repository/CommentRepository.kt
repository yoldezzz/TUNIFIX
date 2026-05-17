package com.example.finalproject.domain.repository

import com.example.finalproject.domain.model.Comment

interface CommentRepository {
    suspend fun addComment(comment: Comment): Boolean
    suspend fun getCommentsForIncident(incidentId: String): List<Comment>
}