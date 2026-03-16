package com.example.pract_3_4.feature.socialNetwork.domain.repository

import com.example.pract_3_4.feature.repoSearcher.domain.model.RepoItem
import com.example.pract_3_4.feature.socialNetwork.domain.model.CommentItem
import com.example.pract_3_4.feature.socialNetwork.domain.model.PostItem
import kotlinx.coroutines.flow.Flow

interface SocialNetRepository {
    fun getPosts(): List<PostItem>

    fun getComments(): List<CommentItem>
}