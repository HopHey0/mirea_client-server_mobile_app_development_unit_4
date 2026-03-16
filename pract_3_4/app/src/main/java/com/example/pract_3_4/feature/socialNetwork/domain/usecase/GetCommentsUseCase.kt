package com.example.pract_3_4.feature.socialNetwork.domain.usecase

import com.example.pract_3_4.feature.socialNetwork.domain.model.CommentItem
import com.example.pract_3_4.feature.socialNetwork.domain.repository.SocialNetRepository
import kotlinx.coroutines.flow.Flow

class GetCommentsUseCase(private val repository: SocialNetRepository) {
    fun invoke(): List<CommentItem> = repository.getComments()
}