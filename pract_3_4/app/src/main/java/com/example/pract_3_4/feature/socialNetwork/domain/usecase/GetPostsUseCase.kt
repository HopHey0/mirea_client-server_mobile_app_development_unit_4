package com.example.pract_3_4.feature.socialNetwork.domain.usecase

import com.example.pract_3_4.feature.socialNetwork.domain.model.PostItem
import com.example.pract_3_4.feature.socialNetwork.domain.repository.SocialNetRepository
import kotlinx.coroutines.flow.Flow

class GetPostsUseCase(private val repository: SocialNetRepository) {
    fun invoke(): List<PostItem> = repository.getPosts()
}

